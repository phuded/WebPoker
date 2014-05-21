package poker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import poker.domain.game.round.Round;

/**
 * Created by matt on 17/05/2014.
 */
public interface RoundRepository extends MongoRepository<Round, String> {


}