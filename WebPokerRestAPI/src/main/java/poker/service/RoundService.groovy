package poker.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import poker.domain.BetRequest
import poker.domain.game.Game
import poker.domain.game.bettinground.BettingRound
import poker.domain.game.round.Round
import poker.domain.player.Player
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
     * Update the round
     * @param game
     * @param round
     * @param player
     * @param amountBet
     * @return
     */
    Round updateRound(Game game, Round round, BetRequest betRequest){

        //Check if round finished
        if(round.hasFinished){
            throw new PokerException("Round already finished")
        }

        //Get Current Betting Round
        BettingRound currentBettingRound = bettingRoundService.getCurrentBettingRound(round)

        //Get the current player
        Player currentPlayer = bettingRoundService.getCurrentPlayer(game)

        //Check there is a match
        if(currentPlayer.name == betRequest.player){

            //Actually Bet
            bettingRoundService.makePlayerBet(currentPlayer, currentBettingRound, betRequest)

            //Check if betting round finished
            boolean bettingRoundFinished = bettingRoundService.hasBettingRoundFinished(game,currentBettingRound, currentPlayer)

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
            throw new PokerException("Invalid player")
        }

    }


    /**
     * Finish the round (and detect winners)
     * @param game
     * @param round
     * @return
     */
    def finishRound(Game game, Round round){


        if(game.getNonFoldedPlayers().size() > 1){
            logger.info("================================")

            //Detect hands...
            game.getNonFoldedPlayers().each{ Player player ->

                //Get the player's hands
                handDetector.detectHand(player)

                // logger.info("Player: " + player.name + " - All hand-results: " + player.hands)
                logger.info("Player: " + player.name + " - Best hand: " + player.bestHand)

            }

            logger.info("================================")

            //Get winner
            round.winners = roundWinnerDetector.detectWinners(game.getNonFoldedPlayers())
        }
        else{
            //Winner is last player
            round.winners = [game.getNonFoldedPlayers().first()]
        }

        logger.info("Round Winners: " + round.winners)


        //Pay winners
        round.winners.each { Player winner ->
            winner.funds += (round.pot/round.winners.size())
        }

        //Close the round
        //Set Player Names and Best Hand
        round.winners.each {Player winner ->
            round.winningPlayerNames << winner.name
        }

        round.winningHand = round.winners.get(0).bestHand

        //Clear the winners
        round.winners = null

        //Switch flag
        round.isCurrent = false

        //Remove the current player
        round.currentPlayer = null

        //Set finished
        round.hasFinished = true

        logger.info("Round finished: " + round.roundNumber + " - saving.")
        gameRepository.save(game)
    }

    /**
     * Check if thee parent round has finished
     * @param round
     * @return
     */
    boolean onePlayerRemaining(Game game){
        //Check if > 1 player left
        return (game.getNonFoldedPlayers().size()>1)?false:true
    }

}
