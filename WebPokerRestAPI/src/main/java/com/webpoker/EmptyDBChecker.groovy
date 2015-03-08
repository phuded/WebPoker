package com.webpoker

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component
import com.webpoker.domain.request.PlayerRequest
import com.webpoker.service.PlayerService;

@Component
public class EmptyDBChecker implements ApplicationListener<ContextRefreshedEvent> {

    static final Logger LOGGER = LoggerFactory.getLogger(EmptyDBChecker.class)

    @Autowired
    PlayerService playerService;

    /**
     * Add the matt user if he doesn't exist.
     * @param event
     */
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        if(playerService.allPlayers.empty){

            PlayerRequest request = new PlayerRequest();

            request.name = "matt"
            request.firstName = "Matthew"
            request.lastName = "Carter"
            request.password = "Admin1"
            request.role = "Administrator"

            playerService.createNewPlayer(request);

            LOGGER.debug "Player " + request.name + " created."

        }
        else{
            LOGGER.debug "Players already exist - not creating a default."
        }

    }
}