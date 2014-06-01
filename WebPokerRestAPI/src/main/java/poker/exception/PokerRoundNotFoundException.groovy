package poker.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by matt on 30/05/2014.
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Not Found")
class PokerRoundNotFoundException extends RuntimeException{

    PokerRoundNotFoundException(String message){
        super(message)
    }
}