package com.webpoker.plugin

import com.webpoker.domain.hand.Hand
import com.webpoker.domain.request.BetRequest

/**
 * Created by matt on 05/08/2014.
 */
public interface AIPlayer {

    BetRequest determineBet (Hand bestHand, Integer bettingRoundNumber, Integer pot, Integer currentBet, Integer playerBet)
}