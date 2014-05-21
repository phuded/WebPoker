package poker.game.bettinground

import poker.card.Card
import poker.game.round.Round
import poker.player.Player

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 26/08/13
 * Time: 21:41
 * To change this template use File | Settings | File Templates.
 */
class TurnCardRound extends BettingRound{

    TurnCardRound(Round round){
        super(round)
    }

    @Override
    def dealCards() {
        // Remove turn card from deck and add to round cards
        Card turnCard = parentRound.parentGame.deck.getCard()
        parentRound.roundCards.add(turnCard)

        //Add to each players hand
        parentRound.roundPlayers.each { Player player ->
            //Add river card to player hand
            player.addGameCards(turnCard)
            println "MAIN: Player: " + player.name + " hand after turn card: " + player.allCards
        }
    }
}
