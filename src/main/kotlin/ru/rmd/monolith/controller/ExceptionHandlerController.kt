package ru.rmd.monolith.controller

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebInputException
import ru.rmd.monolith.exception.NotFoundException

@RestControllerAdvice
class ExceptionHandlerController {

    private val logger = KotlinLogging.logger {}

    data class ErrorResponse(val statusCode: Int, val message: String)

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun onNotFoundException(ex: NotFoundException): ResponseEntity<ErrorResponse> {
        logger.info { "NotFoundException: ${ex.message}"  }
        val errorResponse = ErrorResponse(
                statusCode = 0,
                message = ex.message ?: "Unknown error"
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [ServerWebInputException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun onBadRequestException(ex: ServerWebInputException): ResponseEntity<ErrorResponse> {
        logger.warn { "Exception: ${ex.message}"  }
        val errorResponse = ErrorResponse(
                statusCode = 0,
                message = ex.message
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [Throwable::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun illegalArgumentsException(ex: Throwable): ResponseEntity<ErrorResponse> {
        logger.warn { "Exception: ${ex.message}"  }
        val errorResponse = ErrorResponse(
                statusCode = 0,
                message = ex.message ?: "Unknown error"
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

//    @ExceptionHandler(Throwable::class)
//    @ResponseBody
//    fun onException(ex: Throwable): ResponseEntity<ErrorResponse> {
//
//        var statusCode = 0
//        val httpError = when (ex) {
//            is UserException -> {
//                logger.info { "UserException: ${ex.message}"  }
//                statusCode = 1
//                HttpStatus.BAD_REQUEST
//            }
//            is BadRequestException -> {
//                logger.warn { "BadRequestException: ${ex.message}"  }
//                HttpStatus.BAD_REQUEST
//            }
//            is ChangeSetPersister.NotFoundException -> {
//                logger.info { "NotFoundException: ${ex.message}"  }
//                HttpStatus.NOT_FOUND
//            }
//            else -> {
//                logger.error("Unhandled error: ${ex.message}", ex)
//                HttpStatus.INTERNAL_SERVER_ERROR
//            }
//        }
//
//        val errorResponse = ErrorResponse(
//                statusCode = statusCode,
//                statusMessage = httpError.reasonPhrase,
//                message = ex.message ?: "Unknown error"
//        )
//
//        return ResponseEntity(errorResponse, httpError);
//    }
}