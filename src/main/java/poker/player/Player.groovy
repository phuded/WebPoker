package poker.player

import poker.main.domain.card.Card
import poker.hand.Hand
import poker.main.Main
import poker.util.HandDetector

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
    List<Hand> hands
    Hand bestHand

    //Funds
    int funds

    //Round specific
    boolean hasFolded
    int amountBet


    def Player(String playerName, int startingFunds){
        name = playerName
        funds = startingFunds
        hasFolded = false
        amountBet = 0
    }

    def Player(String playerName){
        name = playerName
        funds = Main.startingPlayerFunds
        hasFolded = false
        amountBet = 0
    }

    //Get dealt card
    def receiveCard(Card card){
        initialCards.push(card)
        //Add to all players possible cards too
        allCards.push(card)
    }

    // Reference card from the round
    def addGameCards(Card card){
        allCards.add(card)
    }

    def addGameCards(List <Card> cards){
        allCards.addAll(cards)
    }

    //Bet
    def makeBet(int newCurrentBet){
        int amountToCall = newCurrentBet - amountBet
        funds -= amountToCall
        amountBet = newCurrentBet
    }

    //Get hand
    def detectHand(){
        hands = HandDetector.detect(allCards)
        bestHand = hands.last()
    }

    //Reset player between Poker Rounds
    def resetBetweenRounds(){
        this.initialCards = []
        this.allCards = []
        this.hands = null
        this.bestHand = null
        this.hasFolded = false
        this.amountBet = 0
    }

    //Reset player between betting rounds
    def resetBetweenBettingRounds(){
        this.amountBet = 0
    }

    @Override
    String toString(){
        this.name
    }

}
