package com.webpoker.service

import com.webpoker.domain.card.Card
import com.webpoker.domain.player.GamePlayer
import com.webpoker.domain.hand.Hand

/**
 * Created by matt on 20/05/2014.
 */

interface HandDetectorService {

    public detectHand(GamePlayer player)

    public List<Hand> detect (List<Card> cards)
}
