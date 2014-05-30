package poker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import poker.domain.game.Game
import poker.domain.game.round.Round
import poker.exception.PokerException
import poker.service.GameService

/**
 * Created by matt on 21/05/2014.
 */
@RestController
@RequestMapping("/games/{gameId}/rounds")
class RoundController {

    @Autowired
    private GameService gameService

    /**
     * Create a game round
     * @param gameId
     */
    @RequestMapping(method = RequestMethod.POST)
    Round createNewRound(@PathVariable String gameId){

        Game game = gameService.loadGame(gameId)

        Round currentRound = gameService.findCurrentRound(game);

        //If is a current round
        if(currentRound){
            throw new PokerException("Round already created.")
        }

        //Create new Round
        return gameService.createNextRound(game)
    }

    /**
     * Get the current Round
     * @param gameId
     */
    @RequestMapping(method = RequestMethod.GET)
    Round getCurrentRound(@PathVariable String gameId){

        Game game = gameService.loadGame(gameId)

        Round currentRound = gameService.findCurrentRound(game);

        //If is a current round
        if(!currentRound){
            throw new PokerException("No Round created.")
        }

        return currentRound
    }

    /**
     * Get round status -> current player, cards
     * @param gameId
     * @param roundId
     */
    @RequestMapping(value="/{roundId}",method = RequestMethod.GET)
    void checkRound(@PathVariable String gameId, @PathVariable String roundId){

    }

    /**
     * Make bets, fold etc
     * @param gameId
     * @param roundId
     */
    @RequestMapping(value="/{roundId}",method = RequestMethod.POST)
    void updateRound(@PathVariable String gameId, @PathVariable String roundId, String playerName){

    }




}
