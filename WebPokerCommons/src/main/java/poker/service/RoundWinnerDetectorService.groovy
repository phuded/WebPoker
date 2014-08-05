package poker.service

import poker.domain.player.GamePlayer

/**
 * Created by matt on 21/05/2014.
 */
interface RoundWinnerDetectorService {

    public List<GamePlayer> detectWinners(List<GamePlayer> players)
}