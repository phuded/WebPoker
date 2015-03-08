package com.webpoker.controller

import com.webpoker.domain.player.Player
import com.webpoker.domain.request.PlayerRequest

/**
 * Created by matt on 21/05/2014.
 */

interface PlayerController {

    /**
     * Create a new Player
     * @param players
     * @return
     */
    Player createPlayer(PlayerRequest playerRequest)

    /**
     * List all Players
     * @return
     */
    List<Player> getPlayers()

    /**
     * Get a Player
     * @return
     */
    Player getPlayer(String gameId)

}
