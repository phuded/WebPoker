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
class GameController {

    @Autowired
    private GameService gameService

    /**
     * Create a new Game
     * @param players
     * @return
     */
    @RequestMapping(value="/games",method = RequestMethod.POST)
    Game createGame(@RequestParam("players") List<String> players, @RequestParam(value="amount",required = false) Long amount) {

        //TODO: Remove
        gameService.clearDatabase()

        println players

        Game game = gameService.createNewGame(players,amount)

        return game;
    }

    /**
     * List all Games
     * @return
     */
    @RequestMapping(value="/games",method = RequestMethod.GET)
    List<Game> getGames() {

        return gameService.getAllGames()
    }

    /**
     * Get a Game
     * @return
     */
    @RequestMapping(value="/games/{gameId}",method = RequestMethod.GET)
    Game getGame(@PathVariable String gameId) {

        return gameService.loadGame(gameId)
    }

    /**
     * Start a game
     * @param gameId
     */
    @RequestMapping(value="/games/{gameId}",method = RequestMethod.POST)
    void startGame(@PathVariable String gameId){

        Game game = gameService.loadGame(gameId)

        gameService.startNextRound(game)

    }


}
