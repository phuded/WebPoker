package poker.domain.player

import org.springframework.data.annotation.Id

/**
 * Created by matt on 27/06/2014.
 */
class Player {
    @Id
    String id

    String name

    String firstName

    String lastName

    String password

    Player(String name, String firstName, String lastName, String password){
        this.name = name
        this.firstName = firstName
        this.lastName = lastName
        this.password = password
    }
}
