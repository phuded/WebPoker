package poker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import poker.domain.request.GameRequest
import poker.domain.game.Game
import poker.service.GameService

/**
 * Created by matt on 21/05/2014.
 */
@RestController
@RequestMapping("/games")
class GameControllerImpl implements GameController{

    @Autowired
    private GameService gameService

    /**
     * Create a new Game
     * @param players
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    Game createGame(@RequestBody GameRequest gameRequest) {

        //TODO: Remove
        gameService.clearDatabase()

        Game game = gameService.createNewGame(gameRequest)

        return game;
    }

    /**
     * List all Games
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    List<Game> getGames() {

        return gameService.getAllGames()
    }

    /**
     * Get a Game
     * @return
     */
    @RequestMapping(value="/{gameId}",method = RequestMethod.GET)
    Game getGame(@PathVariable String gameId) {

        return gameService.loadGame(gameId)
    }

}
