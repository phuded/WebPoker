package poker.domain.request

import poker.exception.PokerException

/**
 * Created by matt on 03/06/2014.
 */
class GameRequest {

    String name
    List<String> playerNames
    Integer startingPlayerFunds = 1000

    void validate(){

        if(name == null){
            throw new PokerException("You must specify a name.")
        }

        if(playerNames == null || playerNames.size() < 2) {
            throw new PokerException("You must specify at least 2 players.")
        }
    }

}
