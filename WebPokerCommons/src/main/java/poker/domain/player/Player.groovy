package poker.domain.player

import org.springframework.data.annotation.Id

/**
 * Created by matt on 27/06/2014.
 */
class Player {
    @Id
    Integer id

    String name

    String firstName

    String lastName

    Player(String name, String firstName, String lastName){
        this.name = name
        this.firstName = firstName
        this.lastName = lastName
    }
}
