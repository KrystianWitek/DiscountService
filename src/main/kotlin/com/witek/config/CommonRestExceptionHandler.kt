package com.witek.config

import com.witek.ErrorResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice(annotations = [RestController::class])
class CommonRestExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNoSuchElementException(e: NoSuchElementException): ErrorResponse {
        val msg: String = e.message ?: "Value not exist"
        return ErrorResponse(msg)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ErrorResponse {
        val msg: String = e.bindingResult.fieldErrors
            .map { "${it.field}: ${it.defaultMessage}" }
            .sortedBy { it }
            .joinToString { it }
        return ErrorResponse(msg)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException): ErrorResponse {
        return ErrorResponse("Invalid path variable '${ex.name}': expected type ${ex.requiredType?.simpleName}")
    }

    @ExceptionHandler(IllegalStateException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleIllegalStateException(ex: IllegalStateException): ErrorResponse {
        log.error(ex.message, ex)
        return ErrorResponse("There was an server error. Please notify dev team immediately!")
    }

    private val log: Logger by lazy { LoggerFactory.getLogger(javaClass) }
}