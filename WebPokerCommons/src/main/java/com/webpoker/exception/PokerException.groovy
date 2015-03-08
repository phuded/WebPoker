package com.webpoker.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by matt on 30/05/2014.
 */
@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Error")
class PokerException extends RuntimeException{

    PokerException(String message){
        super(message)
    }
}
