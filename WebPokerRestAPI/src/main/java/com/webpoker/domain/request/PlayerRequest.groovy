package com.webpoker.domain.request

import com.webpoker.domain.player.PlayerRole
import com.webpoker.exception.PokerException

/**
 * Request to make a new player
 */
class PlayerRequest {

    String name
    String firstName
    String lastName
    String password
    PlayerRole role

    void validate(){

        if(!name || !firstName || !lastName || !password || !role){
            throw new PokerException("You must specify a Player name, first name, last name, role and password.")
        }

    }

}
