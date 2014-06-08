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
class TurnCardRound extends BettingRound{

    static final Logger logger = LoggerFactory.getLogger(TurnCardRound.class)

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
        game.nonFoldedPlayers.each { Player player ->
            //Add river card to player hand
            player.addGameCards(turnCard)

            logger.info(player.name + " hand after turn card: " + player.allCards)
        }

        areCardsDealt = true
    }
}
