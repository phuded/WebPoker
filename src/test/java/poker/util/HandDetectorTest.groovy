package poker.util

import poker.main.domain.card.Card
import poker.main.domain.card.CardValue
import poker.main.domain.card.Suit
import poker.hand.HandType

class HandDetectorTest extends GroovyTestCase {

    void testPair() {

        def cards = []
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.FOUR,Suit.DIAMONDS)
        cards << new Card(CardValue.ACE,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.SPADES)
        cards << new Card(CardValue.FOUR,Suit.CLUBS)
        cards << new Card(CardValue.KING,Suit.DIAMONDS)
        cards << new Card(CardValue.QUEEN,Suit.DIAMONDS)

        println "testPair cards: " + cards

        def results = HandDetector.detect(cards)

        println "testPair results: " + results

        assert results.size() == 1 && results.first().handType == HandType.PAIR
        println "================================"
    }

    void testThreeOfAKind() {

        def cards = []
        cards << new Card(CardValue.ACE,Suit.HEARTS)
        cards << new Card(CardValue.FOUR,Suit.DIAMONDS)
        cards << new Card(CardValue.ACE,Suit.SPADES)
        cards << new Card(CardValue.THREE,Suit.SPADES)
        cards << new Card(CardValue.FIVE,Suit.CLUBS)
        cards << new Card(CardValue.ACE,Suit.DIAMONDS)
        cards << new Card(CardValue.QUEEN,Suit.DIAMONDS)

        println "testThreeOfAKind cards: " + cards

        def results = HandDetector.detect(cards)

        println "testThreeOfAKind results: " + results

        assert results.size() == 1 && results.first().handType == HandType.THREE_OF_A_KIND
        println "================================"
    }

    void testTwoPair() {

        def cards = []
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.DIAMONDS)
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.SPADES)
        cards << new Card(CardValue.ACE,Suit.CLUBS)

        println "testTwoPair cards: " + cards

        def results = HandDetector.detect(cards)

        println "testTwoPair results: " + results

        assert results.size() == 3 && results.findAll{it.handType == HandType.TWO_PAIR}.size()==1
        println "================================"
    }

    void testBestTwoPair() {

        def cards = []
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.DIAMONDS)
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.SPADES)
        cards << new Card(CardValue.ACE,Suit.CLUBS)
        cards << new Card(CardValue.ACE,Suit.HEARTS)

        println "testBestTwoPair cards: " + cards

        def results = HandDetector.detect(cards)

        println "testBestTwoPair results: " + results

        assert results.size() == 4 && results.findAll{it.handType == HandType.TWO_PAIR}.size()==1
        println "================================"
    }

    void testStraight() {

        def cards = []
        cards << new Card(CardValue.TEN,Suit.HEARTS)
        cards << new Card(CardValue.JACK,Suit.CLUBS)
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.QUEEN,Suit.HEARTS)
        cards << new Card(CardValue.KING,Suit.DIAMONDS)
        cards << new Card(CardValue.ACE,Suit.DIAMONDS)
        cards << new Card(CardValue.NINE,Suit.HEARTS)

        println "testStraight cards: " + cards

        def results = HandDetector.detect(cards)

        println "testStraight results: " + results

        assert results.size() == 1 && results.first().handType == HandType.STRAIGHT
        println "================================"
    }

    void testLowStraight() {

        def cards = []
        cards << new Card(CardValue.ACE,Suit.HEARTS)
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.KING,Suit.CLUBS)
        cards << new Card(CardValue.THREE,Suit.HEARTS)
        cards << new Card(CardValue.FOUR,Suit.HEARTS)
        cards << new Card(CardValue.FIVE,Suit.DIAMONDS)
        cards << new Card(CardValue.SEVEN,Suit.DIAMONDS)

        println "testLowStraight cards: " + cards

        def results = HandDetector.detect(cards)

        println "testLowStraight results: " + results

        assert results.size() == 1 && results.first().handType == HandType.STRAIGHT
        println "================================"
    }

    void testFlush() {

        def cards = []
        cards << new Card(CardValue.EIGHT,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.SPADES)
        cards << new Card(CardValue.JACK,Suit.HEARTS)
        cards << new Card(CardValue.QUEEN,Suit.HEARTS)
        cards << new Card(CardValue.KING,Suit.HEARTS)
        cards << new Card(CardValue.TWO,Suit.DIAMONDS)
        cards << new Card(CardValue.ACE,Suit.HEARTS)

        println "testFlush cards: " + cards

        def results = HandDetector.detect(cards)

        println "testFlush results: " + results

        assert results.size() == 1
        println "================================"
    }

    void testFourOfAKind() {

        def cards = []
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.TWO,Suit.DIAMONDS)
        cards << new Card(CardValue.TWO,Suit.SPADES)
        cards << new Card(CardValue.TWO,Suit.CLUBS)
        cards << new Card(CardValue.FOUR,Suit.SPADES)
        cards << new Card(CardValue.KING,Suit.CLUBS)
        cards << new Card(CardValue.ACE,Suit.HEARTS)

        println "testFourOfAKind cards: " + cards

        def results = HandDetector.detect(cards)

        println "testFourOfAKind results: " + results

        assert results.size() == 1 && results.first().handType == HandType.FOUR_OF_A_KIND
        println "================================"
    }

    void testFullHouse() {

        def cards = []
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.TWO,Suit.DIAMONDS)
        cards << new Card(CardValue.TWO,Suit.SPADES)
        cards << new Card(CardValue.THREE,Suit.CLUBS)
        cards << new Card(CardValue.FOUR,Suit.SPADES)
        cards << new Card(CardValue.FOUR,Suit.CLUBS)
        cards << new Card(CardValue.FOUR,Suit.HEARTS)

        println "testFullHouse cards: " + cards

        def results = HandDetector.detect(cards)

        println "testFullHouse results: " + results

        assert results.size() == 3 && results.findAll{it.handType == HandType.FULLHOUSE}.size()==1
        println "================================"
    }

    void testStraightFlush() {

        def cards = []
        cards << new Card(CardValue.THREE,Suit.DIAMONDS)
        cards << new Card(CardValue.FOUR,Suit.SPADES)
        cards << new Card(CardValue.FIVE,Suit.HEARTS)
        cards << new Card(CardValue.SIX,Suit.HEARTS)
        cards << new Card(CardValue.SEVEN,Suit.HEARTS)
        cards << new Card(CardValue.EIGHT,Suit.HEARTS)
        cards << new Card(CardValue.NINE,Suit.HEARTS)

        println "testStraightFlush cards: " + cards

        def results = HandDetector.detect(cards)

        println "testStraightFlush results: " + results

        assert results.size() == 3 && results.last().handType == HandType.STRAIGHT_FLUSH
        println "================================"
    }

    void testMismatchedStraightFlush() {

        def cards = []
        cards << new Card(CardValue.THREE,Suit.DIAMONDS)
        cards << new Card(CardValue.FOUR,Suit.HEARTS)
        cards << new Card(CardValue.FIVE,Suit.HEARTS)
        cards << new Card(CardValue.SIX,Suit.HEARTS)
        cards << new Card(CardValue.SEVEN,Suit.HEARTS)
        cards << new Card(CardValue.EIGHT,Suit.HEARTS)
        cards << new Card(CardValue.TEN,Suit.HEARTS)

        println "testMismatchedStraightFlush cards: " + cards

        def results = HandDetector.detect(cards)

        println "testMismatchedStraightFlush results: " + results

        assert results.size() == 3 && results.last().handType == HandType.STRAIGHT_FLUSH
        println "================================"
    }

    void testMismatchedLowStraightFlush() {

        def cards = []
        cards << new Card(CardValue.ACE,Suit.HEARTS)
        cards << new Card(CardValue.TWO,Suit.HEARTS)
        cards << new Card(CardValue.THREE,Suit.HEARTS)
        cards << new Card(CardValue.FOUR,Suit.HEARTS)
        cards << new Card(CardValue.FIVE,Suit.HEARTS)
        cards << new Card(CardValue.SEVEN,Suit.HEARTS)
        cards << new Card(CardValue.TEN,Suit.HEARTS)

        println "testMismatchedLowStraightFlush cards: " + cards

        def results = HandDetector.detect(cards)

        println "testMismatchedLowStraightFlush results: " + results

        assert results.size() == 3 && results.last().handType == HandType.STRAIGHT_FLUSH
        println "================================"
    }

    void testRoyalFlush() {

      def cards = []
      cards << new Card(CardValue.TWO,Suit.HEARTS)
      cards << new Card(CardValue.THREE,Suit.HEARTS)
      cards << new Card(CardValue.TEN,Suit.HEARTS)
      cards << new Card(CardValue.JACK,Suit.HEARTS)
      cards << new Card(CardValue.QUEEN,Suit.HEARTS)
      cards << new Card(CardValue.KING,Suit.HEARTS)
      cards << new Card(CardValue.ACE,Suit.HEARTS)

      println "testRoyalFlush cards: " + cards

      def results = HandDetector.detect(cards)

      println "testRoyalFlush results: " + results

      assert results.size() == 4 && results.last().handType == HandType.ROYAL_FLUSH
      println "================================"
    }
}