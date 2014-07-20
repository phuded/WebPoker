package poker.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import poker.domain.player.GamePlayer
import poker.domain.player.Player
import poker.domain.request.GameRequest
import poker.domain.game.Game
import poker.domain.security.PokerUser
import poker.exception.PokerException
import poker.exception.PokerNotFoundException
import poker.repository.GameRepository

/**
 * Created by matt on 21/05/2014.
 */
@Service
class GameService {

    static final Logger logger = LoggerFactory.getLogger(GameService.class)

    @Autowired
    GameRepository gameRepository

    @Autowired
    PlayerService playerService

    /**
     * Create a new Game
     * @param playerNames
     * @param startingFunds
     * @return
     */
    Game createNewGame(GameRequest gameRequest){

        Game game = new Game(gameRequest.name, gameRequest.startingPlayerFunds)

        gameRepository.save(game)

        return game
    }

    /**
     * Load Game
     * @param gameId
     * @return
     */
    Game loadGame(String gameId){
       Game game = gameRepository.findOne(gameId)

       if(!game){
           throw new PokerNotFoundException("No game found with ID: " + gameId)
       }

       return game
    }

    /**
     * Load Game
     * @param gameId
     * @return
     */
    Game loadGame(String gameId, String userName){
        Game game = gameRepository.findOne(gameId)

        if(!game || !game.getPlayer(userName)){
            throw new PokerNotFoundException("No game found with ID: " + gameId)
        }

        return game
    }

    /**
     * List all Games
     * @return
     */
    List<Game> getAllGames(){
        return gameRepository.findAll()
    }

    /**
     * Get all Games player is playing in
     * @param pokerUser
     * @return
     */
    List<Game> getAllCurrentGames(String playerName){

       List<Game> allGames = allGames

       return allGames.findAll{ Game game ->
           game.getPlayer(playerName)
       }
    }

    /**
     * Add a player to a game
     * @param game
     * @param player
     * @return
     */
    Game addToPlayerToGame(String gameId, PokerUser user){

        Game game = gameRepository.findOne(gameId)

        //Load the player
        Player player = playerService.loadPlayer(user.id)

        //Check if player already a member
        if(game.getPlayer(player.name)){
            throw new PokerException("Player: " + player.name + " is already playing in game: " + gameId)
        }

        //TODO - Change this so players can join mid-game??
        if(!game.rounds.empty){
            throw new PokerException("Player: " + player.name + " cannot be added to game: " + gameId + " as it is started.")
        }

        game.addPlayer(player);

        gameRepository.save(game)

        return game
    }

    //TODO Remove later
    def clearDatabase(){
        gameRepository.deleteAll()
    }
}
