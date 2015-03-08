package com.webpoker.domain.request

import com.webpoker.domain.player.betting.BettingAction
import com.webpoker.exception.PokerException

/**
 * Request to make a bet
 */
class BetRequest {

    String bettingAction
    Integer bet

    void validate(){
        if(bettingAction == null && bet == null){
            throw new PokerException("You must either bet, check, call or fold.")
        }

        if(bettingAction != null && !BettingAction.isValidAction(bettingAction)){
            throw new PokerException("Invalid bet type: it must either check, call or fold.")
        }

    }
}
