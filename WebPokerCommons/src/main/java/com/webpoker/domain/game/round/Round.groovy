package com.webpoker.domain.game.round

import com.fasterxml.jackson.annotation.JsonIgnore
import com.webpoker.domain.card.Card
import com.webpoker.domain.game.Game
import com.webpoker.domain.game.bettinground.BettingRound
import com.webpoker.domain.game.bettinground.FirstRound
import com.webpoker.domain.game.bettinground.FlopRound
import com.webpoker.domain.game.bettinground.RiverCardRound
import com.webpoker.domain.hand.Hand
import com.webpoker.domain.player.GamePlayer
import com.webpoker.domain.card.Deck
import com.webpoker.domain.game.bettinground.TurnCardRound

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 24/08/2013
 * Time: 02:20
 * To change this template use File | Settings | File Templates.
 */
class Round {

    int roundNumber

    //Is current round
    boolean isCurrent

    //Has round finished?
    boolean hasFinished

    // Up for 5 cards
    List<Card> roundCards

    //Winners
    @JsonIgnore
    List<GamePlayer> winners

    //Current Player
    String currentPlayerName

    //Pot
    int pot

    //Betting rounds
    List<BettingRound> bettingRounds

    //Winning Player Names
    List<String> winningPlayerNames = []

    //Winning hand
    Hand winningHand

    //Default Constructor
    Round(){}

    Round(Game game, int roundNumber){
        this.roundNumber = roundNumber

        isCurrent = false
        hasFinished = false

        //Create/replace deck and shuffle
        game.deck = new Deck()
        game.deck.shuffle()

        //Prepare round cards
        roundCards = []

        //Betting rounds
        bettingRounds = [new FirstRound(),
                         new FlopRound(),
                         new TurnCardRound(),
                         new RiverCardRound()]
    }


    /**
     * Close the round
     */
    void close(){
        //Close the round
        //Set Player Names and Best Hand
        this.winners.each {GamePlayer winner ->
            this.winningPlayerNames << winner.name
        }

        this.winningHand = this.winners.get(0).bestHand

        //Clear the winners
        this.winners = null

        //Switch flag
        this.isCurrent = false

        //Remove the current player
        this.currentPlayerName = null

        //Set finished
        this.hasFinished = true

    }

    /**
     * Pay the winners of the round
     */
    void payWinners(){
        this.winners.each { GamePlayer winner ->
            winner.funds += (this.pot/this.winners.size())
        }
    }

    /**
     * Get the current betting round
     * @return
     */
    @JsonIgnore
    BettingRound getCurrentBettingRound(){
        return bettingRounds.find{BettingRound bettingRound ->
            bettingRound.isCurrent
        }
    }

}
