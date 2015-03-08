package com.webpoker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.webpoker.domain.player.Player;

/**
 * Created by matt on 17/05/2014.
 */
public interface PlayerRepository extends MongoRepository<Player, String> {

     Player findByName(String name);
}