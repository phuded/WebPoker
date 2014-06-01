package poker.util

import poker.domain.card.Card
import poker.domain.card.CardValue
import poker.domain.hand.Hand

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 16/08/13
 * Time: 18:31
 * To change this template use File | Settings | File Templates.
 */
class PokerUtil {

    /**
     * Put cards in order of value
     * @param cards
     * @return
     */
    static sortCards(List<Card> cards){
        cards.sort {
            it.cardValue.value
        }
    }

    /**
     * Switch between the Ace being High and Low
     * @param cards
     * @param low
     * @return
     */
    static convertAce(List<Card> cards, boolean low){
        cards.each {
            if(it.cardValue == CardValue.ACE){
                if(low){
                    it.cardValue.value = 1
                }
                else{
                    it.cardValue.value = 14
                }
            }
        }

        //Sort after changing Ace value
        sortCards(cards)
    }

    /**
     * Sort hands by value
     * @param hands
     * @return
     */
    static sortHandResults(List<Hand> hands){
        hands.sort{
            it.handType.value
        }

    }
}
