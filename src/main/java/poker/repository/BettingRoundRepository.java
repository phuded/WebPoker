package poker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import poker.domain.game.bettinground.BettingRound;

/**
 * Created by matt on 17/05/2014.
 */
public interface BettingRoundRepository extends MongoRepository<BettingRound, String> {


}