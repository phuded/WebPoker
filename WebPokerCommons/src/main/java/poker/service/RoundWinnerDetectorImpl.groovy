package poker.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import poker.domain.card.Card
import poker.domain.player.GamePlayer
import poker.domain.hand.HandType

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 24/08/2013
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */

@Service
class RoundWinnerDetectorImpl implements RoundWinnerDetector{

    static final Logger logger = LoggerFactory.getLogger(RoundWinnerDetectorImpl.class)

    @Override
    public List<GamePlayer> detectWinners(List<GamePlayer> players){
        //Find best hand type
        HandType bestHandType = players.max{it.bestHand.handType}.bestHand.handType

        logger.info("MAIN: Best hand type: " + bestHandType.name)

        //Get all players with that hand type
        List<GamePlayer> winningPlayers = players.findAll{it.bestHand.handType == bestHandType}

        logger.info("MAIN: Players with winning hand type: " + winningPlayers)

        // If one player with hand-> they are the winner
        if(winningPlayers.size() == 1){
            return winningPlayers
        }
        else{
            //Pick winners who have the best of that hand type
            return getWinnersOfBestHandType(winningPlayers)
        }

    }

    /**
     * Get winners based on cards in hand
     * @param potentialWinners
     * @return
     */
    private List<GamePlayer> getWinnersOfBestHandType(List<GamePlayer> potentialWinners){

       List<GamePlayer> losers = []
       int handSize = potentialWinners.first().bestHand.cards.size()
       int cardIndex = handSize -1

        for(cardIndex; cardIndex>=0; cardIndex--){
            //Get the best card in the position
            Card bestCardInPosition = potentialWinners.max{it.bestHand.cards[cardIndex].cardValue.value}.bestHand.cards[cardIndex]
            int bestCardValue = bestCardInPosition.cardValue.value

            //Loop through players -> discard any which don't have max card
            potentialWinners.each{ GamePlayer player ->
                int lastCardValue = player.bestHand.cards[cardIndex].cardValue.value
                if(lastCardValue != bestCardValue){
                    losers << player
                }
            }

            //Remove losers from potential winners and reset losers
            potentialWinners.removeAll(losers)
            losers = []

        }

        //If still more than 1 winner and the hand size is less than 5
        if(potentialWinners.size() > 1 && handSize <5){
            return getWinnersOfBestSecondaryCards(potentialWinners)
        }
        else{
            return potentialWinners
        }
    }

    /**
     * Filter winners based on kicker cards
     */
    private List<GamePlayer> getWinnersOfBestSecondaryCards(List<GamePlayer> potentialWinners){

        List<GamePlayer> losers = []
        int secondaryCardSize = potentialWinners.first().bestHand.secondaryCards.size()
        int cardIndex = secondaryCardSize -1

        for(cardIndex; cardIndex>=0; cardIndex--){
            //Get the best card in the position
            Card bestCardInPosition = potentialWinners.max{GamePlayer player -> player.bestHand.secondaryCards[cardIndex].cardValue.value}.bestHand.secondaryCards[cardIndex]
            int bestCardValue = bestCardInPosition.cardValue.value

            //Loop through players -> disgard any which don't have max card
            potentialWinners.each{ GamePlayer player ->
                int lastCardValue = player.bestHand.secondaryCards[cardIndex].cardValue.value
                if(lastCardValue != bestCardValue){
                    losers << player
                }
            }

            //Remove losers from potential winners and reset losers
            potentialWinners.removeAll(losers)
            losers = []

        }

        return potentialWinners
    }

}
