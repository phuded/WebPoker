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
class GameService {

    @Autowired
    GameRepository gameRepository

    @Autowired
    HandDetector handDetector

    @Autowired
    RoundWinnerDetector roundWinnerDetector

    // Start a new Game
    Game createNewGame(List<String> playerNames, int startingFunds){
        Game game = new Game(playerNames,startingFunds)

        gameRepository.save(game)

        return game
    }

    // Play the next round
    def startNextRound(Game game){

        if(game.rounds.size() < Game.tempRoundLimit){
            //Reset players cards and hands
            game.players*.resetBetweenRounds()

            //New round and play
            Round round = new Round(game)
            game.rounds << round

            println "Saving... new round"
            gameRepository.save(game)

            playRound(game, round)
        }
    }

    //Play the round
    def playRound(Game game, Round round){

        println "================================"
        println "MAIN: New Round - " + game.rounds.size()
        println "================================"

        //Player betting rounds
        playBettingRounds(game, round)

        //Detect winners
        detectRoundWinners(round)

        println "Saving..."
        gameRepository.save(game)

        //Seems to work TODO - Add winnings to winners!
        round.winners.each { Player winner ->
            winner.funds += (round.pot/round.winners.size())
        }

        println "Saving... final"
        gameRepository.save(game)

        //Finish and play next round
        startNextRound(game)
    }

    def playBettingRounds(Game game, Round round){

        println "Saving..."
        gameRepository.save(game)

        //TODO: Refactor
        for(int roundNum = 0; roundNum< round.bettingRounds.size(); roundNum++){

            //Current round
            BettingRound currentBettingRound = round.bettingRounds[roundNum]

            //Deal cards
            currentBettingRound.dealCards(game,round)

            println "Saving..."
            gameRepository.save(game)

            //Bet!
            currentBettingRound.beginBetting(round)

            println "Saving..."
            gameRepository.save(game)

            //Finish round - and check if only 1 player remains
            if(completeBettingRound(game,round,currentBettingRound)){
                //Finish whole round
                break
            }
        }
    }

    //Complete the betting round
    boolean completeBettingRound(Game game, Round round, BettingRound bettingRound){
        int bettingRoundPot = bettingRound.getPot(game)
        round.pot += bettingRoundPot

        println "Betting round pot: " + bettingRoundPot + ". Total: " + round.pot

        //Reset ALL players after betting round  - including amountBet!
        game.players*.resetBetweenBettingRounds()

        //Check if > 1 player left
        return (round.roundPlayers.size()>1)?false:true
    }


    //Detect the round winner(s)
    def detectRoundWinners(Round round){

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
    }
}
