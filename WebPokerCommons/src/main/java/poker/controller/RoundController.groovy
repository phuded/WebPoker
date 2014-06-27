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
    List<Round> getRounds(String gameId, Integer playerId)

    /**
     * Create a game round
     * @param gameId
     */
    Round createNewRound(String gameId, Integer playerId)

    /**
     * Get the round in the game
     * @param gameId
     */
    Round getRound(String gameId, Integer roundNumber, Integer playerId)

    /**
     * Get the current Round
     * @param gameId
     */
    Round getCurrentRound(String gameId, Integer playerId)


    /**
     * Make bets, fold etc
     * @param gameId
     * @param roundId
     */
    Round updateRound(String gameId, Integer roundNumber, Integer playerId, BetRequest betRequest)


}
