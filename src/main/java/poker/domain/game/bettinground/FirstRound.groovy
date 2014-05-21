package poker.domain.game.bettinground

import poker.domain.game.round.Round
import poker.domain.player.Player

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 26/08/13
 * Time: 21:33
 * To change this template use File | Settings | File Templates.
 */
class FirstRound extends BettingRound{


    FirstRound(Round round) {
        super(round)
    }

    @Override
    def dealCards() {

        //Deal 2 cards to each player
        parentRound.roundPlayers.each { Player player ->
            //Player gets two cards
            dealInitialCardsToPlayer(player)

            println "MAIN: " + player.name + " - Hand after first deal: " + player.allCards
        }
    }

    // Deal 1st two cards to player
    def dealInitialCardsToPlayer(Player player){
        player.receiveCard(parentRound.parentGame.deck.getCard())
        player.receiveCard(parentRound.parentGame.deck.getCard())
    }
}
