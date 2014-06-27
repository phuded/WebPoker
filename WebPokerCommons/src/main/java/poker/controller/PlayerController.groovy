package poker.controller

import poker.domain.game.Game
import poker.domain.player.Player
import poker.domain.request.PlayerRequest

/**
 * Created by matt on 21/05/2014.
 */

interface PlayerController {

    /**
     * Create a new Player
     * @param players
     * @return
     */
    Game createPlayer(PlayerRequest playerRequest)

    /**
     * List all Players
     * @return
     */
    List<Player> getPlayers()

    /**
     * Get a Player
     * @return
     */
    Player getPlayer(Integer gameId)

}
