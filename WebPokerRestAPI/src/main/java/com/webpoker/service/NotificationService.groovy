package com.webpoker.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import com.webpoker.domain.notification.Notification
import com.webpoker.domain.request.BetRequest

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
    void sendNotification(String gameId, BetRequest betRequest, String playerName){
        Notification notification = new Notification(gameId, "bet")

        notification.playerName = playerName
        notification.bettingAction = betRequest.bettingAction
        notification.bet = betRequest.bet

        this.template.convertAndSend("/topic/notifications", notification);
    }

    /**
     * Send new round notification
     */
    void sendNotification(String gameId, int roundNumber){
        Notification notification = new Notification(gameId, "round")

        notification.roundNumber = roundNumber

        this.template.convertAndSend("/topic/notifications", notification);
    }
}
