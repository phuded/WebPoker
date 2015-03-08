package com.webpoker.domain.hand

import com.webpoker.domain.card.Card

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 14/08/13
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
class Hand {

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
        return handType.name + " - " + cards + "("+secondaryCards+")"
    }

    @Override
    boolean equals(Object hand){
        Hand nextHand = (Hand)hand

        if(this.handType == nextHand.handType && this.cards == nextHand.cards && this.secondaryCards == nextHand.secondaryCards){
            return true
        }

        return false
    }

    @Override
    int hashCode(){
       return (this.handType.hashCode() + this.cards.hashCode())
    }
}
