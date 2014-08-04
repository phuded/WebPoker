package poker.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import poker.domain.request.BetRequest
import poker.domain.game.Game
import poker.domain.game.bettinground.BettingRound
import poker.domain.game.round.Round
import poker.domain.player.GamePlayer
import poker.domain.player.betting.BettingAction
import poker.repository.GameRepository

/**
 * Created by matt on 21/05/2014.
 */
@Service
class BettingRoundService {

    static final Logger logger = LoggerFactory.getLogger(BettingRoundService.class)

    @Autowired
    GameRepository gameRepository


    /**
     * Set the next betting round
     * @param game
     * @return
     */
    BettingRound setNextBettingRound(Game game, Round round, BettingRound currentBettingRound){

        //Check if last
        if(currentBettingRound.last){
            return null
        }

        //Get the next
        BettingRound nextBettingRound = round.bettingRounds.get(currentBettingRound.bettingRoundNumber)

        //Set next as current
        nextBettingRound.isCurrent = true

        //Set the first player to current
        GamePlayer firstPlayer = game.nonFoldedPlayers.first()

        round.currentPlayerName = firstPlayer.name

        //Deal the cards
        nextBettingRound.dealCards(game,round)

        logger.info("Set next betting round - saving.")
        gameRepository.save(game)

        return nextBettingRound
    }

    /**
     * Perform bet
     * @param player
     * @param bettingRound
     * @param betRequest
     * @return
     */
    public makePlayerBet(GamePlayer player, BettingRound bettingRound, BetRequest betRequest){
        //Is a betting action
        if(betRequest.bettingAction) {

            BettingAction bettingAction = BettingAction.getBettingActionByName(betRequest.bettingAction)

            if (bettingAction == BettingAction.FOLD) {
                //Set to folded
                player.hasFolded = true
            }
            else if (bettingAction == BettingAction.CALL || bettingAction == BettingAction.CHECK) {
                //Player bets the difference
                player.makeBet(bettingRound.amountBetPerPlayer)
            }
        }
        else{
            //Add raise to current bet
            bettingRound.amountBetPerPlayer += betRequest.bet

            //Player bets difference
            player.makeBet(bettingRound.amountBetPerPlayer)

        }

        //Player has now bet once
        player.hasBetOnce = true

        logger.info(player.name + " bet: " + betRequest.bettingAction + "/" + betRequest.bet)
    }

    /**
     * Has the betting round finished?
     * @param game
     * @param bettingRound
     * @return
     */
    boolean hasBettingRoundFinished(Game game, BettingRound bettingRound){

        //In case after the current player folding - is only 1 player left
        if(game.nonFoldedPlayers.size() == 1){

            logger.debug("There is only 1 non-folded player")
            return true
        }

        //Are there any non-folded players who have yet to bet once?
        if(game.anyNonFoldedPlayerYetToBet){

            logger.debug("There is still a non-folded player who has not bet")

            //Betting not finished
            return false
        }


        // Do not keep betting
        boolean bettingRoundFinished = true

        game.nonFoldedPlayers.each{ GamePlayer player ->

            if(player.amountBet != bettingRound.amountBetPerPlayer){

                //Must continue betting
                bettingRoundFinished = false
            }
        }

        return bettingRoundFinished
    }

    /**
     * Complete the betting round
     * @param game
     * @param round
     * @param bettingRound
     * @return
     */
    void finishBettingRound(Game game, Round round, BettingRound bettingRound){
        //Get the pot
        int bettingRoundPot = bettingRound.getPot(game)

        //Add to the overall round pot
        round.pot += bettingRoundPot

        logger.info("Betting round pot: " + bettingRoundPot + ". Total round pot: " + round.pot)

        //Reset ALL players after betting round  - including amountBet!
        game.players*.resetBetweenBettingRounds()

        bettingRound.close()

        logger.info("Betting round finished - saving.")
        gameRepository.save(game)
    }

}
