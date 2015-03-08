package com.webpoker.domain.player.betting

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 26/08/13
 * Time: 18:51
 * To change this template use File | Settings | File Templates.
 */
enum Chip {
    TEN("Ten",10),
    TWENTY("Twenty",20),
    FIFTY("Fifty",20),
    ONE_HUNDRED("One Hundred",100),
    TWO_HUNDRED("Two Hundred",200),
    FIVE_HUNDRED("Five Hundred",500)

    String name
    int value

    Chip(String name, int value){
        this.name = name
        this.value = value
    }
}
