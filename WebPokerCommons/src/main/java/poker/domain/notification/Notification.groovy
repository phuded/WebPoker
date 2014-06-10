package poker.domain.notification

/**
 * Created by matt on 06/06/2014.
 */
class Notification {

  String gameId
  String type

  String playerName
  String bettingAction
  Integer bet

  Integer roundNumber

  Notification(String gameId, String type){
      this.gameId = gameId
      this.type = type
  }
}
