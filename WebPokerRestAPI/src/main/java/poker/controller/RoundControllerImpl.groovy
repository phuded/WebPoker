package poker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.*
import poker.domain.notification.BetNotification
import poker.domain.player.betting.BettingAction
import poker.domain.request.BetRequest
import poker.domain.game.Game
import poker.domain.game.round.Round
import poker.exception.PokerException
import poker.exception.PokerRoundNotFoundException
import poker.service.GameService
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
    SimpMessagingTemplate template

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
        return roundService.createNextRound(game)
    }

    /**
     * Get the round in the game
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
     * Make bets, fold etc
     * @param gameId
     * @param roundId
     */
    @RequestMapping(value="/{roundNumber}",method = RequestMethod.PUT)
    Round updateRound(@PathVariable String gameId, @PathVariable Integer roundNumber, @RequestBody BetRequest betRequest){

        Game game = gameService.loadGame(gameId)

        List<Round> rounds = game.rounds;

        if(rounds.size() < roundNumber){
            throw new PokerRoundNotFoundException("No Round found.")
        }

        Round round = rounds.get(--roundNumber);

        //Validate the request
        betRequest.validate()

        //Update the round
        round = roundService.updateRound(game,round,betRequest)

        sendNotification(betRequest)

        return round
    }


    void sendNotification(BetRequest betRequest){//, BettingAction bettingAction, int bet) throws Exception {

        BetNotification betNotification = new BetNotification()
        betNotification.playerName = betRequest.player
        betNotification.bettingAction = BettingAction.getBettingActionByName(betRequest.bettingAction)
        betNotification.bet = betRequest.bet

        this.template.convertAndSend("/topic/betNotifications", betNotification);
    }


}
