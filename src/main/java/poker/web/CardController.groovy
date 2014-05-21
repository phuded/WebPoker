package poker.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import poker.card.Card;
import poker.card.CardValue;
import poker.card.Suit
import poker.player.Player;
import poker.repo.*
import poker.service.HandDetector;
import poker.service.RoundWinnerDetector;

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
    private RoundWinnerDetector roundWinnerDetector;

    @RequestMapping("/cards")
    String getCards() {
        StringBuilder output = new StringBuilder();

        //Delete
        playerRepository.deleteAll()
        cardRepository.deleteAll()

        def playerNames = ["Matt","Cathy","Ella","Becky"]
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