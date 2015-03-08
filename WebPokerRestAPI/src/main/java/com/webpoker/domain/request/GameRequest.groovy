package com.webpoker.domain.request

import com.webpoker.exception.PokerException

/**
 * Request to start a new Game
 */
class GameRequest {

    String name
    Integer startingPlayerFunds = 1000

    void validate(){

        if(!name){
            throw new PokerException("You must specify a Game name.")
        }

    }

}
