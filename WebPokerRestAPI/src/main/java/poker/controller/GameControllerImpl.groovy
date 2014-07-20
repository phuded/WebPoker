package poker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import poker.domain.request.GameRequest
import poker.domain.game.Game
import poker.domain.security.PokerUser
import poker.service.GameService
import poker.service.security.PokerUserDetailsService

/**
 * Created by matt on 21/05/2014.
 */
@RestController
@RequestMapping("/games")
class GameControllerImpl implements GameController{

    @Autowired
    private GameService gameService

    /**
     * Create a new Game - Admin only
     * @param players
     * @return
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @RequestMapping(method = RequestMethod.POST)
    Game createGame(@RequestBody GameRequest gameRequest) {

        //Validate it
        gameRequest.validate()

        //TODO: Remove
        gameService.clearDatabase()

        return gameService.createNewGame(gameRequest)
    }

    /**
     * List all Games, based on game memberships
     * @return
     */
    @Override
    @RequestMapping(method = RequestMethod.GET)
    List<Game> getGames(@RequestParam(value = "showAll", required = false) boolean showAll) {

        if(showAll){
            return gameService.allGames
        }

        return gameService.getAllCurrentGames(PokerUserDetailsService.currentUserName)
    }

    /**
     * Get a Game - only return if user can access
     * @return
     */
    @Override
    @RequestMapping(value="/{gameId}", method = RequestMethod.GET)
    Game getGame(@PathVariable String gameId) {

        return gameService.loadGame(gameId, PokerUserDetailsService.currentUserName)
    }

    /**
     * Add a player to the game
     * @param gameId
     * @return
     */
    @Override
    @RequestMapping(value="/{gameId}/players", method = RequestMethod.POST)
    Game addPlayer(@PathVariable String gameId) {

        return gameService.addToPlayerToGame(gameId, PokerUserDetailsService.currentUser)

    }
}
