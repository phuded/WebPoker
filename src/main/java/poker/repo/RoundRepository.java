package poker.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import poker.game.round.Round;

/**
 * Created by matt on 17/05/2014.
 */
public interface RoundRepository extends MongoRepository<Round, String> {


}