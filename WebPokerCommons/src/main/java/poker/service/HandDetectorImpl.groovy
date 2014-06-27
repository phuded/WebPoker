package poker.service

import org.springframework.stereotype.Service
import poker.domain.card.Card
import poker.domain.card.CardValue
import poker.domain.card.Suit
import poker.domain.hand.HandType
import poker.domain.hand.Hand
import poker.domain.player.GamePlayer
import poker.util.PokerUtil

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 14/08/13
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 */

@Service
class HandDetectorImpl implements HandDetector{

    public detectHand(GamePlayer player){
        player.hands = detect(player.allCards)

        //Set best hand
        player.bestHand = player.hands.last()
    }

    public List<Hand> detect(List<Card> cards){
        //Sorting cards here
        PokerUtil.sortCards(cards)

        List<Hand> hands = []
        detectSameCard(cards,hands)

        //Look for straights
        detectStraight(cards,hands)
        //Look for low straights
        detectAceLowStraight(cards,hands)

        detectFlush(cards,hands)
        detectFullHouse(hands)
        detectStraightFlush(hands)

        //Get high cards
        if(hands.isEmpty()){
           hands << new Hand(HandType.HIGH_CARD,cards[-5..-1])
        }

        //Sort results
        PokerUtil.sortHandResults(hands)

        //Get secondary cards
        getSecondaryCards(hands.last(),cards)

        return hands
    }

    public void detectSameCard(List<Card> cards,List<Hand> hands){
        cards.each {
            CardValue currentCardValue = it.cardValue

            List<Card> foundCards = cards.findAll{
                it.cardValue == currentCardValue
            }

            switch (foundCards.size()){
                case 2: hands << new Hand(HandType.PAIR,foundCards)
                        break
                case 3: hands << new Hand(HandType.THREE_OF_A_KIND,foundCards)
                        break
                case 4: hands << new Hand(HandType.FOUR_OF_A_KIND,foundCards)
                        break
            }
        }

        //Get unique hands since above logic will create multiple entries for each unique hand
        hands = hands.unique()

        //Get all pairs
        List<Hand> pairResults = hands.findAll{
            it.handType == HandType.PAIR
        }

        //If more than 1 pair -> two pair
        if(pairResults.size() > 1){
            List<Card> twoPairCards = pairResults.collectMany{it.cards}
            //Limit to best 2 pair
            hands << new Hand(HandType.TWO_PAIR,twoPairCards[-4..-1])
        }
    }

    public void detectStraight(List <Card> cards, List<Hand> hands){
        boolean straight = false

        //Number of found cards - add first card to list
        //TODO: Handle null pointer
        def straightCards = [cards[0]]

        //First card value
        int cardValue = cards[0].cardValue.value

        for (int i = 1; i < cards.size(); i++){
            Card nextCard = cards[i]

            if(nextCard.cardValue.value == (cardValue + 1)){
                //Add next card to list of straight cards
                straightCards << nextCard

                //Straight found
                if(straightCards.size() > 4){
                    straight = true
                }
            }
            else{
                //If straight found and next card not included in straight - finish
                if(straight){
                    break
                }
                //Reset found cards
                straightCards = [nextCard]
            }

            //Get next card value
            cardValue = nextCard.cardValue.value
        }

       if(straight){
           //Add straight
           Hand straightHandResult =  new Hand(HandType.STRAIGHT,straightCards[-5..-1])
           //Set secondary cards as all straight cards - for use in straight flush detection
           straightHandResult.secondaryCards = straightCards
           hands << straightHandResult
       }
    }

    public void detectAceLowStraight(List<Card> cards, List<Hand> hands){
        if(!hands.find{it.handType == HandType.STRAIGHT}){
            //Reorder cards
            PokerUtil.convertAce(cards,true)
            //Look for straights
            detectStraight(cards,hands)
            //Reorder cards back
            PokerUtil.convertAce(cards,false)
        }
    }

    public void detectFlush(List <Card> cards,List<Hand> hands){

        for(Card card : cards){
            Suit currentCardSuit = card.suit

            List<Card> foundCards = cards.findAll{
                it.suit == currentCardSuit
            }

            if(foundCards.size() > 4){
                hands << new Hand(HandType.FLUSH, foundCards[-5..-1])
                break
            }
        }
    }

    public void detectFullHouse(List<Hand> hands){
        List <Hand> threes = hands.findAll{
            it.handType == HandType.THREE_OF_A_KIND
        }

        List <Hand> pairs = hands.findAll{
            it.handType == HandType.PAIR
        }

        Hand highestThree =  threes.isEmpty()?null:threes.last()
        Hand highestPair = null

        //If no pairs are present
        if(pairs.isEmpty()){
              //If there are more than one 3 of a kinds
              if(threes.size() > 1){
                  //Use first one (lower valued)
                  highestPair = threes.first()
              }
        }
        else{
            highestPair = pairs.last()
        }

        if(highestThree && highestPair){
            def fullHouseCards = []
            fullHouseCards.addAll(highestPair.cards[0..1])
            fullHouseCards.addAll(highestThree.cards)
            hands << new Hand(HandType.FULLHOUSE,fullHouseCards)
        }
    }

    public void detectStraightFlush(List<Hand> hands){
        //Check if have a straight and flush to begin with
        Hand flush = hands.find{it.handType == HandType.FLUSH}
        Hand straight = hands.find{it.handType == HandType.STRAIGHT}

        if(flush && straight){
            boolean isStraightFlush = false
            Suit flushSuit = flush.cards.last().suit
            List<Card> straightFlushCards = []

            //Loop through ALL straight cards (not just best 5)
            straight.secondaryCards.each{ Card card ->
                //If they have the flush suit - add
                if(card.suit == flushSuit){
                    straightFlushCards << card

                    //Have a straight flush
                    if(straightFlushCards.size() > 4){
                        isStraightFlush = true
                    }
                }
                else{
                    //If no straight flush -> reset
                    if(!isStraightFlush){
                        straightFlushCards = []
                    }

                }
            }

            if(isStraightFlush){
                //Get best 5 straight flush cards
                straightFlushCards = straightFlushCards[-5..-1]
                hands << new Hand(HandType.STRAIGHT_FLUSH,straightFlushCards)

                //Check for royal flush
                if(straightFlushCards.last().cardValue == CardValue.ACE){
                    hands << new Hand(HandType.ROYAL_FLUSH, straightFlushCards)
                }
            }
        }
    }

    public void getSecondaryCards(Hand bestHand, List<Card> cards){
        int cardsToFill = 5 - bestHand.cards.size()
        if(cardsToFill > 0){
            //Get best remaining cards
            List <Card> bestRemainingCards = cards.findAll{
                !bestHand.cards.contains(it)
            }
            //Add as secondary cards
            bestHand.secondaryCards.addAll(bestRemainingCards[-cardsToFill..-1])
        }
    }

}
