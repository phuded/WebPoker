package poker.service

//import groovy.util.logging.Log4j2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component
import poker.domain.request.PlayerRequest
import poker.exception.PokerNotFoundException;

//@Log4j2
@Component
public class StartupHousekeeper implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    PlayerService playerService;

    /**
     * Add the matt user if he doesn't exist.
     * @param event
     */
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        PlayerRequest request = new PlayerRequest();

        request.name = "matt"
        request.firstName = "Matthew"
        request.lastName = "Carter"
        request.password = "Admin1"
        request.role = "Administrator"

        try {
            playerService.loadPlayerByName(request.name)

           // log.debug "Player " + request.name + " already exists."
        }
        catch (PokerNotFoundException e){

            playerService.createNewPlayer(request);

            //log.debug "Player " + request.name + " created."
        }

    }
}