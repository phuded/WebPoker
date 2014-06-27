package poker.service

import poker.domain.card.Card
import poker.domain.hand.Hand
import poker.domain.player.GamePlayer

/**
 * Created by matt on 20/05/2014.
 */

interface HandDetector {

    public detectHand(GamePlayer player)

    public List<Hand> detect (List<Card> cards)
}
