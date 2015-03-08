package com.webpoker.domain.request

import com.webpoker.exception.PokerException

/**
 * Created by matt on 03/06/2014.
 */
class PlayerRequest {

    String name
    String firstName
    String lastName
    String password
    String role

    void validate(){

        if(!name || !firstName || !lastName || !password || !role){
            throw new PokerException("You must specify a Player name, first name, last name, role and password.")
        }

    }

}
