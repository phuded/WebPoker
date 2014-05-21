package poker.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import poker.hand.Hand;

/**
 * Created by matt on 17/05/2014.
 */
public interface HandRepository extends MongoRepository<Hand, String> {


}