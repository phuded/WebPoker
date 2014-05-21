package poker.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import poker.domain.card.Card;
import poker.domain.card.CardValue;
import poker.domain.card.Suit
import poker.domain.player.Player;
import poker.repository.*
import poker.service.HandDetector;
import poker.service.RoundWinnerDetectorImpl;

@RestController
public class CardController {

    private static final Logger logger = LoggerFactory.getLogger(CardController.class);

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private BettingRoundRepository bettingRoundRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private HandRepository handRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private HandDetector handDetector;

    @Autowired
    private RoundWinnerDetectorImpl roundWinnerDetector;

    @RequestMapping("/cards")
    String getCards() {
        StringBuilder output = new StringBuilder();

        //Delete
        playerRepository.deleteAll()
        cardRepository.deleteAll()

        def playerNames = ["Matt"]//,"Cathy","Ella","Becky"]
        int startingPlayerFunds = 1000;

       // Game game = new Game(playerNames,startingPlayerFunds)
       // println "================================"
      //  game.play()

        playerNames.each {
            playerRepository.save(new Player(it, null))
        }

        Player player = playerRepository.findByName("Matt");

        logger.info("Player: " + player.name);

        for (Player player1 : playerRepository.findAll()) {
            output.append("<br>"+player1);
        }

        List<Card> cards = []
        cards <<  new Card(CardValue.TWO, Suit.HEARTS)
        cards <<  new Card(CardValue.TWO, Suit.SPADES)
        cards <<  new Card(CardValue.THREE, Suit.HEARTS)
        cards <<  new Card(CardValue.FOUR, Suit.HEARTS)
        cards <<  new Card(CardValue.FIVE, Suit.HEARTS)

        cardRepository.save(cards)

        player = playerRepository.findByName("Matt");

        player.addGameCards(cards);

        logger.info("Player cards: " + player.allCards);

        output.append("<br>added cards")

        playerRepository.save(player);

        output.append("<br>saved")

        List<Card> foundCards = cardRepository.findBySuit(Suit.SPADES);
        Card foundCard = foundCards.get(0);

        foundCard.suit = Suit.DIAMONDS;

        cardRepository.save(foundCard);

        output.append("<br>saved card")


        player.allCards.get(0).cardValue = CardValue.ACE

        playerRepository.save(player);

       // player.detectHand();

        handDetector.detectHand(player)

        playerRepository.save(player);


        output.append("<br><br>"+player.bestHand);



        /*
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
               */
        return output.toString();
    }

}