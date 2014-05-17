package poker.util

import poker.main.domain.card.Card
import poker.main.domain.card.CardValue
import poker.hand.Hand

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 16/08/13
 * Time: 18:31
 * To change this template use File | Settings | File Templates.
 */
class PokerUtil {

    static sortCards(List<Card> cards){
        cards.sort {
            it.cardValue.value
        }
    }

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

    static sortHandResults(List<Hand> hands){
        hands.sort{
            it.handType.value
        }

    }
}
