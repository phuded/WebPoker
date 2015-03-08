package com.webpoker.domain

import com.webpoker.domain.game.round.Round
import com.webpoker.domain.player.GamePlayer

/**
 * POJO containing correct response for a given logged in user
 */
class RoundResponse{

    Round round
    GamePlayer player

    RoundResponse (Round round, GamePlayer player){
        this.round = round
        this.player = player
    }

}
