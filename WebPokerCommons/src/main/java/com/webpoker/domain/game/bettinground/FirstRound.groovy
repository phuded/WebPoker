package com.webpoker.domain.game.bettinground

import com.webpoker.domain.game.Game
import com.webpoker.domain.game.round.Round
import com.webpoker.domain.player.GamePlayer
import groovy.util.logging.Slf4j

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 26/08/13
 * Time: 21:33
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
class FirstRound extends BettingRound{

    @Override
    int getBettingRoundNumber(){
        return  1
    }

    @Override
    def dealCards(Game game, Round round) {

        //Deal 2 cards to each player
        game.nonFoldedPlayers.each { GamePlayer player ->
            //Player gets two cards
            dealInitialCardsToPlayer(game, player)

            log.info(player.name + " - Hand after first deal: " + player.allCards)
        }

        areCardsDealt = true
    }

    // Deal 1st two cards to player
    def dealInitialCardsToPlayer(Game game, GamePlayer player){
        player.receiveCard(game.deck.getCard())
        player.receiveCard(game.deck.getCard())
    }
}
