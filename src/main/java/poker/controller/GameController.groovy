package poker.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import poker.domain.game.Game
import poker.repository.CardRepository
import poker.repository.GameRepository
import poker.repository.PlayerRepository
import poker.service.GameService

/**
 * Created by matt on 21/05/2014.
 */
@RestController
class GameController {

    @Autowired
    private GameRepository gameRepository

    @Autowired
    private CardRepository cardRepository

    @Autowired
    private PlayerRepository playerRepository

    @Autowired
    private GameService gameService

    @RequestMapping("/games")
    String startGame() {

        cardRepository.deleteAll()
        gameRepository.deleteAll()
        playerRepository.deleteAll()

        def playerNames = ["Matt","Cathy"]

        Game game = gameService.createNewGame(playerNames,100)

        gameService.startNextRound(game);
    }
}
