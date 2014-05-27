package poker.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import poker.domain.game.Game
import poker.domain.game.round.Round
import poker.repository.GameRepository

/**
 * Created by matt on 21/05/2014.
 */
@Service
class GameService {

    @Autowired
    GameRepository gameRepository

    @Autowired
    RoundService roundService

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

            roundService.playRound(game, round)
        }
    }
}
