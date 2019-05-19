package ru.rmd.monolith.controller

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono

@Component
class ExceptionHandler : WebExceptionHandler {

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        /* Handle different exceptions here */
        when(ex) {
            is NoSuchElementException -> exchange.response.statusCode = HttpStatus.NOT_FOUND
            is Exception -> exchange.response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
        }

        /* Do common thing like logging etc...  */

        return Mono.empty()
    }

}