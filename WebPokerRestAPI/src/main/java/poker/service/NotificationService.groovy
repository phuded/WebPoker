package poker.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import poker.domain.notification.BetNotification
import poker.domain.player.betting.BettingAction
import poker.domain.request.BetRequest

/**
 * Created by matt on 08/06/2014.
 */
@Service
class NotificationService {

    @Autowired
    SimpMessagingTemplate template

    /**
     * Send betting notification
     * @param betRequest
     */
    void sendNotification(BetRequest betRequest, String player){

        BetNotification betNotification = new BetNotification()
        betNotification.playerName = player

        betNotification.bettingAction = betRequest.bettingAction
        betNotification.bet = betRequest.bet

        this.template.convertAndSend("/topic/betNotifications", betNotification);
    }
}
