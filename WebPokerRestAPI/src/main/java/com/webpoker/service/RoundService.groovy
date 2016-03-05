package com.webpoker.service

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.webpoker.domain.request.BetRequest
import com.webpoker.domain.game.Game
import com.webpoker.domain.game.bettinground.BettingRound
import com.webpoker.domain.game.round.Round
import com.webpoker.domain.player.GamePlayer
import com.webpoker.exception.PokerException
import com.webpoker.repository.GameRepository

/**
 * Created by matt on 21/05/2014.
 */
@Slf4j
@Service
class RoundService {

    @Autowired
    GameRepository gameRepository

    @Autowired
    HandDetectorService handDetectorService

    @Autowired
    RoundWinnerDetectorService roundWinnerDetectorService

    @Autowired
    BettingRoundService bettingRoundService


    /**
     * Create a new round and set it to current
     * TODO: Refactor
     * @param game
     * @return
     */
    Round createNextRound(Game game){

        //TODO: Refactor this check
        if(!game.readyToStart()){
            throw new PokerException("Cannot start round since there are less than 2 players.")
        }

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

        round.currentPlayerName = firstPlayer.name

        //Deal the cards
        round.bettingRounds.first().dealCards(game, round)

        log.info("Saving new Round: " + round.roundNumber)
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
    Round updateRound(Game game, Round round, BetRequest betRequest, String requestPlayerName){

        //Check if round finished
        if(round.hasFinished){
            throw new PokerException("Round already finished")
        }

        //Get Current Betting Round
        BettingRound currentBettingRound = round.currentBettingRound

        //Get the current player
        GamePlayer currentPlayer = game.getPlayer(round.currentPlayerName)

        //Check there is not a match
        if(currentPlayer.name != requestPlayerName) {
            throw new PokerException("Error: " + requestPlayerName + " is not the current Player.")
        }

        //Actually Bet
        bettingRoundService.makePlayerBet(currentPlayer, currentBettingRound, betRequest)

        //Check if betting round finished
        boolean bettingRoundFinished = bettingRoundService.hasBettingRoundFinished(game, currentBettingRound)

        if(bettingRoundFinished){
            //Finish the betting round
            bettingRoundService.finishBettingRound(game, round, currentBettingRound)

            //Check if only one player left
            if(game.onePlayerRemaining){
                //Just finish the round
                finishRound(game, round)
            }
            else{
                //Set next betting round
                BettingRound nextBettingRound = bettingRoundService.setNextBettingRound(game,round,currentBettingRound)

                //If last betting round
                if(nextBettingRound == null){
                   //Finish the round
                  finishRound(game, round)
                }
            }
        }
        else{

            //Make next person current player
            GamePlayer nextPlayer = game.getNextPlayer(currentPlayer)

            round.currentPlayerName = nextPlayer.name

            gameRepository.save(game)
        }

        return round
    }


    /**
     * Finish the round (and detect winners)
     * @param game
     * @param round
     * @return
     */
    def finishRound(Game game, Round round){

        if(game.nonFoldedPlayers.size() > 1){
            log.info("================================")

            //Detect hands...
            game.nonFoldedPlayers.each{ GamePlayer player ->

                //Get the player's hands
                handDetectorService.detectHand(player)

                log.info("Player: " + player.name + " - Best hand: " + player.bestHand)

            }

            log.info("================================")

            //Get winner
            round.winners = roundWinnerDetectorService.detectWinners(game.nonFoldedPlayers)
        }
        else{
            //Winner is last player
            round.winners = [game.nonFoldedPlayers.first()]
        }

        log.info("Round Winners: " + round.winners)

        //Pay winners
        round.payWinners()

        //Close the round
        round.close()

        //Shift the players
        game.shiftPlayers()

        //Reset players cards and hands
        game.players*.resetBetweenRounds()

        log.info("Round finished: " + round.roundNumber + ". Saving.")

        gameRepository.save(game)
    }

}
