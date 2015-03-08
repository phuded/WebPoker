package com.webpoker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.webpoker.domain.game.Game;

/**
 * Created by matt on 17/05/2014.
 */
public interface GameRepository extends MongoRepository<Game, Integer> {

}