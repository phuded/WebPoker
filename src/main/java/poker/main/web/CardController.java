package poker.main.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import poker.main.domain.card.Card;
import poker.main.domain.card.CardValue;
import poker.main.domain.card.Suit;
import poker.main.repo.CardRepository;

@RestController
public class CardController {

    @Autowired
    private CardRepository repository;

    @RequestMapping("/cards")
    String getCards() {

        StringBuilder output = new StringBuilder();

        repository.deleteAll();

        // save a couple of cards
        repository.save(new Card(CardValue.TWO, Suit.HEARTS));
        repository.save(new Card(CardValue.FOUR,Suit.DIAMONDS));
        repository.save(new Card(CardValue.ACE,Suit.HEARTS));
        repository.save(new Card(CardValue.THREE,Suit.SPADES));

        // fetch all cards
        output.append("Cards found with findAll():");
        output.append("<br>-------------------------------");
        for (Card customer : repository.findAll()) {
            output.append("<br>"+customer);
        }

        // fetch an individual card
        output.append("<br>Cards found with findBySuit('Hearts'):");
        output.append("<br>--------------------------------");
        output.append("<br>"+repository.findBySuit(Suit.HEARTS));

        output.append("<br>Cards found with findByValue('3'):");
        output.append("<br>--------------------------------");
        for (Card customer : repository.findByCardValue(CardValue.THREE)) {
            output.append("<br>"+customer);
        }

        return output.toString();
    }

}