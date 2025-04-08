package com.nookbook.backend.web.controllers

import com.nookbook.backend.core.services.exceptions.UserDoesNotExistException
import com.nookbook.backend.core.services.exceptions.UserImageException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserControllerExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    fun handleUserDoesNotExistException(ex: UserDoesNotExistException): ResponseEntity<String> =
        ResponseEntity<String>("The user cannot be found", HttpStatus.NOT_FOUND)

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler
    fun handleUserImageException(ex: UserImageException): ResponseEntity<String> =
        ResponseEntity<String>(ex.message, HttpStatus.NOT_ACCEPTABLE)
}