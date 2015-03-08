package com.webpoker.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.webpoker.domain.player.Player
import com.webpoker.domain.request.GameRequest
import com.webpoker.domain.game.Game
import com.webpoker.domain.security.PokerUser
import com.webpoker.exception.PokerException
import com.webpoker.exception.PokerNotFoundException
import com.webpoker.repository.GameRepository

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
     * Load Game - no security check
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
     * Load Game which a player belongs to
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
     * TODO: Need to create round automatically...
     * @param game
     * @param player
     * @return
     */
    Game addToPlayerToGame(String gameId, PokerUser user){
        //Get the game
        Game game = loadGame(gameId)

        //Load the player
        Player player = playerService.loadPlayer(user.id)

        //Check if player already a member, if so do nothing
        if(game.getPlayer(player.name)){
            return game
        }

        //TODO - Change this so players can join mid-game??
        if(!game.rounds.empty){
            throw new PokerException("Player: " + player.name + " cannot be added to game: " + gameId + " as it is started.")
        }

        //Add the player to the game
        game.addPlayer(player)

        gameRepository.save(game)

        return game
    }

    //TODO Remove later
    def clearDatabase(){
        gameRepository.deleteAll()
    }
}
