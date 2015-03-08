package com.webpoker.domain.game.bettinground

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.webpoker.domain.card.Card
import com.webpoker.domain.game.Game
import com.webpoker.domain.player.GamePlayer
import com.webpoker.domain.game.round.Round

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 26/08/13
 * Time: 21:23
 * To change this template use File | Settings | File Templates.
 */
class FlopRound extends BettingRound{

    static final Logger logger = LoggerFactory.getLogger(FlopRound.class)

    @Override
    int getBettingRoundNumber(){
        return  2
    }

    @Override
    def dealCards(Game game, Round parentRound) {
        //Remove flop from deck and add the round cards
        List<Card> flop = game.deck.getFlop()
        parentRound.roundCards.addAll(flop)

        //Add flop to each player's hands
        game.nonFoldedPlayers.each { GamePlayer player ->
            //Add flop to player hand
            player.addGameCards(flop)

            logger.info(player.name + " - Hand after flop: " + player.allCards)
        }

        areCardsDealt = true
    }
}
