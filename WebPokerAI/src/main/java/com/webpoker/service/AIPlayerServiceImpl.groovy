package com.webpoker.service

import org.springframework.beans.factory.annotation.Autowired
import com.webpoker.domain.hand.Hand
import com.webpoker.domain.player.GamePlayer
import com.webpoker.domain.request.BetRequest
import com.webpoker.plugin.NormalAIPlayer
import com.webpoker.plugin.AIPlayer

/**
 * Created by matt on 05/08/2014.
 */
class AIPlayerServiceImpl implements AIPlayerService{

    @Autowired
    HandDetectorService handDetectorService

    @Override
    BetRequest performBet(GamePlayer player) {
        //Determine hand
        List<Hand> hands = handDetectorService.detect(player);
        Hand bestHand = hands.last();

        AIPlayer aiPlayer = new NormalAIPlayer();

        return aiPlayer.determineBet(bestHand, null, null, null, null);
    }


}
