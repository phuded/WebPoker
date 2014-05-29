package poker.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import poker.domain.game.Game
import poker.domain.game.round.Round
import poker.repository.CardRepository
import poker.repository.GameRepository
import poker.repository.PlayerRepository

/**
 * Created by matt on 21/05/2014.
 */
@Service
class GameService {

    @Autowired
    GameRepository gameRepository

    @Autowired
    CardRepository cardRepository

    @Autowired
    private PlayerRepository playerRepository


    @Autowired
    RoundService roundService

    /**
     * Create a new Game
     * @param playerNames
     * @param startingFunds
     * @return
     */
    Game createNewGame(List<String> playerNames, Integer startingFunds){
        Game game = new Game(playerNames,startingFunds)

        gameRepository.save(game)

        return game
    }

    /**
     * Load Game
     * @param gameId
     * @return
     */
    Game loadGame(String gameId){
       return gameRepository.findOne(gameId)
    }

    /**
     * List all Games
     * @return
     */
    List<Game> getAllGames(){
        return gameRepository.findAll()
    }

    /**
     * Begin the Game TODO: Round Limited currently
     * @param game
     * @return
     */
    def startNextRound(Game game){

        if(game.rounds.size() < Game.tempRoundLimit){
            //Reset players cards and hands
            game.players*.resetBetweenRounds()

            //New round and play
            Round round = new Round(game)
            game.rounds << round

            println "Saving... new round"
            gameRepository.save(game)

            roundService.playRound(game, round)
        }
    }

    //TODO Sort
    def clearDatabase(){
        cardRepository.deleteAll()
        gameRepository.deleteAll()
        playerRepository.deleteAll()
    }
}
