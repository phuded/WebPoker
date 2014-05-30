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

    /**
     * Create a new Game
     * @param playerNames
     * @param startingFunds
     * @return
     */
    Game createNewGame(List<String> playerNames, Integer startingFunds){
        Game game = new Game(playerNames,startingFunds)

        gameRepository.save(game)

        return game
    }

    /**
     * Load Game
     * @param gameId
     * @return
     */
    Game loadGame(String gameId){
       return gameRepository.findOne(gameId)
    }

    /**
     * List all Games
     * @return
     */
    List<Game> getAllGames(){
        return gameRepository.findAll()
    }

    /**
     * Begin the Game TODO: Round Limited currently
     * @param game
     * @return
     */
    def createNextRound(Game game){

        if(game.rounds.size() < Game.tempRoundLimit){
            //Reset players cards and hands
            game.players*.resetBetweenRounds()

            //New round and play
            Round round = new Round(game)
            game.rounds << round

            //Set to current
            round.isCurrentRound = true

            println "Saving round status"
            gameRepository.save(game)
        }
    }

    /**
     * Find and play the current round
     * @param game
     * @return
     */
    def findAndPlayRound(Game game){
        //Get the current round
        Round round = game.rounds.find {Round round ->
           round.isCurrentRound
        }

        roundService.executeRound(game, round)
    }


    //TODO Sort
    def clearDatabase(){
        gameRepository.deleteAll()
    }
}
