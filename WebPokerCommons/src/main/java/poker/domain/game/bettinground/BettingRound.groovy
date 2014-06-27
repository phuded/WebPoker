package poker.domain.game.bettinground

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import poker.domain.game.Game
import poker.domain.game.round.Round
import poker.domain.player.GamePlayer

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 26/08/13
 * Time: 21:21
 * To change this template use File | Settings | File Templates.
 */
@JsonPropertyOrder(["bettingRoundNumber","amountBetPerPlayer","isCurrent","hasFinished","areCardsDealt"])
abstract class BettingRound {

    //Current bet in betting round
    int amountBetPerPlayer

    //Boolean for are cards dealt
    boolean areCardsDealt

    //Is current?
    boolean isCurrent

    //Has finished?
    boolean hasFinished

    BettingRound(){
        areCardsDealt = false
        amountBetPerPlayer = 0

        isCurrent = false
        hasFinished = false
    }

    abstract int getBettingRoundNumber()

    abstract dealCards(Game game, Round round)

    /**
     * Get the total pot from the betting round - include folded players (use game players)
     */
    def getPot(Game game){
        int roundPot = 0

        //All players (current and folded)
        game.players.each{GamePlayer player ->
            roundPot += player.amountBet
        }
        return roundPot
    }

    /**
     * Is the last betting round?
     * @return
     */
    @JsonIgnore
    boolean isLast(){
        return false
    }

    /**
     * Close the round
     */
    void close(){
        this.isCurrent = false

        this.hasFinished = true
    }

}
