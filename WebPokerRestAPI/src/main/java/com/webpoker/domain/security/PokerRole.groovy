package com.webpoker.domain.security

/**
 * Created by matt on 19/07/2014.
 */
enum PokerRole {
    ROLE_PLAYER ("Player"),
    ROLE_ADMINISTRATOR ("Administrator")

    String name

    PokerRole(String name){
        this.name = name
    }

    static PokerRole getRole(String name){
        return PokerRole.find {
            it.name == name
        }
    }
}