package poker.controller

import poker.domain.request.BetRequest

/**
 * Created by matt on 21/05/2014.
 */

interface RoundController {

    /**
     * Get all of the rounds for the game
     * @param gameId
     */
    //List<Round> getRounds(String gameId)

    /**
     * Create a game round
     * @param gameId
     */
    def createNewRound(String gameId)

    /**
     * Get the round in the game
     * @param gameId
     */
    def getRound(String gameId, Integer roundNumber)

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
    def updateRound(String gameId, Integer roundNumber, BetRequest betRequest)


}
