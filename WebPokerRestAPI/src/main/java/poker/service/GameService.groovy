package poker.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import poker.domain.player.Player
import poker.domain.request.GameRequest
import poker.domain.game.Game
import poker.exception.PokerNotFoundException
import poker.repository.GameRepository

import java.security.Principal

/**
 * Created by matt on 21/05/2014.
 */
@Service
class GameService {

    static final Logger logger = LoggerFactory.getLogger(GameService.class)

    @Autowired
    GameRepository gameRepository

    @Autowired
    PlayerService playerService

    /**
     * Create a new Game
     * @param playerNames
     * @param startingFunds
     * @return
     */
    Game createNewGame(GameRequest gameRequest){

        Game game = new Game(gameRequest.name, gameRequest.startingPlayerFunds)

        gameRepository.save(game)

        return game
    }

    /**
     * Load Game
     * @param gameId
     * @return
     */
    Game loadGame(String gameId){
       Game game = gameRepository.findOne(gameId)

       if(!game){
           throw new PokerNotFoundException("No game found with ID: " + gameId)
       }

       return game
    }

    /**
     * List all Games
     * @return
     */
    List<Game> getAllGames(){
        return gameRepository.findAll()
    }

    /**
     * Add a player to a game
     * @param game
     * @param player
     * @return
     */
    Game addToPlayerToGame(String gameId, Principal principal){

        Game game = gameRepository.findOne(gameId)

        Player player = playerService.loadPlayerByName(principal.getName())

        game.addPlayer(player);

        gameRepository.save(game)

        return game
    }

    //TODO Remove later
    def clearDatabase(){
        gameRepository.deleteAll()
    }
}
