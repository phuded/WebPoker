package poker.domain.player

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.annotation.Id
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

    private static int startingPlayerFunds = 200
    private static final Logger logger = LoggerFactory.getLogger(Player.class);

    @Id
    String id

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

    //TODO Always goes to 0 at end of round
    int amountBet


    Player(String name, Integer funds){
        this.name = name

        if(funds) {
            this.funds = funds
        }
        else{
            this.funds = startingPlayerFunds
        }

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
        int amountToCall = newCurrentBet - this.amountBet
        this.funds -= amountToCall
        this.amountBet = newCurrentBet
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
        this.name + " - " + this.allCards
    }

}
