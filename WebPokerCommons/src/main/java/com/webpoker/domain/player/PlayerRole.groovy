package com.webpoker.domain.player

/**
 * Created by matt on 19/07/2014.
 */
enum PlayerRole {

    ROLE_PLAYER ("Player"),
    ROLE_ADMINISTRATOR ("Administrator")

    String name

    PlayerRole(String name){
        this.name = name
    }
}