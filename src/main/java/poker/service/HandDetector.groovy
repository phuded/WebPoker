package poker.service

import poker.card.Card
import poker.hand.Hand
import poker.player.Player

/**
 * Created by matt on 20/05/2014.
 */

interface HandDetector {

    public detectHand(Player player)

    public List<Hand> detect (List<Card> cards)
}
