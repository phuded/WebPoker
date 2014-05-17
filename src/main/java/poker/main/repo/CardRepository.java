package poker.main.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import poker.main.domain.card.Card;
import poker.main.domain.card.CardValue;
import poker.main.domain.card.Suit;

import java.util.List;

/**
 * Created by matt on 17/05/2014.
 */
public interface CardRepository extends MongoRepository<Card, String> {

    public List<Card> findBySuit(Suit suit);
    public List<Card> findByCardValue(CardValue value);

}