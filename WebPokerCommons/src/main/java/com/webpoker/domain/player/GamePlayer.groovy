package com.webpoker.domain.player

import com.webpoker.domain.card.Card
import com.webpoker.domain.hand.Hand

/**
 * Player associated with a game
 */
class GamePlayer {
    //Player name
    String name

    // First 2 cards
    List<Card> initialCards = []

    // First 2 plus all parentGame cards (up to 7)
    List<Card> allCards = []

    //Hands
    List<Hand> hands
    Hand bestHand

    //Funds
    Integer funds

    //Order
    int order

    //Has bet once
    boolean hasBetOnce

    //Round specific
    boolean hasFolded

    //Always goes to 0 at end of round
    int amountBet

    GamePlayer(String name, Integer order, Integer funds){
        this.name = name

        this.order = order
        this.funds = funds

        this.hasBetOnce = false
        this.hasFolded = false
        this.amountBet = 0
    }

    //Get dealt card
    void receiveCard(Card card){
        this.initialCards.push(card)
        //Add to all players possible cards too
        this.allCards.push(card)
    }

    // Reference card from the round
    void addGameCards(Card card){
        this.allCards.add(card)
    }

    void addGameCards(List <Card> cards){
        this.allCards.addAll(cards)
    }

    //Bet
    void makeBet(int newCurrentBet){
        //TODO - Prevent -ve balance
        //Determine how much is need to meet current bet
        int amountToCall = newCurrentBet - this.amountBet

        //Subtract from balance
        this.funds -= amountToCall

        //Set the amount bet
        this.amountBet = newCurrentBet
    }

    //Reset player between Poker Rounds
    void resetBetweenRounds(){

        //Call reset between betting rounds
        resetBetweenBettingRounds()

        this.initialCards = []
        this.allCards = []
        this.hands = null
        this.bestHand = null
        this.hasFolded = false
    }

    //Reset player between betting rounds
    void resetBetweenBettingRounds(){
        this.hasBetOnce = false
        this.amountBet = 0
    }

    @Override
    String toString(){
        this.name + " - " + this.allCards
    }

    @Override
    boolean equals(Object object){
       if(object instanceof GamePlayer && ((GamePlayer)object).name.equals(this.name)){
           return true
       }

       return false
    }
}
