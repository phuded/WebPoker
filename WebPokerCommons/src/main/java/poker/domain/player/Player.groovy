package poker.domain.player

import poker.domain.card.Card
import poker.domain.hand.Hand

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 13/08/13
 * Time: 18:11
 * To change this template use File | Settings | File Templates.
 */

class Player {
    String name

    // First 2 cards
    List<Card> initialCards = []

    // First 2 plus all parentGame cards (up to 7)
    List<Card> allCards = []

    //Hands
    List<Hand> hands
    Hand bestHand

    //Funds
    int funds

    //Round specific
    boolean hasFolded

    //Is this players turn
    boolean isCurrent

    //Always goes to 0 at end of round
    int amountBet

    Player(String name, Integer funds){
        this.name = name
        this.funds = funds

        this.isCurrent = false
        this.hasFolded = false
        this.amountBet = 0
    }

    //Get dealt card
    def receiveCard(Card card){
        this.initialCards.push(card)
        //Add to all players possible cards too
        this.allCards.push(card)
    }

    // Reference card from the round
    def addGameCards(Card card){
        this.allCards.add(card)
    }

    def addGameCards(List <Card> cards){
        this.allCards.addAll(cards)
    }

    //Bet
    def makeBet(int newCurrentBet){
        //TODO - Prevent -ve balance
        //Determine how much is need to meet current bet
        int amountToCall = newCurrentBet - this.amountBet

        //Subtract from balance
        this.funds -= amountToCall

        //Set the amount bet
        this.amountBet = newCurrentBet
    }

    //Reset player between Poker Rounds
    def resetBetweenRounds(){
        this.isCurrent = false
        this.initialCards = []
        this.allCards = []
        this.hands = null
        this.bestHand = null
        this.hasFolded = false
        this.amountBet = 0
    }

    //Reset player between betting rounds
    def resetBetweenBettingRounds(){
        this.isCurrent = false
        this.amountBet = 0
    }

    @Override
    String toString(){
        this.name + " - " + this.allCards
    }

    @Override
    boolean equals(Object object){
       if(object instanceof Player && ((Player)object).name.equals(this.name)){
           return true
       }

       return false
    }

}
