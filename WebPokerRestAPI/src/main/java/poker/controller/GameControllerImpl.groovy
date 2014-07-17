package poker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import poker.domain.request.GameRequest
import poker.domain.game.Game
import poker.domain.security.PokerUser
import poker.service.GameService
import poker.service.security.PokerUserDetailsService

/**
 * Created by matt on 21/05/2014.
 * TODO: Check Roles here
 */
@RestController
@RequestMapping("/games")
class GameControllerImpl implements GameController{

    @Autowired
    private GameService gameService

    /**
     * Create a new Game
     * TODO: Admin only
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
     * TODO: Need to get a list of all games user can access
     * @return
     */
    @Override
    @RequestMapping(method = RequestMethod.GET)
    List<Game> getGames() {

        return gameService.getAllGames()
    }

    /**
     * Get a Game
     * TODO: Security needed
     * @return
     */
    @Override
    @RequestMapping(value="/{gameId}",method = RequestMethod.GET)
    Game getGame(@PathVariable String gameId) {

        return gameService.loadGame(gameId)
    }

    /**
     * Add a player to the game
     * @param gameId
     * @return
     */
    @Override
    @RequestMapping(value="/{gameId}/players",method = RequestMethod.POST)
    Game addPlayer(@PathVariable String gameId) {

        //Get logged in user
        PokerUser player = PokerUserDetailsService.currentUser;

        return gameService.addToPlayerToGame(gameId, player)

    }
}
