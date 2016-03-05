package com.webpoker.domain.game

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import com.webpoker.domain.player.GamePlayer
import com.webpoker.domain.player.Player
import com.webpoker.util.PokerDateSerializer
import com.webpoker.domain.card.Deck
import com.webpoker.domain.game.round.Round
import com.webpoker.exception.PokerNotFoundException

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 13/08/13
 * Time: 18:10
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@JsonPropertyOrder(["id","name","createdDate","lastModifiedDate","playerNames","rounds"])
class Game {

    @Id
    String id

    @Indexed(unique=true)
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
    @JsonIgnore
    List<Round> rounds

    @JsonIgnore
    Integer playerStartingFunds


    List<String> getPlayerNames(){
       return players*.name
    }

    //Default Constructor
    Game(){}

    Game(String name, Integer playerStartingFunds){
        this.name = name
        this.players = []
        this.rounds = []

        this.playerStartingFunds = playerStartingFunds
    }

    /**
     * Check game is ready to start
     * @return
     */
    boolean readyToStart(){
        return this.players.size() > 1
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
    @JsonIgnore
    boolean isAnyNonFoldedPlayerYetToBet(){
        List<GamePlayer> playersYetToBet = nonFoldedPlayers.findAll {GamePlayer player ->
            !player.hasBetOnce
        }

        return !playersYetToBet.empty
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
     * Get the last round
     * @return
     */
    @JsonIgnore
    Round getLastRound(){

        if(rounds.empty){
            throw new PokerNotFoundException("The game has no rounds.")
        }

        return rounds.last()
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
     * Get the current player by player name
     * @return
     */
    @JsonIgnore
    GamePlayer getPlayer(String name){
        return players.find {GamePlayer player ->
            player.name == name
        }
    }

    /**
     * Shift the player order
     */
    void shiftPlayers(){
        this.players = this.players[1..-1,0]

        //Re-allocate order
        this.players.eachWithIndex{ GamePlayer player, int i ->
            player.order = i
        }

        log.info("Players after shifting: " + this.players)
    }

    /**
     * Check if the game only has 1 non folder player left
     * @return
     */
    @JsonIgnore
    boolean isOnePlayerRemaining(){
        return this.nonFoldedPlayers.size() == 1
    }



    /**
     * Get the next player
     * @param game
     * @return
     */
    GamePlayer getNextPlayer(GamePlayer currentPlayer){

        //Check for a non-folded player who has a higher order than the current
        currentPlayer = this.nonFoldedPlayers.find { GamePlayer pl ->
            pl.order > currentPlayer.order
        }

        //If that does not exist -> get the first
        if(currentPlayer == null){

            log.debug("There is no non-folded player with a higher order number")

            //Must reset
            currentPlayer = this.nonFoldedPlayers.first()
        }

        log.info("Next Player to: " + currentPlayer.name)

        return currentPlayer
    }

}
