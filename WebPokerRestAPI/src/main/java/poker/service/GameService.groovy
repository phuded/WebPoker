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
     * Create a new round and set it to current TODO: Round Limited currently
     * @param game
     * @return
     */
    Round createNextRound(Game game){

        int numberOfRounds = game.rounds.size()

        if(numberOfRounds < Game.tempRoundLimit){
            //Reset players cards and hands
            game.players*.resetBetweenRounds()

            //New round and play
            Round round = new Round(game, numberOfRounds + 1)
            game.rounds << round

            //Set to current
            round.isCurrentRound = true

            println "Saving round status"
            gameRepository.save(game)

            //TODO...
            roundService.executeRound(game, round)

            return round
        }
    }

    /**
     * Find the current round
     * @param game
     * @return
     */
    Round findCurrentRound(Game game){
        //Get the current round
        Round round = game.rounds.find {Round round ->
            round.isCurrentRound
        }

        return round
    }

    //TODO Sort
    def clearDatabase(){
        gameRepository.deleteAll()
    }
}
