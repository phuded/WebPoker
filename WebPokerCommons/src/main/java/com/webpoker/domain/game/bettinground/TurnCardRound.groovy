package com.webpoker.domain.game.bettinground

import com.webpoker.domain.card.Card
import com.webpoker.domain.game.Game
import com.webpoker.domain.game.round.Round
import com.webpoker.domain.player.GamePlayer
import groovy.util.logging.Slf4j

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 26/08/13
 * Time: 21:41
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
class TurnCardRound extends BettingRound{

    @Override
    int getBettingRoundNumber(){
        return  3
    }

    @Override
    def dealCards(Game game, Round parentRound) {
        // Remove turn card from deck and add to round cards
        Card turnCard = game.deck.getCard()
        parentRound.roundCards.add(turnCard)

        //Add to each players hand
        game.nonFoldedPlayers.each { GamePlayer player ->
            //Add river card to player hand
            player.addGameCards(turnCard)

            log.info(player.name + " hand after turn card: " + player.allCards)
        }

        areCardsDealt = true
    }
}
