package poker.controller

import poker.domain.game.round.Round
import poker.domain.request.BetRequest

/**
 * Created by matt on 21/05/2014.
 */

interface RoundController {

    /**
     * Get all of the rounds for the game
     * @param gameId
     */
    List<Round> getRounds(String gameId)

    /**
     * Get the round in the game
     * @param gameId
     */
    Round getRound(String gameId, Integer roundNumber)


    /**
     * Create a game round
     * @param gameId
     */
    def createNewRound(String gameId)

    /**
     * Get the current Round
     * @param gameId
     */
    def getCurrentRound(String gameId)


    /**
     * Make bets, fold etc
     * @param gameId
     * @param roundId
     */
    def updateRound(String gameId, BetRequest betRequest)


}
