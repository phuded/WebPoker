package poker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import poker.domain.player.Player
import poker.domain.request.GameRequest
import poker.domain.game.Game
import poker.service.GameService
import poker.service.PlayerService

/**
 * Created by matt on 21/05/2014.
 */
@RestController
@RequestMapping("/games")
class GameControllerImpl implements GameController{

    @Autowired
    private GameService gameService

    @Autowired
    private PlayerService playerService

    /**
     * Create a new Game
     * @param players
     * @return
     */
    @Override
    @RequestMapping(method = RequestMethod.POST)
    Game createGame(@RequestBody GameRequest gameRequest) {

        //Validate it
        gameRequest.validate()

        //TODO: Remove
        gameService.clearDatabase()

        Game game = gameService.createNewGame(gameRequest)

        return game;
    }

    /**
     * List all Games
     * @return
     */
    @Override
    @RequestMapping(method = RequestMethod.GET)
    List<Game> getGames() {

        return gameService.getAllGames()
    }

    /**
     * Get a Game
     * @return
     */
    @Override
    @RequestMapping(value="/{gameId}",method = RequestMethod.GET)
    Game getGame(@PathVariable String gameId) {

        return gameService.loadGame(gameId)
    }

    @Override
    @RequestMapping(value="/{gameId}/players",method = RequestMethod.POST)
    Game addPlayer(@PathVariable String gameId, @RequestParam String playerId) {

        Game game = getGame(gameId)

        Player player = playerService.loadPlayer(playerService)

        game.addPlayer(player)

        return game

    }
}
