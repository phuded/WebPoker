package poker.domain.notification

import poker.domain.player.betting.BettingAction

/**
 * Created by matt on 06/06/2014.
 */
class BetNotification{
  String playerName
  BettingAction bettingAction
  Integer bet
}
