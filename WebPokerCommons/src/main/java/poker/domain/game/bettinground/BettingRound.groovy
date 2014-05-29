package poker.domain.game.bettinground

import com.fasterxml.jackson.annotation.JsonIgnore
import poker.domain.game.Game
import poker.domain.game.round.Round
import poker.domain.player.Player

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 26/08/13
 * Time: 21:21
 * To change this template use File | Settings | File Templates.
 */
abstract class BettingRound {

    //Current bet in betting round
    int amountBetPerPlayer

    @JsonIgnore
    boolean firstCycle

    BettingRound(){
        amountBetPerPlayer = 0
        firstCycle = true
    }

    abstract dealCards(Game game, Round parentRound)

    // Get the total pot from the betting round - include folded players (use game players)
    def getPot(Game game){
        int roundPot = 0
        //All players (current and folded)
        game.players.each{Player player ->
            roundPot += player.amountBet
        }
        return roundPot
    }
}
