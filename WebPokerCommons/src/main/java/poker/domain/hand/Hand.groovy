package poker.domain.hand

import poker.domain.card.Card

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 14/08/13
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
class Hand {

    //@Id
    String id;

    HandType handType
    List<Card> cards
    //Cards not included in hand
    List<Card> secondaryCards

    def Hand(HandType handType, List<Card> cards){
        this.handType = handType
        this.cards = cards
        this.secondaryCards = []
    }

    @Override
    String toString(){
        def result = handType.name + " - " + cards + "("+secondaryCards+")"
    }

    @Override
    boolean equals(Hand nextHandResult){
        if(this.handType == nextHandResult.handType && this.cards == nextHandResult.cards && this.secondaryCards == nextHandResult.secondaryCards){
            return true
        }
        return false
    }

    @Override
    int hashCode(){
       return (this.handType.hashCode() + this.cards.hashCode())
    }
}
