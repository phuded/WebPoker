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
import poker.domain.player.GamePlayer
import poker.domain.player.Player
import poker.exception.PokerNotFoundException
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

    @JsonIgnore
    List<GamePlayer> players

    //List of rounds
    List<Round> rounds

    @JsonIgnore
    Integer playerStartingFunds

    //Default Constructor
    Game(){}

    Game(String name, Integer playerStartingFunds){
        this.name = name
        this.players = []
        this.rounds = []

        this.playerStartingFunds = playerStartingFunds
    }

    /**
     * Add player to game
     * @param player
     * @return
     */
    void addPlayer(Player player){
        players << new GamePlayer(player.name, players.size() + 1, this.playerStartingFunds)
    }


    /**
     * Get a list of non folded players
     * @return
     */
    @JsonIgnore
    List<GamePlayer> getNonFoldedPlayers(){

       def nonFoldedPlayers = players.findAll { GamePlayer player ->
           !player.hasFolded
       }

       return nonFoldedPlayers
    }

    /**
     *  Check if there are any players who have not bet once
     */
    boolean anyNonFoldedPlayersYetToBet(){
        List<GamePlayer> playersYetToBet = nonFoldedPlayers.findAll {GamePlayer player ->
          !player.hasBetOnce
        }

        return playersYetToBet.size() > 0
    }

    /**
     * Get the current round
     * @return
     */
    @JsonIgnore
    Round getCurrentRound(){
        return rounds.find{ Round round ->
            round.isCurrent
        }
    }

    /**
     * Get round by number
     * @param roundNumber
     */
    @JsonIgnore
    Round getRoundByNumber(Integer roundNumber){

        if(rounds.size() < roundNumber){
            throw new PokerNotFoundException("No Round found: " + roundNumber)
        }

        return rounds.get(--roundNumber);
    }


    /**
     * Get the current player
     * @return
     */
    @JsonIgnore
    GamePlayer getCurrentPlayer(){
        return players.find {GamePlayer player ->
            player.isCurrent
        }
    }

    /**
     * Get the current player by player name
     * @return
     */
    @JsonIgnore
    GamePlayer getPlayerByName(String name){
        return players.find {GamePlayer player ->
            player.name = name
        }
    }

}
