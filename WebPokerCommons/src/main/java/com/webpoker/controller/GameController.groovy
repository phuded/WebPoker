package com.webpoker.controller

import com.webpoker.domain.request.GameRequest
import com.webpoker.domain.game.Game

/**
 * Created by matt on 21/05/2014.
 */

interface GameController {

    /**
     * Create a new Game
     * @param players
     * @return
     */
    Game createGame(GameRequest gameRequest)

    /**
     * List all Games
     * @return
     */
    List<Game> getGames(boolean showAll)

    /**
     * Get a Game
     * @return
     */
    Game getGame(String gameId)

    /**
     * Add a player to a game
     * @param gameId
     * @param platerId
     */
    Game addPlayer(String gameId)

}
