package poker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import poker.domain.game.Game
import poker.domain.player.Player
import poker.domain.request.PlayerRequest
import poker.service.PlayerService

/**
 * Created by matt on 27/06/2014.
 */
@RestController
@RequestMapping("/players")
class PlayerControllerImpl implements PlayerController{

    @Autowired
    private PlayerService playerService

    @Override
    @RequestMapping(method = RequestMethod.POST)
    Game createPlayer(@RequestBody PlayerRequest playerRequest) {
        //Validate it
        playerRequest.validate()

        //TODO: Remove
        playerService.clearDatabase()

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
