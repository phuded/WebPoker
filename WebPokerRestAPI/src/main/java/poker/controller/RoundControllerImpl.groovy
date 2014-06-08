package poker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import poker.domain.request.BetRequest
import poker.domain.game.Game
import poker.domain.game.round.Round
import poker.exception.PokerException
import poker.exception.PokerNotFoundException
import poker.service.GameService
import poker.service.NotificationService
import poker.service.RoundService

/**
 * Created by matt on 21/05/2014.
 */
@RestController
@RequestMapping("/games/{gameId}/rounds")
class RoundControllerImpl implements RoundController{

    @Autowired
    GameService gameService

    @Autowired
    RoundService roundService

    @Autowired
    NotificationService notificationService

    /**
     * Get all of the rounds for the game
     * @param gameId
     */
    @RequestMapping(method = RequestMethod.GET)
    List<Round> getRounds(@PathVariable String gameId, @RequestParam String playerName){

        Game game = gameService.loadGame(gameId)

        return game.getRounds(playerName)
    }

    /**
     * Create a game round
     * @param gameId
     */
    @RequestMapping(method = RequestMethod.POST)
    Round createNewRound(@PathVariable String gameId, @RequestParam String playerName){

        Game game = gameService.loadGame(gameId)

        Round currentRound = game.currentRound

        //If is a current round
        if(currentRound){
            throw new PokerException("A current round already exists.")
        }

        //Create new Round
        Round newRound = roundService.createNextRound(game)

        return newRound.filter(playerName,game)
    }

    /**
     * Get the round in the game
     * @param gameId
     */
    @RequestMapping(value="/{roundNumber}",method = RequestMethod.GET)
    Round getRound(@PathVariable String gameId, @PathVariable Integer roundNumber, @RequestParam String playerName){

        Game game = gameService.loadGame(gameId)

        List<Round> rounds = game.rounds;

        if(rounds.size() < roundNumber){
           throw new PokerNotFoundException("No Round found: " + roundNumber)
        }

        Round round = rounds.get(--roundNumber);

        return round.filter(playerName, game)
    }

    /**
     * Get the current Round
     * @param gameId
     */
    @RequestMapping(value="/current",method = RequestMethod.GET)
    Round getCurrentRound(@PathVariable String gameId, @RequestParam String playerName){

        Game game = gameService.loadGame(gameId)

        Round currentRound = game.currentRound

        //If there is not a current round
        if(!currentRound){
            throw new PokerNotFoundException("No Current Round.")
        }

        return currentRound.filter(playerName, game)
    }


    /**
     * Make bets, fold etc
     * @param gameId
     * @param roundId
     */
    @RequestMapping(value="/{roundNumber}",method = RequestMethod.PUT)
    Round updateRound(@PathVariable String gameId, @PathVariable Integer roundNumber, @RequestParam String playerName, @RequestBody BetRequest betRequest){

        Game game = gameService.loadGame(gameId)

        List<Round> rounds = game.rounds;

        if(rounds.size() < roundNumber){
            throw new PokerNotFoundException("No Round found: " + roundNumber)
        }

        Round round = rounds.get(--roundNumber);

        //Validate the request
        betRequest.validate()

        //Update the round
        round = roundService.updateRound(game,round,betRequest,playerName)

        //Issue notification
        notificationService.sendNotification(betRequest,playerName)

        return round.filter(playerName, game)
    }
}
