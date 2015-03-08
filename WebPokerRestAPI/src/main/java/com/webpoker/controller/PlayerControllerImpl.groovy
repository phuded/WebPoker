package com.webpoker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import com.webpoker.domain.player.Player
import com.webpoker.domain.request.PlayerRequest
import com.webpoker.service.PlayerService

/**
 * Created by matt on 27/06/2014.
 * TODO: Check roles
 */
@RestController
@RequestMapping("/players")
class PlayerControllerImpl implements PlayerController{

    @Autowired
    PlayerService playerService

    @Override
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    Player createPlayer(@RequestBody PlayerRequest playerRequest) {
        //Validate it
        playerRequest.validate()

        Player player = playerService.createNewPlayer(playerRequest)

        return player
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    List<Player> getPlayers() {

        return playerService.getAllPlayers()
    }

    @Override
    @RequestMapping(value="/{playerId}",method = RequestMethod.GET)
    Player getPlayer(@PathVariable String playerId) {

        return playerService.loadPlayer(playerId)
    }
}
