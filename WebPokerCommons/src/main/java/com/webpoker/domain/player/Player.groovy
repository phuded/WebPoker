package com.webpoker.domain.player

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed

/**
 * Persisted Player Object
 */
class Player {
    @Id
    String id

    @Indexed(unique=true)
    String name

    String firstName

    String lastName

    @JsonIgnore
    String password

    PlayerRole role

    Player(String name, String firstName, String lastName, PlayerRole role, String password){
        this.name = name
        this.firstName = firstName
        this.lastName = lastName
        this.role = role
        this.password = password
    }
}
