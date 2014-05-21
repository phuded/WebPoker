package poker.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import poker.card.Card;
import poker.card.CardValue;
import poker.card.Suit;

import java.util.List;

/**
 * Created by matt on 17/05/2014.
 */
public interface CardRepository extends MongoRepository<Card, String> {

    public List<Card> findBySuit(Suit suit);
    public List<Card> findByCardValue(CardValue value);

}