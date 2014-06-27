package poker.domain.request

import poker.exception.PokerException

/**
 * Created by matt on 03/06/2014.
 */
class PlayerRequest {

    String name
    String firstName
    String lastName

    void validate(){

        if(!name || !firstName || !lastName){
            throw new PokerException("You must specify a Player name, first name and last name.")
        }

    }

}
