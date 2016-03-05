package com.webpoker.service

import com.webpoker.domain.card.Card
import com.webpoker.domain.card.CardValue
import com.webpoker.domain.card.Suit
import com.webpoker.domain.hand.HandType
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HandDetectorServiceImpl.class)
class HandDetectorTest extends GroovyTestCase {

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

        log.info("testPair cards: " + cards)

        def results = handDetectorService.detect(cards)

        log.info("testPair results: " + results)

        assert results.size() == 1 && results.first().handType == HandType.PAIR
        log.info("================================")
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

        log.info("testThreeOfAKind cards: " + cards)

        def results = handDetectorService.detect(cards)

        log.info("testThreeOfAKind results: " + results)

        assert results.size() == 1 && results.first().handType == HandType.THREE_OF_A_KIND
        log.info("================================")
    }

    @Test
    void testTwoPair() {

        def cards = []
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.DIAMONDS)
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.SPADES)
        cards << new Card(CardValue.ACE,Suit.CLUBS)

        log.info("testTwoPair cards: " + cards)

        def results = handDetectorService.detect(cards)

        log.info("testTwoPair results: " + results)

        assert results.size() == 3 && results.findAll{it.handType == HandType.TWO_PAIR}.size()==1
        log.info("================================")
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

        log.info("testBestTwoPair cards: " + cards)

        def results = handDetectorService.detect(cards)

        log.info("testBestTwoPair results: " + results)

        assert results.size() == 4 && results.findAll{it.handType == HandType.TWO_PAIR}.size()==1
        log.info("================================")
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

        log.info("testStraight cards: " + cards)

        def results = handDetectorService.detect(cards)

        log.info("testStraight results: " + results)

        assert results.size() == 1 && results.first().handType == HandType.STRAIGHT
        log.info("================================")
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

        log.info("testLowStraight cards: " + cards)

        def results = handDetectorService.detect(cards)

        log.info("testLowStraight results: " + results)

        assert results.size() == 1 && results.first().handType == HandType.STRAIGHT
        log.info("================================")
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

        log.info("testFlush cards: " + cards)

        def results = handDetectorService.detect(cards)

        log.info("testFlush results: " + results)

        assert results.size() == 1
        log.info("================================")
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

        log.info("testFourOfAKind cards: " + cards)

        def results = handDetectorService.detect(cards)

        log.info("testFourOfAKind results: " + results)

        assert results.size() == 1 && results.first().handType == HandType.FOUR_OF_A_KIND
        log.info("================================")
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

        log.info("testFullHouse cards: " + cards)

        def results = handDetectorService.detect(cards)

        log.info("testFullHouse results: " + results)

        assert results.size() == 3 && results.findAll{it.handType == HandType.FULLHOUSE}.size()==1
        log.info("================================")
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

        log.info("testStraightFlush cards: " + cards)

        def results = handDetectorService.detect(cards)

        log.info("testStraightFlush results: " + results)

        assert results.size() == 3 && results.last().handType == HandType.STRAIGHT_FLUSH
        log.info("================================")
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

        log.info("testMismatchedStraightFlush cards: " + cards)

        def results = handDetectorService.detect(cards)

        log.info("testMismatchedStraightFlush results: " + results)

        assert results.size() == 3 && results.last().handType == HandType.STRAIGHT_FLUSH
        log.info("================================")
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

        log.info("testMismatchedLowStraightFlush cards: " + cards)

        def results = handDetectorService.detect(cards)

        log.info("testMismatchedLowStraightFlush results: " + results)

        assert results.size() == 3 && results.last().handType == HandType.STRAIGHT_FLUSH
        log.info("================================")
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

      log.info("testRoyalFlush cards: " + cards)

      def results = handDetectorService.detect(cards)

      log.info("testRoyalFlush results: " + results)

      assert results.size() == 4 && results.last().handType == HandType.ROYAL_FLUSH
      log.info("================================")
    }
}