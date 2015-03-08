package com.webpoker.domain.hand

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 14/08/13
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
public enum HandType {
    HIGH_CARD("High Card",0),
    PAIR ("Pair",1),
    TWO_PAIR ("Two Pair",2),
    THREE_OF_A_KIND("Three of a Kind",3),
    STRAIGHT("Straight",4),
    FLUSH("Flush",5),
    FULLHOUSE("Full House", 6),
    FOUR_OF_A_KIND("Four of a Kind",7),
    STRAIGHT_FLUSH("Straight Flush",8),
    ROYAL_FLUSH("Royal Flush",9)

    String name
    int value

    HandType(name,value){
        this.name = name
        this.value = value
    }


}