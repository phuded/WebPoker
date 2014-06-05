package poker.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import poker.domain.request.GameRequest
import poker.domain.game.Game
import poker.domain.game.round.Round
import poker.domain.player.Player
import poker.repository.GameRepository

/**
 * Created by matt on 21/05/2014.
 */
@Service
class GameService {

    static final Logger logger = LoggerFactory.getLogger(GameService.class)

    @Autowired
    GameRepository gameRepository

    /**
     * Create a new Game
     * @param playerNames
     * @param startingFunds
     * @return
     */
    Game createNewGame(GameRequest gameRequest){
        //Validate it
        gameRequest.validate();

        Game game = new Game(gameRequest.name, gameRequest.playerNames,gameRequest.startingPlayerFunds)

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
     * Find the current round
     * @param game
     * @return
     */
    Round findCurrentRound(Game game){
        //Get the current round
        Round round = game.rounds.find {Round round ->
            round.isCurrent
        }

        return round
    }

    //TODO Remove later
    def clearDatabase(){
        gameRepository.deleteAll()
    }
}
