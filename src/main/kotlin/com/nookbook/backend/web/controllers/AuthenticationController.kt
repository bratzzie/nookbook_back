package com.nookbook.backend.web.controllers

import com.nookbook.backend.core.services.UserService
import com.nookbook.backend.persistence.models.UserEntity
import com.nookbook.backend.web.models.RegistrationDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthenticationController(private val userService: UserService) {

    @PostMapping("/reg")
    fun registerUser(@RequestBody user: RegistrationDTO): UserEntity {
        return userService.createUser(user)
    }

    @PostMapping("/email/code")
    fun createEmailVerificationCode(@RequestBody body: LinkedHashMap<String, String>): ResponseEntity<String> {
        body.get("username")?.let {
            userService.createEmailVerification(it)
            return ResponseEntity<String>("Verification code generated, email was sent", HttpStatus.OK)
        }

        return ResponseEntity<String>("Username was not presented.", HttpStatus.BAD_REQUEST)
    }

}