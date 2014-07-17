package poker.domain.notification

import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * Created by matt on 06/06/2014.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
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
