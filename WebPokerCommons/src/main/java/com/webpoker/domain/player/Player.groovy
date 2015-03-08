package com.webpoker.domain.player

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed

/**
 * Created by matt on 27/06/2014.
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

    String role

    Player(String name, String firstName, String lastName, String role, String password){
        this.name = name
        this.firstName = firstName
        this.lastName = lastName
        this.role = role
        this.password = password
    }
}
