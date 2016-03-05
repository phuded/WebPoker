package com.webpoker.service

import com.webpoker.domain.card.Card
import com.webpoker.domain.card.CardValue
import com.webpoker.domain.card.Suit
import com.webpoker.domain.hand.HandType
import com.webpoker.domain.player.GamePlayer
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
/**
 * Created with IntelliJ IDEA.
 * User: matt
 * Date: 25/08/2013
 * Time: 12:39
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = [HandDetectorServiceImpl.class, RoundWinnerDetectorServiceImpl.class])
class RoundWinnerDetectorTest extends GroovyTestCase {

    @Autowired
    HandDetectorService handDetectorService

    @Autowired
    RoundWinnerDetectorService roundWinnerDetectorService

    @Test
    void testDifferentHighCards() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.FOUR,Suit.DIAMONDS)
        gameCards << new Card(CardValue.ACE,Suit.HEARTS)
        gameCards << new Card(CardValue.KING,Suit.DIAMONDS)
        gameCards << new Card(CardValue.QUEEN,Suit.DIAMONDS)

        GamePlayer player1 = new GamePlayer("Player 1",1,null)
        player1.receiveCard(new Card(CardValue.TEN,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.NINE,Suit.SPADES))

        player1.addGameCards(gameCards)

        GamePlayer player2 = new GamePlayer("Player 2",2,null)
        player2.receiveCard(new Card(CardValue.TEN,Suit.HEARTS))
        player2.receiveCard(new Card(CardValue.EIGHT,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<GamePlayer> players = [player1,player2]

        players.each{ GamePlayer player ->
            handDetectorService.detectHand(player)
            log.info("testDifferentHighCards: "+ player.name + " - Best hand: " + player.bestHand)
        }

        List <GamePlayer> winners = roundWinnerDetectorService.detectWinners(players)
        log.info("testDifferentHighCards: Winners: " + winners)

        assert winners.size() == 1 && winners.first().name == "Player 1"
        log.info("================================")
    }

    @Test
    void testSamePairsWithSameKickers() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.ACE,Suit.HEARTS)
        gameCards << new Card(CardValue.KING,Suit.DIAMONDS)
        gameCards << new Card(CardValue.QUEEN,Suit.DIAMONDS)

        GamePlayer player1 = new GamePlayer("Player 1",1,null)
        player1.receiveCard(new Card(CardValue.FIVE,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.THREE,Suit.SPADES))

        player1.addGameCards(gameCards)

        GamePlayer player2 = new GamePlayer("Player 2",2,null)
        player2.receiveCard(new Card(CardValue.SIX,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.EIGHT,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<GamePlayer> players = [player1,player2]

        players.each{ GamePlayer player ->
            handDetectorService.detectHand(player)
            log.info("testSamePairsWithSameKickers: "+ player.name + " - Best hand: " + player.bestHand)
        }

        List <GamePlayer> winners = roundWinnerDetectorService.detectWinners(players)
        log.info("testSamePairsWithSameKickers: Winners: " + winners)

        assert winners.size() == 2
        log.info("================================")
    }

    @Test
    void testSamePairsWithDifferentKickers() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.TEN,Suit.HEARTS)
        gameCards << new Card(CardValue.KING,Suit.DIAMONDS)
        gameCards << new Card(CardValue.QUEEN,Suit.DIAMONDS)

        GamePlayer player1 = new GamePlayer("Player 1",1,null)
        player1.receiveCard(new Card(CardValue.ACE,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.THREE,Suit.SPADES))

        player1.addGameCards(gameCards)

        GamePlayer player2 = new GamePlayer("Player 2",2,null)
        player2.receiveCard(new Card(CardValue.SIX,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.EIGHT,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<GamePlayer> players = [player1,player2]

        players.each{ GamePlayer player ->
            handDetectorService.detectHand(player)
            log.info("testSamePairsWithDifferentKickers: "+ player.name + " - Best hand: " + player.bestHand)
        }

        List <GamePlayer> winners = roundWinnerDetectorService.detectWinners(players)
        log.info("testSamePairsWithDifferentKickers: Winners: " + winners)

        assert winners.size() == 1 && winners.first().name == "Player 1"
        log.info("================================")
    }

    @Test
    void testDifferentThreesInFullHouse() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.SIX,Suit.HEARTS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)
        gameCards << new Card(CardValue.FOUR,Suit.DIAMONDS)

        GamePlayer player1 = new GamePlayer("Player 1",1,null)
        player1.receiveCard(new Card(CardValue.FOUR,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.FOUR,Suit.SPADES))

        player1.addGameCards(gameCards)

        GamePlayer player2 = new GamePlayer("Player 2",2,null)
        player2.receiveCard(new Card(CardValue.SIX,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.SIX,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<GamePlayer> players = [player1,player2]

        players.each{ GamePlayer player ->
            handDetectorService.detectHand(player)
            log.info("testDifferentThreesInFullHouse: "+ player.name + " - Best hand: " + player.bestHand)
        }

        List <GamePlayer> winners = roundWinnerDetectorService.detectWinners(players)
        log.info("testDifferentThreesInFullHouse: Winners: " + winners)

        assert winners.size() == 1 && winners.first().name == "Player 2"
        log.info("================================")
    }

    @Test
    void testDifferentThreesInFullHouse2() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)

        GamePlayer player1 = new GamePlayer("Player 1",1,null)
        player1.receiveCard(new Card(CardValue.FIVE,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.FOUR,Suit.SPADES))

        player1.addGameCards(gameCards)

        GamePlayer player2 = new GamePlayer("Player 2",2,null)
        player2.receiveCard(new Card(CardValue.SEVEN,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.SIX,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<GamePlayer> players = [player1,player2]

        players.each{ GamePlayer player ->
            handDetectorService.detectHand(player)
            log.info("testDifferentThreesInFullHouse2: "+ player.name + " - Best hand: " + player.bestHand)
        }

        List <GamePlayer> winners = roundWinnerDetectorService.detectWinners(players)
        log.info("testDifferentThreesInFullHouse2: Winners: " + winners)

        assert winners.size() == 1 && winners.first().name == "Player 1"
        log.info("================================")
    }

    @Test
    void testDifferentPairsInFullHouse() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)
        gameCards << new Card(CardValue.SIX,Suit.DIAMONDS)

        GamePlayer player1 = new GamePlayer("Player 1",1,null)
        player1.receiveCard(new Card(CardValue.SIX,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.FOUR,Suit.SPADES))

        player1.addGameCards(gameCards)

        GamePlayer player2 = new GamePlayer("Player 2",2,null)
        player2.receiveCard(new Card(CardValue.FIVE,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.NINE,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<GamePlayer> players = [player1,player2]

        players.each{ GamePlayer player ->
            handDetectorService.detectHand(player)
            log.info("testDifferentPairsInFullHouse: "+ player.name + " - Best hand: " + player.bestHand)
        }

        List <GamePlayer> winners = roundWinnerDetectorService.detectWinners(players)
        log.info("testDifferentPairsInFullHouse: Winners: " + winners)

        assert winners.size() == 1 && winners.first().name == "Player 1"
        log.info("================================")
    }

    @Test
    void testFourOfAKindFromFullHouse() {

        List<Card> gameCards = []
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.TWO,Suit.DIAMONDS)
        gameCards << new Card(CardValue.TWO,Suit.HEARTS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)
        gameCards << new Card(CardValue.FIVE,Suit.DIAMONDS)

        GamePlayer player1 = new GamePlayer("Player 1",1,null)
        player1.receiveCard(new Card(CardValue.FIVE,Suit.CLUBS))
        player1.receiveCard(new Card(CardValue.FOUR,Suit.SPADES))

        player1.addGameCards(gameCards)

        GamePlayer player2 = new GamePlayer("Player 2",2,null)
        player2.receiveCard(new Card(CardValue.TWO,Suit.CLUBS))
        player2.receiveCard(new Card(CardValue.SIX,Suit.SPADES))

        player2.addGameCards(gameCards)

        List<GamePlayer> players = [player1,player2]

        players.each{ GamePlayer player ->
            handDetectorService.detectHand(player)
            log.info("testFourOfAKindFromFullHouse: "+ player.name + " - Best hand: " + player.bestHand)
        }

        List <GamePlayer> winners = roundWinnerDetectorService.detectWinners(players)
        log.info("testFourOfAKindFromFullHouse: Winners: " + winners)

        assert winners.size() == 1 && winners.first().name == "Player 2" && winners.first().bestHand.handType == HandType.FOUR_OF_A_KIND
        log.info("================================")
    }
}
