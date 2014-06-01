package poker.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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
    Round updateRound(Game game, Round round, String player, String bet){

        //Check if round finished
        if(round.hasRoundFinished){
            throw new PokerException("Round already finished")
        }

        //Get Current Betting Round
        BettingRound currentBettingRound = bettingRoundService.getCurrentBettingRound(round)

        //Get current player
        Player currentPlayer = bettingRoundService.getCurrentPlayer(game)

        if(currentPlayer.name == player){

            //Actually Bet
            bettingRoundService.makePlayerBet(currentPlayer, currentBettingRound, bet)

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

                    println "Set next betting round. Saving.."
                    gameRepository.save(game)

                    //If last betting round
                    if(nextBettingRound == null){
                       //Finish the round
                      finishRound(game,round)
                    }
                }
            }
            else{

                //Make next person current player
                bettingRoundService.setNextPlayer(game)

                println "Set next player and saving.."
                gameRepository.save(game)
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
            println "================================"

            //Detect hands...
            game.getNonFoldedPlayers().each{ Player player ->

                //Get the player's hands
                handDetector.detectHand(player)

                // println "MAIN: "+ player.name + " - All hand-results: " + player.hands
                println "MAIN: " + player.name + " - Best hand: " + player.bestHand

            }

            println "================================"

            //Get winner
            round.winners = roundWinnerDetector.detectWinners(game.getNonFoldedPlayers())
        }
        else{
            //Winner is last player
            round.winners = [game.getNonFoldedPlayers().first()]
        }

        println "MAIN: Winners: " + round.winners


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

        //Clear the objects
        round.winners = null

        //Switch flag
        round.isCurrentRound = false

        round.hasRoundFinished = true

        println "Saving after round..."
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
