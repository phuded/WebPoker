package com.webpoker.service

import com.webpoker.domain.player.GamePlayer
import com.webpoker.domain.request.BetRequest

/**
 * Created by matt on 05/08/2014.
 */
interface AIPlayerService {

    BetRequest performBet(GamePlayer player)
}
