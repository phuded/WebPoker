package poker.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import poker.domain.game.Game
import poker.domain.game.round.Round
import poker.repository.GameRepository

/**
 * Created by matt on 21/05/2014.
 */
@Service
class GameService {

    static final Logger logger = LoggerFactory.getLogger(GameService.class)

    @Autowired
    GameRepository gameRepository

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
     * Find the current round
     * @param game
     * @return
     */
    Round findCurrentRound(Game game){
        //Get the current round
        Round round = game.rounds.find {Round round ->
            round.isCurrentRound
        }

        return round
    }

    /**
     * Create a new round and set it to current TODO: Round Limited currently
     * @param game
     * @return
     */
    Round createNextRound(Game game){

        int numberOfRounds = game.rounds.size()

        if(numberOfRounds < Game.tempRoundLimit){
            //Reset players cards and hands
            game.players*.resetBetweenRounds()

            //New round and play
            Round round = new Round(game, numberOfRounds + 1)
            game.rounds << round

            //Set to current
            round.isCurrentRound = true

            //Set first betting round to current
            round.bettingRounds.first().isCurrentBettingRound = true

            //Set the first player to current
            game.players.first().isCurrentPlayer = true

            //Deal the cards
            round.bettingRounds.first().dealCards(game,round)

            logger.info("Saving new Round: " + round.roundNumber)
            gameRepository.save(game)

            return round
        }
    }



    //TODO Remove later
    def clearDatabase(){
        gameRepository.deleteAll()
    }
}
