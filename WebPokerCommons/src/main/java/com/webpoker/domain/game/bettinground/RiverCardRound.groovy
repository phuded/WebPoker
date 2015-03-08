package com.webpoker.domain.game.bettinground

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.webpoker.domain.card.Card
import com.webpoker.domain.player.GamePlayer
import com.webpoker.domain.game.Game
import com.webpoker.domain.game.round.Round

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 26/08/13
 * Time: 21:41
 * To change this template use File | Settings | File Templates.
 */
class RiverCardRound extends BettingRound{

    static final Logger logger = LoggerFactory.getLogger(RiverCardRound.class)

    @Override
    int getBettingRoundNumber(){
        return  4
    }

    @Override
    def dealCards(Game game, Round parentRound) {
        // Remove river card from deck and add to round cards
        Card finalCard = game.deck.getCard()
        parentRound.roundCards.add(finalCard)

        //Add to each players hand
        game.nonFoldedPlayers.each { GamePlayer player ->
            //Add river card to player hand
            player.addGameCards(finalCard)

            logger.info(player.name + " - After river card: " + player.allCards)
        }

        areCardsDealt = true
    }

    @Override
    boolean isLast(){
        return true
    }

}
