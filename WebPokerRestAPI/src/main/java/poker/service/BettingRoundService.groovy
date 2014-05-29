package poker.service

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

    @Autowired
    GameRepository gameRepository

    /**
     * Create the Betting round - Start,Flop, River etc
     * @param game
     * @param round
     * @param bettingRound
     * @return
     */
    boolean executeBettingRound(Game game, Round round, BettingRound bettingRound){
        //Deal cards
        bettingRound.dealCards(game,round)

        //Bet!
        playBettingRound(bettingRound,round)

        //Finish round - and check if only 1 player remains
        return hasParentRoundCompleted(game,round,bettingRound)
    }

    /**
     * Bet in betting round
     * @param bettingRound
     * @param parentRound
     */
    def playBettingRound(BettingRound bettingRound, Round parentRound){

        //Do this while all the active players are not in-line or folded
        while(playersStillBetting(bettingRound, parentRound.roundPlayers)){

            //Run a cycle over every player
            executeBettingCycle(parentRound,bettingRound)

        }
    }

    /**
     * Run a betting cycle
     * @param parentRound
     * @param bettingRound
     * @return
     */
    public executeBettingCycle(Round parentRound, BettingRound bettingRound){
        parentRound.roundPlayers.each{ Player player ->

            //Player makes bet
            executePlayerBet(player,bettingRound)

        }
    }

    /**
     * One player bets
     * @param player
     * @param bettingRound
     * @return
     */
    public executePlayerBet(Player player, BettingRound bettingRound){
        //Must be first
        if(bettingRound.amountBetPerPlayer == 0){
            print player.name + " - Place Bet: "

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in))

            int betAmount = br.readLine().toInteger()

            //Make bet and set new current bet
            player.makeBet(betAmount.toInteger())
            bettingRound.amountBetPerPlayer += betAmount.toInteger()
        }
        //Next player
        else{

            if(player.amountBet == bettingRound.amountBetPerPlayer){
                //Skip!!
            }
            else{
                //Give player options..
                print player.name + " - fold (f), call (c) or specify amount to raise (Current bet: " + bettingRound.amountBetPerPlayer + "):"

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in))

                def betAmount = br.readLine()

                if(betAmount == "f"){
                    player.hasFolded = true
                }
                else if(betAmount == "c"){
                    player.makeBet(bettingRound.amountBetPerPlayer)
                }
                else{
                    //Add raise to current bet
                    bettingRound.amountBetPerPlayer += betAmount.toInteger()

                    //Player bets difference
                    player.makeBet(bettingRound.amountBetPerPlayer)

                }
            }

        }
    }



    /**
     * Check if all players match the current bet or have folded
     * @param bettingRound
     * @param players
     * @return
     */
    private boolean playersStillBetting(BettingRound bettingRound, List<Player> players){

        //Workaround to ensure evaluates to 'true' on first pass
        if(bettingRound.firstCycle){
            bettingRound.firstCycle = false
            return true
        }


        //TODO: REMOVES PLAYER FROM ROUND -> LOOK AT
        //Remove folded players
        players.removeAll{it.hasFolded}

        // Do not keep betting
        boolean continueBetting = false
        players.each{ Player player ->

            if(player.amountBet != bettingRound.amountBetPerPlayer){

                //Must continue betting
                continueBetting = true
            }
        }

        return continueBetting
    }

    /**
     * Complete the betting round and check the overall round has completed
     * @param game
     * @param round
     * @param bettingRound
     * @return
     */
    boolean hasParentRoundCompleted(Game game, Round round, BettingRound bettingRound){
        //Get the pot
        int bettingRoundPot = bettingRound.getPot(game)

        //Add to the overall round pot
        round.pot += bettingRoundPot

        println "Betting round pot: " + bettingRoundPot + ". Total round pot: " + round.pot

        //Reset ALL players after betting round  - including amountBet!
        game.players*.resetBetweenBettingRounds()

        println "Saving..."
        gameRepository.save(game)

        //Check if > 1 player left
        return (round.roundPlayers.size()>1)?false:true
    }
}
