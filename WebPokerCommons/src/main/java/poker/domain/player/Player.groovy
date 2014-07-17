package poker.domain.player

import com.fasterxml.jackson.annotation.JsonIgnore
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

    @JsonIgnore
    String password

    Player(String name, String firstName, String lastName, String password){
        this.name = name
        this.firstName = firstName
        this.lastName = lastName
        this.password = password
    }
}
