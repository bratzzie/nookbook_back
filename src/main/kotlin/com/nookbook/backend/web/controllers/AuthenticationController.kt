package com.nookbook.backend.web.controllers

import com.nookbook.backend.core.converters.UserDTOToUserEntityConverter
import com.nookbook.backend.core.services.UserService
import com.nookbook.backend.web.controllers.exceptions.RequestBodyIsNotValidException
import com.nookbook.backend.web.models.UserDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val userService: UserService,
    private val userConverter: UserDTOToUserEntityConverter
) {

    @PostMapping("/reg")
    fun registerUser(@RequestBody user: UserDTO): UserDTO {
        return userConverter.toUserDTO(userService.createUser(userConverter.toUserEntity(user)))
    }

    @PostMapping("/email/code")
    fun createEmailVerificationCode(@RequestBody body: LinkedHashMap<String, String>): ResponseEntity<String> {
        body["username"]?.let {
            userService.createEmailVerification(it)
            return ResponseEntity<String>("Verification code generated, email was sent", HttpStatus.OK)
        }

        throw RequestBodyIsNotValidException("Username")
    }

    @PostMapping("/email/verify")
    fun verifyEmail(@RequestBody body: LinkedHashMap<String, String>): UserDTO {
        val code = body["code"]?.toLong() ?: throw RequestBodyIsNotValidException("Verification code")
        val username = body["username"] ?: throw RequestBodyIsNotValidException("Username")

        return userConverter.toUserDTO(userService.verifyEmail(username, code))
    }

    @PutMapping("/update/password")
    fun updatePassword(@RequestBody body: LinkedHashMap<String, String>): UserDTO {
        val password = body["password"] ?: throw RequestBodyIsNotValidException("Password")
        val username = body["username"] ?: throw RequestBodyIsNotValidException("Username")

        return userConverter.toUserDTO(userService.updatePassword(username, password))

    }
}