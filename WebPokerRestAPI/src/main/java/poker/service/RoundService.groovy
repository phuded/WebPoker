package poker.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import poker.domain.game.Game
import poker.domain.game.bettinground.BettingRound
import poker.domain.game.round.Round
import poker.domain.player.Player
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
    GameService gameService

    @Autowired
    BettingRoundService bettingRoundService

    /**
     * Pay the round of poker
     * @param game
     * @param round
     * @return
     */
    def executeRound(Game game, Round round){

        println "================================"
        println "MAIN: Round - " + round.roundNumber
        println "================================"

        //Player betting rounds
        playRound(game, round)

        //Detect winners
        detectRoundWinners(game,round)

        println "Saving..."
        gameRepository.save(game)

        //Pay Winners
        payRoundWinners(round)

        //Close the round
        closeRound(round)

        println "Saving... final"
        gameRepository.save(game)
    }

    //Play the betting rounds
    def playRound(Game game, Round round){

        //Loop through each betting round
        round.bettingRounds.any { BettingRound currentBettingRound ->

            //Can break out of round as round might finish before all cards are dealt (1 player remaining)
            boolean roundComplete = bettingRoundService.executeBettingRound(game,round,currentBettingRound)

            return roundComplete
        }

        println "Saving after round..."
        gameRepository.save(game)
    }

    /**
     * Detect the round winner(s)
     * @param game
     * @param round
     * @return
     */
    def detectRoundWinners(Game game, Round round){


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

    }

    /**
     * Pay the winner(s) of the round
     * @param game
     * @param round
     * @return
     */
    def payRoundWinners(Round round){
        //Seems to work
        round.winners.each { Player winner ->
            winner.funds += (round.pot/round.winners.size())
        }

    }

    /**
     * Close the round
     * TODO: Move to round?
     */
    def closeRound(Round round){
        //Set Player Names and Best Hand
        round.winners.each {Player winner ->
            round.winningPlayerNames << winner.name
        }

        round.winningHand = round.winners.get(0).bestHand

        //Clear the objects
        round.winners = null

        //Switch flag
        round.isCurrentRound = false
    }
}
