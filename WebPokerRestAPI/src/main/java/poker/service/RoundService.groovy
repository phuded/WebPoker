package poker.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import poker.domain.request.BetRequest
import poker.domain.game.Game
import poker.domain.game.bettinground.BettingRound
import poker.domain.game.round.Round
import poker.domain.player.GamePlayer
import poker.exception.PokerException
import poker.repository.GameRepository

/**
 * Created by matt on 21/05/2014.
 */
@Service
class RoundService {

    static final Logger logger = LoggerFactory.getLogger(RoundService.class)

    @Autowired
    GameRepository gameRepository

    @Autowired
    HandDetector handDetector

    @Autowired
    RoundWinnerDetector roundWinnerDetector

    @Autowired
    BettingRoundService bettingRoundService


    /**
     * Create a new round and set it to current
     * @param game
     * @return
     */
    Round createNextRound(Game game){

        int numberOfRounds = game.rounds.size()

        //Create new round and play
        Round round = new Round(game, numberOfRounds + 1)
        game.rounds << round

        //Set to current
        round.isCurrent = true

        //Set first betting round to current
        round.bettingRounds.first().isCurrent = true

        //Set the first player to current
        GamePlayer firstPlayer = game.players.first()
        firstPlayer.isCurrent = true
        round.currentPlayerName = firstPlayer.name

        //Deal the cards
        round.bettingRounds.first().dealCards(game, round)

        logger.info("Saving new Round: " + round.roundNumber)
        gameRepository.save(game)

        return round

    }


    /**
     * Update the round
     * @param game
     * @param round
     * @param player
     * @param amountBet
     * @return
     */
    Round updateRound(Game game, Round round, BetRequest betRequest, String playerName){

        //Check if round finished
        if(round.hasFinished){
            throw new PokerException("Round already finished")
        }

        //Get Current Betting Round
        BettingRound currentBettingRound = round.currentBettingRound

        //Get the current player
        GamePlayer currentPlayer = game.currentPlayer

        //Check there is a match
        if(currentPlayer.name == playerName){

            //Actually Bet
            bettingRoundService.makePlayerBet(currentPlayer, currentBettingRound, betRequest)

            //Check if betting round finished
            boolean bettingRoundFinished = bettingRoundService.hasBettingRoundFinished(game,currentBettingRound)

            if(bettingRoundFinished){
                //Finish the betting round
                bettingRoundService.finishBettingRound(game,round,currentBettingRound)

                //Check if only one player left
                if(onePlayerRemaining(game)){
                    //Finish the round
                    finishRound(game, round)
                }
                else{
                    //Set next betting round
                    BettingRound nextBettingRound = bettingRoundService.setNextBettingRound(game,round,currentBettingRound)

                    //If last betting round
                    if(nextBettingRound == null){
                       //Finish the round
                      finishRound(game,round)
                    }
                }
            }
            else{

                //Make next person current player
                bettingRoundService.setNextPlayer(game, round)
            }

            return round

        }
        else{
            throw new PokerException("Invalid player: " + playerName + " - not the current Player.")
        }

    }


    /**
     * Finish the round (and detect winners)
     * @param game
     * @param round
     * @return
     */
    def finishRound(Game game, Round round){

        if(game.nonFoldedPlayers.size() > 1){
            logger.info("================================")

            //Detect hands...
            game.nonFoldedPlayers.each{ GamePlayer player ->

                //Get the player's hands
                handDetector.detectHand(player)

                // logger.info("Player: " + player.name + " - All hand-results: " + player.hands)
                logger.info("Player: " + player.name + " - Best hand: " + player.bestHand)

            }

            logger.info("================================")

            //Get winner
            round.winners = roundWinnerDetector.detectWinners(game.nonFoldedPlayers)
        }
        else{
            //Winner is last player
            round.winners = [game.nonFoldedPlayers.first()]
        }

        logger.info("Round Winners: " + round.winners)


        //Pay winners
        round.winners.each { GamePlayer winner ->
            winner.funds += (round.pot/round.winners.size())
        }

        //Close the round
        round.close();

        //Shift the players
        shiftPlayers(game)

        //Reset players cards and hands
        game.players*.resetBetweenRounds()

        logger.info("Round finished: " + round.roundNumber + " - saving.")
        gameRepository.save(game)
    }

    /**
     * Shift the player order
     * @param game
     */
    void shiftPlayers(Game game){
        //Shift
        game.players = game.players[1..-1,0]

        //Re-allocate order
        game.players.eachWithIndex{ GamePlayer player, int i ->
            player.order = i
        }

        logger.info("Saving after shift: " + game.players)
        gameRepository.save(game)
    }

    /**
     * Check if thee parent round has finished
     * @param round
     * @return
     */
    boolean onePlayerRemaining(Game game){
        //Check if > 1 player left
        return !(game.nonFoldedPlayers.size()>1)
    }

}
