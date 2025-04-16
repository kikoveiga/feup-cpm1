package com.acme.supermarket.server.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.apache.coyote.BadRequestException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException): ResponseEntity<Map<String, String>> {
        val response = mapOf(
            "timestamp" to System.currentTimeMillis().toString(),
            "status" to HttpStatus.BAD_REQUEST.value().toString(),
            "error" to "Bad Request",
            "message" to (ex.message ?: "Invalid request data"),
            "path" to "/api/register"
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

}
