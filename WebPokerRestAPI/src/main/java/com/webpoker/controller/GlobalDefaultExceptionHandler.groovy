package com.webpoker.controller;

import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    /**
     * Generic handler
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public HttpServletRequest defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

        log.error("Caught exception handling request.", e)

        return req
    }

}