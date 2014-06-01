package poker.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import poker.domain.game.Game
import poker.domain.game.bettinground.BettingRound
import poker.domain.game.round.Round
import poker.domain.player.Player
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
        round.bettingRounds.first().dealCards(game,round)

        logger.info("Setting next betting round - saving.")
        gameRepository.save(game)

        return nextBettingRound
    }


    /**
     * Perform Bet
     * @param player
     * @param bettingRound
     * @return
     */
    public makePlayerBet(Player player, BettingRound bettingRound, String bet){
        //Must be first
        if(bettingRound.amountBetPerPlayer == 0){

            //Make bet and set new current bet
            player.makeBet(bet.toInteger())
            bettingRound.amountBetPerPlayer += bet.toInteger()

            logger.info(player.name + " bet: " + bet)
        }
        //Next player
        else{

            if(player.amountBet == bettingRound.amountBetPerPlayer){
                //Skip!!
            }
            else{
                //logger.debug(player.name + " - fold (f), call (c) or specify amount to raise (Current bet: " + bettingRound.amountBetPerPlayer + ")")

                if(bet == "f"){
                    player.hasFolded = true
                }
                else if(bet == "c"){
                    player.makeBet(bettingRound.amountBetPerPlayer)
                }
                else{
                    //Add raise to current bet
                    bettingRound.amountBetPerPlayer += bet.toInteger()

                    //Player bets difference
                    player.makeBet(bettingRound.amountBetPerPlayer)

                }

                logger.info(player.name + " bet: " + bet)
            }

        }
    }

    /**
     * Has the betting round finished?
     * @param game
     * @param bettingRound
     * @return
     */
    boolean hasBettingRoundFinished(Game game, BettingRound bettingRound){

        //Workaround to ensure evaluates to 'true' on first pass
        if(bettingRound.firstCycle){
            bettingRound.firstCycle = false
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
