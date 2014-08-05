package poker.plugin

import poker.domain.hand.Hand
import poker.domain.request.BetRequest

/**
 * Created by matt on 05/08/2014.
 */
public interface AIPlayer {

    BetRequest determineBet (Hand bestHand, Integer bettingRoundNumber, Integer pot, Integer currentBet, Integer playerBet)
}