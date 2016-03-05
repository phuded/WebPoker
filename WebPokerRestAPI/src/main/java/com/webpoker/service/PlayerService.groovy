package com.webpoker.service

import com.webpoker.domain.player.Player
import com.webpoker.domain.request.PlayerRequest
import com.webpoker.exception.PokerNotFoundException
import com.webpoker.repository.PlayerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
/**
 * Created by matt on 21/05/2014.
 */
@Service
class PlayerService {

    @Autowired
    PlayerRepository playerRepository

    /**
     * Create a new Player
     * @param playerNames
     * @param startingFunds
     * @return
     */
    Player createNewPlayer(PlayerRequest playerRequest){

        Player player = new Player(playerRequest.name, playerRequest.firstName, playerRequest.lastName, playerRequest.role, playerRequest.password)

        playerRepository.save(player)

        return player
    }

    /**
     * Load Player
     * @param userName
     * @return
     */
    Player loadPlayerByName(String userName){
       Player player = playerRepository.findByName(userName)

       if(!player){
           throw new PokerNotFoundException("No Player found with name: " + userName)
       }

       return player
    }

    /**
     * Load Player
     * @param gameId
     * @return
     */
    Player loadPlayer(String playerId){
        Player player = playerRepository.findOne(playerId)

        if(!player){
            throw new PokerNotFoundException("No Player found with ID: " + playerId)
        }

        return player
    }

    /**
     * List all Games
     * @return
     */
    List<Player> getAllPlayers(){
        return playerRepository.findAll()
    }

    //TODO Remove later
    def clearDatabase(){
        playerRepository.deleteAll()
    }
}
