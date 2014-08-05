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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HandDetectorServiceImpl.class)
class HandDetectorTest extends GroovyTestCase {

    static final Logger logger = LoggerFactory.getLogger(HandDetectorTest.class)
    
    @Autowired
    HandDetectorService handDetectorService

    @Test
    void testPair() {

        def cards = []
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.FOUR,Suit.DIAMONDS)
        cards << new Card(CardValue.ACE,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.SPADES)
        cards << new Card(CardValue.FOUR,Suit.CLUBS)
        cards << new Card(CardValue.KING,Suit.DIAMONDS)
        cards << new Card(CardValue.QUEEN,Suit.DIAMONDS)

        logger.info("testPair cards: " + cards)

        def results = handDetectorService.detect(cards)

        logger.info("testPair results: " + results)

        assert results.size() == 1 && results.first().handType == HandType.PAIR
        logger.info("================================")
    }

    @Test
    void testThreeOfAKind() {

        def cards = []
        cards << new Card(CardValue.ACE,Suit.HEARTS)
        cards << new Card(CardValue.FOUR,Suit.DIAMONDS)
        cards << new Card(CardValue.ACE,Suit.SPADES)
        cards << new Card(CardValue.THREE,Suit.SPADES)
        cards << new Card(CardValue.FIVE,Suit.CLUBS)
        cards << new Card(CardValue.ACE,Suit.DIAMONDS)
        cards << new Card(CardValue.QUEEN,Suit.DIAMONDS)

        logger.info("testThreeOfAKind cards: " + cards)

        def results = handDetectorService.detect(cards)

        logger.info("testThreeOfAKind results: " + results)

        assert results.size() == 1 && results.first().handType == HandType.THREE_OF_A_KIND
        logger.info("================================")
    }

    @Test
    void testTwoPair() {

        def cards = []
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.DIAMONDS)
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.SPADES)
        cards << new Card(CardValue.ACE,Suit.CLUBS)

        logger.info("testTwoPair cards: " + cards)

        def results = handDetectorService.detect(cards)

        logger.info("testTwoPair results: " + results)

        assert results.size() == 3 && results.findAll{it.handType == HandType.TWO_PAIR}.size()==1
        logger.info("================================")
    }

    @Test
    void testBestTwoPair() {

        def cards = []
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.DIAMONDS)
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.SPADES)
        cards << new Card(CardValue.ACE,Suit.CLUBS)
        cards << new Card(CardValue.ACE,Suit.HEARTS)

        logger.info("testBestTwoPair cards: " + cards)

        def results = handDetectorService.detect(cards)

        logger.info("testBestTwoPair results: " + results)

        assert results.size() == 4 && results.findAll{it.handType == HandType.TWO_PAIR}.size()==1
        logger.info("================================")
    }

    @Test
    void testStraight() {

        def cards = []
        cards << new Card(CardValue.TEN,Suit.HEARTS)
        cards << new Card(CardValue.JACK,Suit.CLUBS)
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.QUEEN,Suit.HEARTS)
        cards << new Card(CardValue.KING,Suit.DIAMONDS)
        cards << new Card(CardValue.ACE,Suit.DIAMONDS)
        cards << new Card(CardValue.NINE,Suit.HEARTS)

        logger.info("testStraight cards: " + cards)

        def results = handDetectorService.detect(cards)

        logger.info("testStraight results: " + results)

        assert results.size() == 1 && results.first().handType == HandType.STRAIGHT
        logger.info("================================")
    }

    @Test
    void testLowStraight() {

        def cards = []
        cards << new Card(CardValue.ACE,Suit.HEARTS)
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.KING,Suit.CLUBS)
        cards << new Card(CardValue.THREE,Suit.HEARTS)
        cards << new Card(CardValue.FOUR,Suit.HEARTS)
        cards << new Card(CardValue.FIVE,Suit.DIAMONDS)
        cards << new Card(CardValue.SEVEN,Suit.DIAMONDS)

        logger.info("testLowStraight cards: " + cards)

        def results = handDetectorService.detect(cards)

        logger.info("testLowStraight results: " + results)

        assert results.size() == 1 && results.first().handType == HandType.STRAIGHT
        logger.info("================================")
    }

    @Test
    void testFlush() {

        def cards = []
        cards << new Card(CardValue.EIGHT,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.SPADES)
        cards << new Card(CardValue.JACK,Suit.HEARTS)
        cards << new Card(CardValue.QUEEN,Suit.HEARTS)
        cards << new Card(CardValue.KING,Suit.HEARTS)
        cards << new Card(CardValue.TWO,Suit.DIAMONDS)
        cards << new Card(CardValue.ACE,Suit.HEARTS)

        logger.info("testFlush cards: " + cards)

        def results = handDetectorService.detect(cards)

        logger.info("testFlush results: " + results)

        assert results.size() == 1
        logger.info("================================")
    }

    @Test
    void testFourOfAKind() {

        def cards = []
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.TWO,Suit.DIAMONDS)
        cards << new Card(CardValue.TWO,Suit.SPADES)
        cards << new Card(CardValue.TWO,Suit.CLUBS)
        cards << new Card(CardValue.FOUR,Suit.SPADES)
        cards << new Card(CardValue.KING,Suit.CLUBS)
        cards << new Card(CardValue.ACE,Suit.HEARTS)

        logger.info("testFourOfAKind cards: " + cards)

        def results = handDetectorService.detect(cards)

        logger.info("testFourOfAKind results: " + results)

        assert results.size() == 1 && results.first().handType == HandType.FOUR_OF_A_KIND
        logger.info("================================")
    }

    @Test
    void testFullHouse() {

        def cards = []
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.TWO,Suit.DIAMONDS)
        cards << new Card(CardValue.TWO,Suit.SPADES)
        cards << new Card(CardValue.THREE,Suit.CLUBS)
        cards << new Card(CardValue.FOUR,Suit.SPADES)
        cards << new Card(CardValue.FOUR,Suit.CLUBS)
        cards << new Card(CardValue.FOUR,Suit.HEARTS)

        logger.info("testFullHouse cards: " + cards)

        def results = handDetectorService.detect(cards)

        logger.info("testFullHouse results: " + results)

        assert results.size() == 3 && results.findAll{it.handType == HandType.FULLHOUSE}.size()==1
        logger.info("================================")
    }

    @Test
    void testStraightFlush() {

        def cards = []
        cards << new Card(CardValue.THREE,Suit.DIAMONDS)
        cards << new Card(CardValue.FOUR,Suit.SPADES)
        cards << new Card(CardValue.FIVE,Suit.HEARTS)
        cards << new Card(CardValue.SIX,Suit.HEARTS)
        cards << new Card(CardValue.SEVEN,Suit.HEARTS)
        cards << new Card(CardValue.EIGHT,Suit.HEARTS)
        cards << new Card(CardValue.NINE,Suit.HEARTS)

        logger.info("testStraightFlush cards: " + cards)

        def results = handDetectorService.detect(cards)

        logger.info("testStraightFlush results: " + results)

        assert results.size() == 3 && results.last().handType == HandType.STRAIGHT_FLUSH
        logger.info("================================")
    }

    @Test
    void testMismatchedStraightFlush() {

        def cards = []
        cards << new Card(CardValue.THREE,Suit.DIAMONDS)
        cards << new Card(CardValue.FOUR,Suit.HEARTS)
        cards << new Card(CardValue.FIVE,Suit.HEARTS)
        cards << new Card(CardValue.SIX,Suit.HEARTS)
        cards << new Card(CardValue.SEVEN,Suit.HEARTS)
        cards << new Card(CardValue.EIGHT,Suit.HEARTS)
        cards << new Card(CardValue.TEN,Suit.HEARTS)

        logger.info("testMismatchedStraightFlush cards: " + cards)

        def results = handDetectorService.detect(cards)

        logger.info("testMismatchedStraightFlush results: " + results)

        assert results.size() == 3 && results.last().handType == HandType.STRAIGHT_FLUSH
        logger.info("================================")
    }

    @Test
    void testMismatchedLowStraightFlush() {

        def cards = []
        cards << new Card(CardValue.ACE,Suit.HEARTS)
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.HEARTS)
        cards << new Card(CardValue.FOUR,Suit.HEARTS)
        cards << new Card(CardValue.FIVE,Suit.HEARTS)
        cards << new Card(CardValue.SEVEN,Suit.HEARTS)
        cards << new Card(CardValue.TEN,Suit.HEARTS)

        logger.info("testMismatchedLowStraightFlush cards: " + cards)

        def results = handDetectorService.detect(cards)

        logger.info("testMismatchedLowStraightFlush results: " + results)

        assert results.size() == 3 && results.last().handType == HandType.STRAIGHT_FLUSH
        logger.info("================================")
    }

    @Test
    void testRoyalFlush() {

      def cards = []
      cards << new Card(CardValue.TWO,Suit.HEARTS)
      cards << new Card(CardValue.THREE,Suit.HEARTS)
      cards << new Card(CardValue.TEN,Suit.HEARTS)
      cards << new Card(CardValue.JACK,Suit.HEARTS)
      cards << new Card(CardValue.QUEEN,Suit.HEARTS)
      cards << new Card(CardValue.KING,Suit.HEARTS)
      cards << new Card(CardValue.ACE,Suit.HEARTS)

      logger.info("testRoyalFlush cards: " + cards)

      def results = handDetectorService.detect(cards)

      logger.info("testRoyalFlush results: " + results)

      assert results.size() == 4 && results.last().handType == HandType.ROYAL_FLUSH
      logger.info("================================")
    }
}