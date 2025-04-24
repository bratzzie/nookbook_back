package com.nookbook.backend.web.controllers

import com.nookbook.backend.core.services.exceptions.EmailAlreadyTakenException
import com.nookbook.backend.core.services.exceptions.EmailFailedToSendException
import com.nookbook.backend.core.services.exceptions.IncorrectVerificationCodeException
import com.nookbook.backend.core.services.exceptions.UsernameAlreadyTakenException
import com.nookbook.backend.web.controllers.exceptions.InvalidCredentialsException
import com.nookbook.backend.web.controllers.exceptions.RequestBodyIsNotValidException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthenticationControllerExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    fun handleUsernameAlreadyTakenException(ex: UsernameAlreadyTakenException): ResponseEntity<String> =
        ResponseEntity<String>("The username you provided is already in use", HttpStatus.CONFLICT)

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    fun handleEmailAlreadyTakenException(ex: EmailAlreadyTakenException): ResponseEntity<String> =
        ResponseEntity<String>("The email you provided is already in use", HttpStatus.CONFLICT)

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    fun handleEmailFailedToSendException(ex: EmailFailedToSendException): ResponseEntity<String> =
        ResponseEntity<String>(
            "Verification code was failed to be sent, try again later.",
            HttpStatus.INTERNAL_SERVER_ERROR
        )

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    fun handleRequestBodyIsNotValidException(ex: RequestBodyIsNotValidException): ResponseEntity<String> =
        ResponseEntity<String>(
            ex.message,
            HttpStatus.BAD_REQUEST
        )

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    fun handleIncorrectVerificationCodeException(ex: IncorrectVerificationCodeException): ResponseEntity<String> =
        ResponseEntity<String>("The provided verification code is incorrect", HttpStatus.CONFLICT)

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    fun handleInvalidCredentialsException(ex: InvalidCredentialsException): ResponseEntity<String> =
        ResponseEntity<String>("Invalid login data", HttpStatus.FORBIDDEN)
}

