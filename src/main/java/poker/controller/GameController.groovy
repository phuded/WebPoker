package poker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import poker.domain.game.Game
import poker.repository.GameRepository

/**
 * Created by matt on 21/05/2014.
 */
@RestController
class GameController {

    @Autowired
    private GameRepository gameRepository

    @RequestMapping("/games")
    String startGame() {

    def playerNames = ["Matt","Cathy"]

     Game game = new Game(playerNames,100)
     gameRepository.deleteAll();
     gameRepository.save(game)

     game.play()

     null
    }
}
