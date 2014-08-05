package poker.service

import org.springframework.beans.factory.annotation.Autowired
import poker.domain.hand.Hand
import poker.domain.player.GamePlayer
import poker.domain.request.BetRequest
import poker.plugin.AIPlayer
import poker.plugin.NormalAIPlayer

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
