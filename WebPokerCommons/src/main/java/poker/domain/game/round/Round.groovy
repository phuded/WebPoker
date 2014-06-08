package poker.domain.game.round

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Transient
import poker.domain.card.Card
import poker.domain.card.Deck
import poker.domain.game.Game
import poker.domain.game.bettinground.BettingRound
import poker.domain.game.bettinground.FirstRound
import poker.domain.game.bettinground.FlopRound
import poker.domain.game.bettinground.RiverCardRound
import poker.domain.game.bettinground.TurnCardRound
import poker.domain.hand.Hand
import poker.domain.player.Player

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 24/08/2013
 * Time: 02:20
 * To change this template use File | Settings | File Templates.
 */
class Round {

    int roundNumber

    //Is current round
    boolean isCurrent

    //Has round finished?
    boolean hasFinished

    // Up for 5 cards
    List<Card> roundCards

    //Winners
    @JsonIgnore
    List<Player> winners

    //Current Player
    String currentPlayerName

    //Pot
    int pot

    //Betting rounds
    List<BettingRound> bettingRounds

    //Winning Player Names
    List<String> winningPlayerNames = []

    //Winning hand
    Hand winningHand

    //The requested player
    @Transient
    Player player

    //Default Constructor
    Round(){}

    Round(Game game, int roundNumber){
        this.roundNumber = roundNumber

        isCurrent = false
        hasFinished = false

        //Create/replace deck and shuffle
        game.deck = new Deck()
        game.deck.shuffle()

        //Prepare round cards
        roundCards = []

        //Betting rounds
        bettingRounds = [new FirstRound(),
                         new FlopRound(),
                         new TurnCardRound(),
                         new RiverCardRound()]
    }

    /**
     * Close the round
     */
    void close(){
        //Close the round
        //Set Player Names and Best Hand
        this.winners.each {Player winner ->
            this.winningPlayerNames << winner.name
        }

        this.winningHand = this.winners.get(0).bestHand

        //Clear the winners
        this.winners = null

        //Switch flag
        this.isCurrent = false

        //Remove the current player
        this.currentPlayerName = null

        //Set finished
        this.hasFinished = true

    }

    /**
     * Get the current betting round
     * @return
     */
    @JsonIgnore
    BettingRound getCurrentBettingRound(){
        return bettingRounds.find{BettingRound bettingRound ->
            bettingRound.isCurrent
        }
    }

    /**
     * Filter the response
     * @param playerName
     * @param game
     * @return
     */
    Round filter(String playerName, Game game){
        this.player = game.getPlayerByName(playerName)

        return this
    }


}
