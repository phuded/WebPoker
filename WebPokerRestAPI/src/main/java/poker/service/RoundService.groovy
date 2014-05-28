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
    def playRound(Game game, Round round){

        println "================================"
        println "MAIN: New Round - " + game.rounds.size()
        println "================================"

        //Player betting rounds
        executeRound(game, round)

        //Detect winners
        detectRoundWinners(game,round)

        //Pay Winners
        payRoundWinners(game,round)

        //Finish and play next round
        gameService.startNextRound(game)
    }

    //Play the betting rounds
    def executeRound(Game game, Round round){

        //Loop through each betting round
        round.bettingRounds.any { BettingRound currentBettingRound ->

            //Can break out of round as round might finish before all cards are dealt (1 player remaining)
            return bettingRoundService.executeBettingRound(game,round,currentBettingRound)
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
    def detectRoundWinners(Game game,Round round){

        if(round.roundPlayers.size() > 1){
            println "================================"

            //Detect hands...
            round.roundPlayers.each{ Player player ->
                handDetector.detectHand(player)
                // println "MAIN: "+ player.name + " - All hand-results: " + player.hands
                println "MAIN: "+ player.name + " - Best hand: " + player.bestHand

            }

            println "================================"

            //Get winner
            round.winners = roundWinnerDetector.detectWinners(round.roundPlayers)
        }
        else{
            //Winner is last player
            round.winners =  [round.roundPlayers.first()]
        }

        println "MAIN: Winners: " + round.winners


        println "Saving..."
        gameRepository.save(game)
    }

    /**
     * Pay the winner(s) of the round
     * @param game
     * @param round
     * @return
     */
    def payRoundWinners(Game game, Round round){
        //Seems to work
        round.winners.each { Player winner ->
            winner.funds += (round.pot/round.winners.size())
        }

        println "Saving... final"
        gameRepository.save(game)
    }
}
