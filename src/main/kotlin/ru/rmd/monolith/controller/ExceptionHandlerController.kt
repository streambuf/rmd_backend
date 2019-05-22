package ru.rmd.monolith.controller

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.rmd.monolith.exception.BadRequestException
import ru.rmd.monolith.exception.NotFoundException
import ru.rmd.monolith.exception.UserException

@RestControllerAdvice
class ExceptionHandlerController {

    private val logger = KotlinLogging.logger {}

    data class ErrorResponse(val statusCode: Int, val message: String)

    @ExceptionHandler(Throwable::class)
    fun onException(ex: Throwable): ResponseEntity<ErrorResponse> {

        var statusCode = 0
        val httpError = when (ex) {
            is UserException -> {
                logger.info { "UserException: ${ex.message}"  }
                statusCode = 1
                HttpStatus.BAD_REQUEST
            }
            is BadRequestException -> {
                logger.warn { "BadRequestException: ${ex.message}"  }
                HttpStatus.BAD_REQUEST
            }
            is NotFoundException -> {
                logger.info { "NotFoundException: ${ex.message}"  }
                HttpStatus.NOT_FOUND
            }
            else -> {
                logger.error("Unhandled error: ${ex.message}", ex)
                HttpStatus.INTERNAL_SERVER_ERROR
            }
        }

        val errorResponse = ErrorResponse(
                statusCode = statusCode,
                message = ex.message ?: "Неизвестная ошибка"
        )

        return ResponseEntity(errorResponse, httpError);
    }
}