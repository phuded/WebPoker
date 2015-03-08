package com.webpoker.domain.card

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 13/08/13
 * Time: 18:00
 * To change this template use File | Settings | File Templates.
 */
class Deck {
    def cards = []

    def Deck(){
        // Make the deck
        makeDeck()
    }

    Card getCard(){
        return cards.pop()
    }

    def getFlop(){
        def flop = []
        flop << cards.pop()
        flop << cards.pop()
        flop << cards.pop()

        return flop
    }

    // Build deck
    def makeDeck(){
        for(Suit suit: Suit){
            for(CardValue cardValue: CardValue){
                cards << new Card(cardValue,suit)
            }
        }
    }

    def shuffle(){
        Collections.shuffle(cards)
    }

    int getRemainingNumberOfCards(){
       cards.size()
    }
}
