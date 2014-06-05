package poker.service

import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import poker.domain.card.Card
import poker.domain.card.CardValue
import poker.domain.card.Suit
import poker.domain.hand.HandType
import poker.domain.player.Player

/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 25/08/2013
 * Time: 12:39
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = [HandDetectorImpl.class,RoundWinnerDetectorImpl.class])
class RoundWinnerDetectorTest extends GroovyTestCase {

    static final Logger logger = LoggerFactory.getLogger(RoundWinnerDetectorTest.class)

    @Autowired
    HandDetector handDetector

    @Autowired
    RoundWinnerDetector roundWinnerDetector

    @Test
    void testDifferentHighCards() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.FOUR,Suit.DIAMONDS)
        gameCards << new Card(CardValue.ACE,Suit.HEARTS)
        gameCards << new Card(CardValue.KING,Suit.DIAMONDS)
        gameCards << new Card(CardValue.QUEEN,Suit.DIAMONDS)

        Player player1 = new Player("Player 1",1,null)
        player1.receiveCard(new Card(CardValue.TEN,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.NINE,Suit.SPADES))

        player1.addGameCards(gameCards)

        Player player2 = new Player("Player 2",2,null)
        player2.receiveCard(new Card(CardValue.TEN,Suit.HEARTS))
        player2.receiveCard(new Card(CardValue.EIGHT,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<Player> players = [player1,player2]

        players.each{ Player player ->
            handDetector.detectHand(player)
            logger.info("testDifferentHighCards: "+ player.name + " - Best hand: " + player.bestHand)
        }

        List <Player> winners = roundWinnerDetector.detectWinners(players)
        logger.info("testDifferentHighCards: Winners: " + winners)

        assert winners.size() == 1 && winners.first().name == "Player 1"
        logger.info("================================")
    }

    @Test
    void testSamePairsWithSameKickers() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.ACE,Suit.HEARTS)
        gameCards << new Card(CardValue.KING,Suit.DIAMONDS)
        gameCards << new Card(CardValue.QUEEN,Suit.DIAMONDS)

        Player player1 = new Player("Player 1",1,null)
        player1.receiveCard(new Card(CardValue.FIVE,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.THREE,Suit.SPADES))

        player1.addGameCards(gameCards)

        Player player2 = new Player("Player 2",2,null)
        player2.receiveCard(new Card(CardValue.SIX,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.EIGHT,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<Player> players = [player1,player2]

        players.each{ Player player ->
            handDetector.detectHand(player)
            logger.info("testSamePairsWithSameKickers: "+ player.name + " - Best hand: " + player.bestHand)
        }

        List <Player> winners = roundWinnerDetector.detectWinners(players)
        logger.info("testSamePairsWithSameKickers: Winners: " + winners)

        assert winners.size() == 2
        logger.info("================================")
    }

    @Test
    void testSamePairsWithDifferentKickers() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.TEN,Suit.HEARTS)
        gameCards << new Card(CardValue.KING,Suit.DIAMONDS)
        gameCards << new Card(CardValue.QUEEN,Suit.DIAMONDS)

        Player player1 = new Player("Player 1",1,null)
        player1.receiveCard(new Card(CardValue.ACE,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.THREE,Suit.SPADES))

        player1.addGameCards(gameCards)

        Player player2 = new Player("Player 2",2,null)
        player2.receiveCard(new Card(CardValue.SIX,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.EIGHT,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<Player> players = [player1,player2]

        players.each{ Player player ->
            handDetector.detectHand(player)
            logger.info("testSamePairsWithDifferentKickers: "+ player.name + " - Best hand: " + player.bestHand)
        }

        List <Player> winners = roundWinnerDetector.detectWinners(players)
        logger.info("testSamePairsWithDifferentKickers: Winners: " + winners)

        assert winners.size() == 1 && winners.first().name == "Player 1"
        logger.info("================================")
    }

    @Test
    void testDifferentThreesInFullHouse() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.SIX,Suit.HEARTS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)
        gameCards << new Card(CardValue.FOUR,Suit.DIAMONDS)

        Player player1 = new Player("Player 1",1,null)
        player1.receiveCard(new Card(CardValue.FOUR,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.FOUR,Suit.SPADES))

        player1.addGameCards(gameCards)

        Player player2 = new Player("Player 2",2,null)
        player2.receiveCard(new Card(CardValue.SIX,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.SIX,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<Player> players = [player1,player2]

        players.each{ Player player ->
            handDetector.detectHand(player)
            logger.info("testDifferentThreesInFullHouse: "+ player.name + " - Best hand: " + player.bestHand)
        }

        List <Player> winners = roundWinnerDetector.detectWinners(players)
        logger.info("testDifferentThreesInFullHouse: Winners: " + winners)

        assert winners.size() == 1 && winners.first().name == "Player 2"
        logger.info("================================")
    }

    @Test
    void testDifferentThreesInFullHouse2() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)

        Player player1 = new Player("Player 1",1,null)
        player1.receiveCard(new Card(CardValue.FIVE,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.FOUR,Suit.SPADES))

        player1.addGameCards(gameCards)

        Player player2 = new Player("Player 2",2,null)
        player2.receiveCard(new Card(CardValue.SEVEN,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.SIX,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<Player> players = [player1,player2]

        players.each{ Player player ->
            handDetector.detectHand(player)
            logger.info("testDifferentThreesInFullHouse2: "+ player.name + " - Best hand: " + player.bestHand)
        }

        List <Player> winners = roundWinnerDetector.detectWinners(players)
        logger.info("testDifferentThreesInFullHouse2: Winners: " + winners)

        assert winners.size() == 1 && winners.first().name == "Player 1"
        logger.info("================================")
    }

    @Test
    void testDifferentPairsInFullHouse() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)
        gameCards << new Card(CardValue.SIX,Suit.DIAMONDS)

        Player player1 = new Player("Player 1",1,null)
        player1.receiveCard(new Card(CardValue.SIX,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.FOUR,Suit.SPADES))

        player1.addGameCards(gameCards)

        Player player2 = new Player("Player 2",2,null)
        player2.receiveCard(new Card(CardValue.FIVE,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.NINE,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<Player> players = [player1,player2]

        players.each{ Player player ->
            handDetector.detectHand(player)
            logger.info("testDifferentPairsInFullHouse: "+ player.name + " - Best hand: " + player.bestHand)
        }

        List <Player> winners = roundWinnerDetector.detectWinners(players)
        logger.info("testDifferentPairsInFullHouse: Winners: " + winners)

        assert winners.size() == 1 && winners.first().name == "Player 1"
        logger.info("================================")
    }

    @Test
    void testFourOfAKindFromFullHouse() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)

        Player player1 = new Player("Player 1",1,null)
        player1.receiveCard(new Card(CardValue.FIVE,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.FOUR,Suit.SPADES))

        player1.addGameCards(gameCards)

        Player player2 = new Player("Player 2",2,null)
        player2.receiveCard(new Card(CardValue.TWO,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.SIX,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<Player> players = [player1,player2]

        players.each{ Player player ->
            handDetector.detectHand(player)
            logger.info("testFourOfAKindFromFullHouse: "+ player.name + " - Best hand: " + player.bestHand)
        }

        List <Player> winners = roundWinnerDetector.detectWinners(players)
        logger.info("testFourOfAKindFromFullHouse: Winners: " + winners)

        assert winners.size() == 1 && winners.first().name == "Player 2" && winners.first().bestHand.handType == HandType.FOUR_OF_A_KIND
        logger.info("================================")
    }
}
