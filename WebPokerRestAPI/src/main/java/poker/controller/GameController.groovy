package poker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import poker.domain.game.Game
import poker.service.GameService

/**
 * Created by matt on 21/05/2014.
 */
@RestController
@RequestMapping("/games")
class GameController {

    @Autowired
    private GameService gameService

    /**
     * Create a new Game
     * @param players
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    Game createGame(@RequestParam("player") List<String> players, @RequestParam(value="startingFunds",required = false) Integer startingFunds) {

        //TODO: Remove
        gameService.clearDatabase()

        println players

        Game game = gameService.createNewGame(players,startingFunds)

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

    /**
     * Start a game
     * @param gameId
     */
    @RequestMapping(value="/{gameId}",method = RequestMethod.POST)
    void startGame(@PathVariable String gameId){

        Game game = gameService.loadGame(gameId)

        gameService.createNextRound(game)

    }

    /**
     * TODO: Move this
     * Find and start the round
     * @param gameId
     */
    @RequestMapping(value="/{gameId}/round",method = RequestMethod.POST)
    void startRound(@PathVariable String gameId){

        Game game = gameService.loadGame(gameId)

        gameService.findAndPlayRound(game)

    }




}
