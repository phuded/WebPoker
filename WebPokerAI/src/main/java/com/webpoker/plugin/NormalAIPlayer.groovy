package com.webpoker.plugin

import com.webpoker.domain.hand.Hand
import com.webpoker.domain.hand.HandType
import com.webpoker.domain.request.BetRequest

/**
 * Created by matt on 05/08/2014.
 */
class NormalAIPlayer implements AIPlayer{


    BetRequest determineBet (Hand bestHand, Integer bettingRoundNumber, Integer pot, Integer currentBet, Integer playerBet) {

        //How much is needed?
        Integer amountNeededToBet = currentBet - playerBet;

        if(amountNeededToBet == 0){
            //Can just check or can raise
        }

        //Otherwise need to call, raise or fold
        if(bestHand.handType < HandType.PAIR){

        }
        else if(bestHand.handType == HandType.PAIR){

        }
        else if(bestHand.handType > HandType.PAIR){

        }

        //Bluff?
        Integer random = new Random().nextInt(3)

        if(random == 4){
            //Call or Bet
        }

        return null;
    }
}
