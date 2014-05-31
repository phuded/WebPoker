package poker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import poker.domain.game.Game
import poker.domain.game.round.Round
import poker.exception.PokerException
import poker.exception.PokerRoundNotFoundException
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
     * Get all of the rounds for the game
     * @param gameId
     */
    @RequestMapping(method = RequestMethod.GET)
    List<Round> getRounds(@PathVariable String gameId){

        Game game = gameService.loadGame(gameId)

        return game.rounds
    }

    /**
     * Get the rounds for the game
     * @param gameId
     */
    @RequestMapping(value="/{roundNumber}",method = RequestMethod.GET)
    Round getRound(@PathVariable String gameId, @PathVariable Integer roundNumber){

        Game game = gameService.loadGame(gameId)

        List<Round> rounds = game.rounds;

        if(rounds.size() < roundNumber){
           throw new PokerRoundNotFoundException("No Round found.")
        }

        return rounds.get(--roundNumber);
    }

    /**
     * Get the current Round
     * @param gameId
     */
    @RequestMapping(value="/current",method = RequestMethod.GET)
    Round getCurrentRound(@PathVariable String gameId){

        Game game = gameService.loadGame(gameId)

        Round currentRound = gameService.findCurrentRound(game);

        //If there is not a current round
        if(!currentRound){
            throw new PokerRoundNotFoundException("No Current Round.")
        }

        return currentRound
    }

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
            throw new PokerException("A current round already exists.")
        }

        //Create new Round
        return gameService.createNextRound(game)
    }

    /**
     * Get round status -> current player, cards
     * @param gameId
     * @param roundId
     */
    @RequestMapping(value="/{roundId}/play",method = RequestMethod.GET)
    void checkRound(@PathVariable String gameId, @PathVariable String roundId){

    }

    /**
     * Make bets, fold etc
     * @param gameId
     * @param roundId
     */
    @RequestMapping(value="/{roundId}/play",method = RequestMethod.POST)
    void updateRound(@PathVariable String gameId, @PathVariable String roundId, String playerName){

    }




}
