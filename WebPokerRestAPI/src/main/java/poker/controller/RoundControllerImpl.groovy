package poker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import poker.domain.RoundResponse
import poker.domain.player.GamePlayer
import poker.domain.request.BetRequest
import poker.domain.game.Game
import poker.domain.game.round.Round
import poker.domain.security.PokerUser
import poker.exception.PokerException
import poker.exception.PokerNotFoundException
import poker.service.GameService
import poker.service.NotificationService
import poker.service.RoundService
import poker.service.security.PokerUserDetailsService

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
     * TODO: Admin only
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
    RoundResponse createNewRound(@PathVariable String gameId){

        //Get logged in user
        PokerUser player = PokerUserDetailsService.currentUser

        Game game = gameService.loadGame(gameId, player)

        Round currentRound = game.currentRound

        //If is a current round
        if(currentRound){
            throw new PokerException("A current round already exists.")
        }

        //Create new Round
        Round newRound = roundService.createNextRound(game)

        //Send new round notification
        notificationService.sendNotification(game.id, newRound.roundNumber)

        //Build Response
        return buildResponse(newRound, game.getPlayer(player.username))
    }

    /**
     * Get the round in the game - TODO Make Admin only
     * @param gameId
     */
    @RequestMapping(value="/{roundNumber}",method = RequestMethod.GET)
    RoundResponse getRound(@PathVariable String gameId, @PathVariable Integer roundNumber){

        //Get logged in user
        PokerUser player = PokerUserDetailsService.currentUser

        Game game = gameService.loadGame(gameId, player)

        Round round = game.getRoundByNumber(roundNumber)

        //Build Response
        return buildResponse(round, game.getPlayer(player.username))
    }

    /**
     * Get the current Round
     * @param gameId
     */
    @RequestMapping(value="/current",method = RequestMethod.GET)
    RoundResponse getCurrentRound(@PathVariable String gameId){

        //Get logged in user
        PokerUser player = PokerUserDetailsService.currentUser

        Game game = gameService.loadGame(gameId, player)

        Round currentRound = game.currentRound

        //If there is not a current round
        if(!currentRound){
            throw new PokerNotFoundException("No Current Round.")
        }

        //Build Response
        return buildResponse(currentRound, game.getPlayer(player.username))
    }


    /**
     * Make bets, fold etc
     * @param gameId
     * @param roundId
     */
    @RequestMapping(value="/{roundNumber}",method = RequestMethod.PUT)
    RoundResponse updateRound(@PathVariable String gameId, @PathVariable Integer roundNumber, @RequestBody BetRequest betRequest){

        //Validate the request
        betRequest.validate()

        //Get logged in user
        PokerUser player = PokerUserDetailsService.currentUser

        Game game = gameService.loadGame(gameId, player)

        Round round = game.getRoundByNumber(roundNumber)

        //Update the round
        round = roundService.updateRound(game, round, betRequest, player)

        //Issue notification
        notificationService.sendNotification(game.id, betRequest, player.username)

        //Build Response
        return buildResponse(round, game.getPlayer(player.username))
    }

    /**
     * Return a response
     * @param round
     * @param player
     * @return
     */
    RoundResponse buildResponse(Round round, GamePlayer player){
         return new RoundResponse(round, player)
    }

}
