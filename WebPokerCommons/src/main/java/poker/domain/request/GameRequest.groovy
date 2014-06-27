package poker.domain.request

import poker.exception.PokerException

/**
 * Created by matt on 03/06/2014.
 */
class GameRequest {

    String name
    Integer startingPlayerFunds = 1000

    void validate(){

        if(!name){
            throw new PokerException("You must specify a Game name.")
        }

    }

}
