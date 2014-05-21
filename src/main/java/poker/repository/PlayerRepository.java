package poker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import poker.domain.player.Player;


/**
 * Created by matt on 17/05/2014.
 */
public interface PlayerRepository extends MongoRepository<Player, String> {

    public Player findByName(String name);

}