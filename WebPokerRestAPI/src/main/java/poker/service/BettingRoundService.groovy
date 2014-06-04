package poker.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import poker.domain.BetRequest
import poker.domain.game.Game
import poker.domain.game.bettinground.BettingRound
import poker.domain.game.round.Round
import poker.domain.player.Player
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
     * Get the current betting round
     * @param round
     * @return
     */
    BettingRound getCurrentBettingRound(Round round){
      BettingRound currentBettingRound = round.bettingRounds.find { BettingRound bettingRound ->
          bettingRound.isCurrent
      }

      return currentBettingRound
    }

    /**
     * Get the current player
     * @param game
     * @return
     */
    Player getCurrentPlayer(Game game){
        Player activePlayer =game.players.find {Player player ->
            player.isCurrent
        }

        return activePlayer
    }

    /**
     * Set the next player
     * @param game
     * @return
     */
    void setNextPlayer(Game game, Round round){
        //Get the player
        Player player = getCurrentPlayer(game)

        //Set inactive
        player.isCurrent = false

        //Get the index
        int playerInx = game.getNonFoldedPlayers().indexOf(player)

        //Add one
        playerInx++

        if(playerInx == game.getNonFoldedPlayers().size()) {
            //Reset
            player = game.getNonFoldedPlayers().first()
        }
        else {
            //Get the next
            player = game.getNonFoldedPlayers().get(playerInx)
        }

        //Set active
        player.isCurrent = true
        round.currentPlayer = player.name

        logger.info("Setting next Player to: " + player.name + " - saving.")
        gameRepository.save(game)

    }

    /**
     * Set the next betting round
     * @param game
     * @return
     */
    BettingRound setNextBettingRound(Game game,Round round, BettingRound currentBettingRound){

        if(currentBettingRound.getBettingRoundNumber() == 4){
            return null
        }

        //Get the next
        BettingRound nextBettingRound = round.bettingRounds.get(currentBettingRound.getBettingRoundNumber())

        //Set next as current
        nextBettingRound.isCurrent = true

        //Set the first player to current
        Player firstPlayer = game.getNonFoldedPlayers().first()
        firstPlayer.isCurrent = true
        round.currentPlayer = firstPlayer.name

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
    public makePlayerBet(Player player, BettingRound bettingRound, BetRequest betRequest){
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

        logger.info(player.name + " bet: " + betRequest.bettingAction + "/" + betRequest.bet)
    }

    /**
     * Has the betting round finished?
     * @param game
     * @param bettingRound
     * @return
     */
    boolean hasBettingRoundFinished(Game game, BettingRound bettingRound, Player currentPlayer){

        //Workaround to ensure evaluates to 'true' on first pass
        if(bettingRound.firstCycle){

            //Get the index of the next player
            int playerInx = game.getNonFoldedPlayers().indexOf(currentPlayer) + 2

            //Is the next player the last player?
            if(playerInx == game.getNonFoldedPlayers().size()) {
                bettingRound.firstCycle = false
            }

            //Betting not finished
            return false
        }


        // Do not keep betting
        boolean bettingRoundFinished = true

        game.getNonFoldedPlayers().each{ Player player ->

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

        bettingRound.isCurrent = false

        bettingRound.hasFinished = true

        logger.info("Betting round finished - saving.")
        gameRepository.save(game)
    }

}
