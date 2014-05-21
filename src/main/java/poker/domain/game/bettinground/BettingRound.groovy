package poker.domain.game.bettinground

import org.springframework.data.annotation.Id
import poker.domain.game.Game
import poker.domain.game.round.Round
import poker.domain.player.Player

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 26/08/13
 * Time: 21:21
 * To change this template use File | Settings | File Templates.
 */
abstract class BettingRound {

    @Id
    String id;

    //Current bet in betting round
    int currentBet
    boolean firstCycle

    BettingRound(Round round){
        currentBet = 0
        firstCycle = true
    }

    abstract dealCards(Game game, Round parentRound)

    //TODO: Move out of this class
    def beginBetting(Round parentRound){

        //Do this while all the active players are not checked
        while(checkAllBetsComplete(parentRound.roundPlayers)){

            parentRound.roundPlayers.each{ Player player ->

                //Must be first
                if(currentBet == 0){
                    print player.name + " - Place Bet: "
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
                    int betAmount = br.readLine().toInteger()

                    //Make bet and set new current bet
                    player.makeBet(betAmount.toInteger())
                    currentBet += betAmount.toInteger()
                }
                //Next player
                else{

                    if(player.amountBet == currentBet){
                        //Nothing
                    }
                    else{
                        //Give player options..
                        print player.name + " - fold (f), call (c) or specify amount to raise (Current bet: " + currentBet + "):"
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
                        def betAmount = br.readLine()

                        if(betAmount == "f"){
                            player.hasFolded = true
                        }
                        else if(betAmount == "c"){
                            player.makeBet(currentBet)
                        }
                        else{
                            //Add raise to current bet
                            currentBet += betAmount.toInteger()
                            //Player bets difference
                            player.makeBet(currentBet)

                        }
                    }

                }

            }

        }
    }

    //Check if all players match the current bet or have folded
    def checkAllBetsComplete(List<Player> players){

        //Workaround to ensure evaluates to 'true' on first pass
        if(firstCycle){
            firstCycle = false
            return true
        }

        //Remove folded players
        players.removeAll{it.hasFolded}

        // Do not keep betting
        boolean continueBetting = false
        players.each{ Player player ->

            if(player.amountBet != currentBet){
                //Must continue betting
                continueBetting = true
            }
        }

        return continueBetting
    }


    // Get the total pot from the betting round - include folded players (use game players)
    def getPot(Game game){
        int roundPot = 0
        //All players (current and folded)
        game.players.each{Player player ->
            roundPot += player.amountBet
        }
        return roundPot
    }
}
