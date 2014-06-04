package poker.domain.game

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.joda.time.DateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import poker.domain.card.Deck
import poker.domain.game.round.Round
import poker.domain.player.Player
import poker.util.PokerDateSerializer

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 13/08/13
 * Time: 18:10
 * To change this template use File | Settings | File Templates.
 */
@JsonPropertyOrder(["id","name","createdDate","lastModifiedDate","players","rounds"])
class Game {

    @Id
    String id

    String name

    @CreatedDate
    @JsonSerialize(using = PokerDateSerializer.class)
    DateTime createdDate

    @LastModifiedDate
    @JsonSerialize(using = PokerDateSerializer.class)
    DateTime lastModifiedDate

    @JsonIgnore
    Deck deck

    List<Player> players
    List<Round> rounds

    //Default Constructor
    Game(){}

    Game(String name, List<String> playerNames, Integer startingPlayerFunds){
        this.name = name
        this.players = []
        this.rounds = []

        //Create players
        createPlayers(playerNames,startingPlayerFunds)
    }

    def createPlayers(List<String> playerNames, Integer startingPlayerFunds){

       playerNames.eachWithIndex{ String playerName, int i ->
           players << new Player(playerName,++i,startingPlayerFunds)
       }
    }

    /**
     * Get a list of non folded players
     * @return
     */
    @JsonIgnore
    List<Player> getNonFoldedPlayers(){

       def nonFoldedPlayers = players.findAll { Player player ->
           !player.hasFolded
       }

       return nonFoldedPlayers
    }

    /**
     *  Check if there are any players who have not bet once
     */
    boolean anyNonFoldedPlayersYetToBet(){
        List<Player> playersYetToBet = getNonFoldedPlayers().findAll {Player player ->
          !player.hasBetOnce
        }

        return playersYetToBet.size() > 0
    }

}
