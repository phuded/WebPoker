package poker.service

import poker.domain.player.Player

/**
 * Created by matt on 21/05/2014.
 */
interface RoundWinnerDetector {

    public List<Player> detectWinners(List<Player> players)
}
