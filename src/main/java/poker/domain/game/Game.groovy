package poker.domain.game

import org.springframework.data.annotation.Id
import poker.domain.card.Deck
import poker.domain.game.round.Round
import poker.domain.player.Player

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 13/08/13
 * Time: 18:10
 * To change this template use File | Settings | File Templates.
 */
class Game {

    @Id
    String id;

    Deck deck
    List<Player> players
    List<Round> rounds

    //TEMP -> TO DO REMOVE
    static int tempRoundLimit = 2;

    Game(List<String> playerNames, int startingPlayerFunds){
        this.players = []
        this.rounds = []

        //Create players
        createPlayers(playerNames,startingPlayerFunds)
    }

    def createPlayers(List<String> playerNames,int startingPlayerFunds){
       for (String name: playerNames){
           players << new Player(name,startingPlayerFunds)
       }
    }
}
