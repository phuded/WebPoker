package poker.domain.game.round

import com.fasterxml.jackson.annotation.JsonIgnore
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
    String currentPlayer

    //Pot
    int pot

    //Betting rounds
    List<BettingRound> bettingRounds

    //Winning Player Names
    List<String> winningPlayerNames = []

    //Winning hand
    Hand winningHand


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

}
