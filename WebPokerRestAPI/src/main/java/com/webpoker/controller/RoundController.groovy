package com.webpoker.controller

import com.webpoker.domain.RoundResponse
import com.webpoker.domain.game.Game
import com.webpoker.domain.game.round.Round
import com.webpoker.domain.player.GamePlayer
import com.webpoker.domain.request.BetRequest
import com.webpoker.exception.PokerException
import com.webpoker.exception.PokerNotFoundException
import com.webpoker.service.GameService
import com.webpoker.service.NotificationService
import com.webpoker.service.RoundService
import com.webpoker.service.security.PokerUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
/**
 * Created by matt on 21/05/2014.
 */
@RestController
@RequestMapping("/games/{gameId}/rounds")
class RoundController{

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
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @RequestMapping(method = RequestMethod.GET)
    List<Round> getRounds(@PathVariable String gameId){

        //Skipping game membership check
        Game game = gameService.loadGame(gameId)

        return game.rounds
    }


    /**
     * Get the round in the game
     * @param gameId
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @RequestMapping(value="/{roundNumber}",method = RequestMethod.GET)
    Round getRound(@PathVariable String gameId, @PathVariable Integer roundNumber){

        Game game = gameService.loadGame(gameId, PokerUserDetailsService.currentUserName)

        Round round = game.getRoundByNumber(roundNumber)

        //Build Response
        return round
    }

    /**
     * Create a game round
     * @param gameId
     */
    @RequestMapping(method = RequestMethod.POST)
    RoundResponse createNewRound(@PathVariable String gameId){

        String currentUserName = PokerUserDetailsService.currentUserName

        Game game = gameService.loadGame(gameId, currentUserName)

        //Check if there is a current round
        Round currentRound = game.currentRound

        //If there is already a current round - cannot create a new one
        if(currentRound){
            throw new PokerException("A current round already exists.")
        }

        //Create new Round
        Round newRound = roundService.createNextRound(game)

        //Send new round notification
        notificationService.sendNotification(game.id, newRound.roundNumber)

        //Build Response
        return buildResponse(newRound, game, currentUserName)
    }


    /**
     * Get the current Round
     * @param gameId
     */
    @RequestMapping(value="/current",method = RequestMethod.GET)
    RoundResponse getCurrentRound(@PathVariable String gameId){

        String currentUserName = PokerUserDetailsService.currentUserName

        Game game = gameService.loadGame(gameId, currentUserName)

        Round currentRound = game.currentRound

        //If there is not a current round
        if(!currentRound){
            //Just get the last one as it might have just finished
            currentRound = game.lastRound
        }

        //Build Response
        return buildResponse(currentRound, game, currentUserName)
    }


    /**
     * Make bets, fold etc
     * @param gameId
     * @param roundId
     */
    @RequestMapping(value="/current",method = RequestMethod.PUT)
    RoundResponse updateRound(@PathVariable String gameId, @RequestBody BetRequest betRequest){

        //Validate the request
        betRequest.validate()

        String currentUserName = PokerUserDetailsService.currentUserName

        Game game = gameService.loadGame(gameId, currentUserName)

        Round round = game.currentRound

        //If there is not a current round
        if(!round){
            throw new PokerNotFoundException("No Current Round.")
        }

        //Update the round
        round = roundService.updateRound(game, round, betRequest, currentUserName)

        //Issue notification
        notificationService.sendNotification(game.id, betRequest, currentUserName)

        //Build Response
        return buildResponse(round, game, currentUserName)
    }

    /**
     * Return a response
     * @param round
     * @param player
     * @return
     */
    private RoundResponse buildResponse(Round round, Game game, String userName){

        GamePlayer player = game.getPlayer(userName)

        return new RoundResponse(round, player)
    }

}
