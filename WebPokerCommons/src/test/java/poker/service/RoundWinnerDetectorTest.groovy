package poker.service

import org.springframework.beans.factory.annotation.Autowired
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
class RoundWinnerDetectorTest extends GroovyTestCase {

    @Autowired
    HandDetector handDetector

    @Autowired
    RoundWinnerDetector roundWinnerDetector

    void testDifferentHighCards() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.FOUR,Suit.DIAMONDS)
        gameCards << new Card(CardValue.ACE,Suit.HEARTS)
        gameCards << new Card(CardValue.KING,Suit.DIAMONDS)
        gameCards << new Card(CardValue.QUEEN,Suit.DIAMONDS)

        Player player1 = new Player("Player 1")
        player1.receiveCard(new Card(CardValue.TEN,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.NINE,Suit.SPADES))

        player1.addGameCards(gameCards)

        Player player2 = new Player("Player 2")
        player2.receiveCard(new Card(CardValue.TEN,Suit.HEARTS))
        player2.receiveCard(new Card(CardValue.EIGHT,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<Player> players = [player1,player2]

        players.each{ Player player ->
            handDetector.detectHand(player)
            println "testDifferentHighCards: "+ player.name + " - Best hand: " + player.bestHand
        }

        List <Player> winners = roundWinnerDetector.detectWinners(players)
        println "testDifferentHighCards: Winners: " + winners

        assert winners.size() == 1 && winners.first().name == "Player 1"
        println "================================"
    }

    void testSamePairsWithSameKickers() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.ACE,Suit.HEARTS)
        gameCards << new Card(CardValue.KING,Suit.DIAMONDS)
        gameCards << new Card(CardValue.QUEEN,Suit.DIAMONDS)

        Player player1 = new Player("Player 1")
        player1.receiveCard(new Card(CardValue.FIVE,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.THREE,Suit.SPADES))

        player1.addGameCards(gameCards)

        Player player2 = new Player("Player 2")
        player2.receiveCard(new Card(CardValue.SIX,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.EIGHT,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<Player> players = [player1,player2]

        players.each{ Player player ->
            handDetector.detectHand(player)
            println "testSamePairsWithSameKickers: "+ player.name + " - Best hand: " + player.bestHand
        }

        List <Player> winners = roundWinnerDetector.detectWinners(players)
        println "testSamePairsWithSameKickers: Winners: " + winners

        assert winners.size() == 2
        println "================================"
    }

    void testSamePairsWithDifferentKickers() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.TEN,Suit.HEARTS)
        gameCards << new Card(CardValue.KING,Suit.DIAMONDS)
        gameCards << new Card(CardValue.QUEEN,Suit.DIAMONDS)

        Player player1 = new Player("Player 1")
        player1.receiveCard(new Card(CardValue.ACE,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.THREE,Suit.SPADES))

        player1.addGameCards(gameCards)

        Player player2 = new Player("Player 2")
        player2.receiveCard(new Card(CardValue.SIX,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.EIGHT,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<Player> players = [player1,player2]

        players.each{ Player player ->
            handDetector.detectHand(player)
            println "testSamePairsWithDifferentKickers: "+ player.name + " - Best hand: " + player.bestHand
        }

        List <Player> winners = roundWinnerDetector.detectWinners(players)
        println "testSamePairsWithDifferentKickers: Winners: " + winners

        assert winners.size() == 1 && winners.first().name == "Player 1"
        println "================================"
    }

    void testDifferentThreesInFullHouse() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.SIX,Suit.HEARTS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)
        gameCards << new Card(CardValue.FOUR,Suit.DIAMONDS)

        Player player1 = new Player("Player 1")
        player1.receiveCard(new Card(CardValue.FOUR,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.FOUR,Suit.SPADES))

        player1.addGameCards(gameCards)

        Player player2 = new Player("Player 2")
        player2.receiveCard(new Card(CardValue.SIX,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.SIX,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<Player> players = [player1,player2]

        players.each{ Player player ->
            handDetector.detectHand(player)
            println "testDifferentThreesInFullHouse: "+ player.name + " - Best hand: " + player.bestHand
        }

        List <Player> winners = roundWinnerDetector.detectWinners(players)
        println "testDifferentThreesInFullHouse: Winners: " + winners

        assert winners.size() == 1 && winners.first().name == "Player 2"
        println "================================"
    }

    void testDifferentThreesInFullHouse2() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)

        Player player1 = new Player("Player 1")
        player1.receiveCard(new Card(CardValue.FIVE,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.FOUR,Suit.SPADES))

        player1.addGameCards(gameCards)

        Player player2 = new Player("Player 2")
        player2.receiveCard(new Card(CardValue.SEVEN,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.SIX,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<Player> players = [player1,player2]

        players.each{ Player player ->
            handDetector.detectHand(player)
            println "testDifferentThreesInFullHouse2: "+ player.name + " - Best hand: " + player.bestHand
        }

        List <Player> winners = roundWinnerDetector.detectWinners(players)
        println "testDifferentThreesInFullHouse2: Winners: " + winners

        assert winners.size() == 1 && winners.first().name == "Player 1"
        println "================================"
    }

    void testDifferentPairsInFullHouse() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)
        gameCards << new Card(CardValue.SIX,Suit.DIAMONDS)

        Player player1 = new Player("Player 1")
        player1.receiveCard(new Card(CardValue.SIX,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.FOUR,Suit.SPADES))

        player1.addGameCards(gameCards)

        Player player2 = new Player("Player 2")
        player2.receiveCard(new Card(CardValue.FIVE,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.NINE,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<Player> players = [player1,player2]

        players.each{ Player player ->
            handDetector.detectHand(player)
            println "testDifferentPairsInFullHouse: "+ player.name + " - Best hand: " + player.bestHand
        }

        List <Player> winners = roundWinnerDetector.detectWinners(players)
        println "testDifferentPairsInFullHouse: Winners: " + winners

        assert winners.size() == 1 && winners.first().name == "Player 1"
        println "================================"
    }

    void testFourOfAKindFromFullHouse() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)

        Player player1 = new Player("Player 1")
        player1.receiveCard(new Card(CardValue.FIVE,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.FOUR,Suit.SPADES))

        player1.addGameCards(gameCards)

        Player player2 = new Player("Player 2")
        player2.receiveCard(new Card(CardValue.TWO,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.SIX,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<Player> players = [player1,player2]

        players.each{ Player player ->
            handDetector.detectHand(player)
            println "testFourOfAKindFromFullHouse: "+ player.name + " - Best hand: " + player.bestHand
        }

        List <Player> winners = roundWinnerDetector.detectWinners(players)
        println "testFourOfAKindFromFullHouse: Winners: " + winners

        assert winners.size() == 1 && winners.first().name == "Player 2" && winners.first().bestHand.handType == HandType.FOUR_OF_A_KIND
        println "================================"
    }
}
