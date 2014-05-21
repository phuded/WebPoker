package poker.service

import org.springframework.stereotype.Service
import poker.domain.card.Card
import poker.domain.player.Player
import poker.domain.hand.HandType

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 24/08/2013
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */

@Service
class RoundWinnerDetector {

    public List<Player> detectWinners(List<Player> players){
        //Find best hand type
        HandType bestHandType = players.max{it.bestHand.handType}.bestHand.handType

        println "MAIN: Best hand type: " + bestHandType.name

        //Get all players with that hand type
        List<Player> winningPlayers = players.findAll{it.bestHand.handType == bestHandType}

        println "MAIN: Players with winning hand type: " + winningPlayers

        // If one player with hand-> they are the winner
        if(winningPlayers.size() == 1){
            return winningPlayers
        }
        else{
            //Pick winners who have the best of that hand type
            return getWinnersOfBestHandType(winningPlayers)
        }

    }

    //Get winners based on cards in hand
    public List<Player> getWinnersOfBestHandType(List<Player> potentialWinners){

       List<Player> losers = []
       int handSize = potentialWinners.first().bestHand.cards.size()
       int cardIndex = handSize -1

        for(cardIndex; cardIndex>=0; cardIndex--){
            //Get the best card in the position
            Card bestCardInPosition = potentialWinners.max{it.bestHand.cards[cardIndex].cardValue.value}.bestHand.cards[cardIndex]
            int bestCardValue = bestCardInPosition.cardValue.value

            //Loop through players -> disgard any which don't have max card
            potentialWinners.each{ Player player ->
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

    //Filter winners based on kicker cards
    public List<Player> getWinnersOfBestSecondaryCards(List<Player> potentialWinners){

        List<Player> losers = []
        int secondaryCardSize = potentialWinners.first().bestHand.secondaryCards.size()
        int cardIndex = secondaryCardSize -1

        for(cardIndex; cardIndex>=0; cardIndex--){
            //Get the best card in the position
            Card bestCardInPosition = potentialWinners.max{it.bestHand.secondaryCards[cardIndex].cardValue.value}.bestHand.secondaryCards[cardIndex]
            int bestCardValue = bestCardInPosition.cardValue.value

            //Loop through players -> disgard any which don't have max card
            potentialWinners.each{ Player player ->
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
