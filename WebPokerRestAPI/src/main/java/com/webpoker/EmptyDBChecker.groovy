package com.webpoker

import com.webpoker.domain.player.PlayerRole
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component
import com.webpoker.domain.request.PlayerRequest
import com.webpoker.service.PlayerService;

@Slf4j
@Component
public class EmptyDBChecker implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    PlayerService playerService;

    /**
     * Add the default users if none exist.
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
            request.role = PlayerRole.ROLE_ADMINISTRATOR

            playerService.createNewPlayer(request);

            log.info "Player " + request.name + " created."


            request.name = "cathy"
            request.firstName = "Catherine"
            request.lastName = "McGrath"
            request.password = "Admin1"
            request.role = PlayerRole.ROLE_ADMINISTRATOR

            playerService.createNewPlayer(request);

            log.info "Player " + request.name + " created."

        }
        else{
            log.info "Players already exist - not creating a default."
        }

    }
}