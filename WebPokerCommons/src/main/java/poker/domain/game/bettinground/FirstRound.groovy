package poker.domain.game.bettinground

import poker.domain.game.Game
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

    @Override
    def dealCards(Game game, Round round) {

        //Deal 2 cards to each player
        game.getNonFoldedPlayers().each { Player player ->
            //Player gets two cards
            dealInitialCardsToPlayer(game, player)

            println "MAIN: " + player.name + " - Hand after first deal: " + player.allCards
        }
    }

    // Deal 1st two cards to player
    def dealInitialCardsToPlayer(Game game, Player player){
        player.receiveCard(game.deck.getCard())
        player.receiveCard(game.deck.getCard())
    }
}
