package poker.domain.game.bettinground

import poker.domain.card.Card
import poker.domain.game.Game
import poker.domain.game.round.Round
import poker.domain.player.Player

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 26/08/13
 * Time: 21:23
 * To change this template use File | Settings | File Templates.
 */
class FlopRound extends BettingRound{

    FlopRound(Round round){
        super(round)
    }

    @Override
    def dealCards(Game game, Round parentRound) {
        //Remove flop from deck and add the round cards
        List<Card> flop = game.deck.getFlop()
        parentRound.roundCards.addAll(flop)

        //Add flop to each player's hands
        parentRound.roundPlayers.each { Player player ->
            //Add flop to player hand
            player.addGameCards(flop)

            println "MAIN: " + player.name + " - Hand after flop: " + player.allCards
        }
    }
}
