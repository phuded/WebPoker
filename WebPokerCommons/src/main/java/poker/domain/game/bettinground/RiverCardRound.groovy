package poker.domain.game.bettinground

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import poker.domain.card.Card
import poker.domain.game.Game
import poker.domain.game.round.Round
import poker.domain.player.Player

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
        game.getNonFoldedPlayers().each { Player player ->
            //Add river card to player hand
            player.addGameCards(finalCard)

            logger.info("MAIN: " + player.name + " - After river card: " + player.allCards)
        }

        areCardsDealt = true
    }
}
