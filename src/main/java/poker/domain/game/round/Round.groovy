package poker.domain.game.round

import org.springframework.data.annotation.Id
import poker.domain.card.Card
import poker.domain.card.Deck
import poker.domain.game.Game
import poker.domain.game.bettinground.BettingRound
import poker.domain.game.bettinground.FirstRound
import poker.domain.game.bettinground.FlopRound
import poker.domain.game.bettinground.RiverCardRound
import poker.domain.game.bettinground.TurnCardRound
import poker.domain.player.Player

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 24/08/2013
 * Time: 02:20
 * To change this template use File | Settings | File Templates.
 */
class Round {

    @Id
    String id;

    // Up for 5 cards
    List<Card> roundCards

    //Players in round
    List<Player> roundPlayers

    //Winners
    List<Player> winners

    //Pot
    int pot

    //Betting rounds
    List<BettingRound> bettingRounds

    def Round(Game game){
        //TODO: SORT THIS STUFF OUT

        //Create/replace deck and shuffle
        game.deck = new Deck()
        game.deck.shuffle()

        //Add players to round
        roundPlayers = []
        roundPlayers.addAll(game.players)

        //Prepare round cards
        roundCards = []

        //Betting rounds
        bettingRounds = [new FirstRound(),
                         new FlopRound(),
                         new TurnCardRound(),
                         new RiverCardRound()]
    }

}
