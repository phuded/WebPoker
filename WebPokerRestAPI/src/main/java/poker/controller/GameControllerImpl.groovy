package poker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import poker.domain.request.GameRequest
import poker.domain.game.Game
import poker.domain.security.PokerUser
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
    @Override
    @RequestMapping(method = RequestMethod.POST)
    Game createGame(@RequestBody GameRequest gameRequest) {

        //Validate it
        gameRequest.validate()

        //TODO: Remove
        gameService.clearDatabase()

        return gameService.createNewGame(gameRequest)
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
    Game addPlayer(@PathVariable String gameId) {

        //Get logged in user
        PokerUser player = (PokerUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return gameService.addToPlayerToGame(gameId, player)

    }
}
