package poker.service

import poker.domain.player.GamePlayer
import poker.domain.request.BetRequest

/**
 * Created by matt on 05/08/2014.
 */
interface AIPlayerService {

    BetRequest performBet(GamePlayer player)
}
