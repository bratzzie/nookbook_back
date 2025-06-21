package com.nookbook.backend.web.controllers

import com.nookbook.backend.core.converters.UserDTOToUserEntityConverter
import com.nookbook.backend.core.services.TokenService
import com.nookbook.backend.core.services.UserService
import com.nookbook.backend.web.controllers.exceptions.InvalidCredentialsException
import com.nookbook.backend.web.controllers.exceptions.RequestBodyIsNotValidException
import com.nookbook.backend.web.models.AuthenticatedUserDTO
import com.nookbook.backend.web.models.UserDTO
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
class AuthenticationController(
    private val userService: UserService,
    private val userConverter: UserDTOToUserEntityConverter,
    private val tokenService: TokenService,
    private val authManager: AuthenticationManager
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

    @PostMapping("/forgot/code")
    fun createForgotPasswordCode(@RequestBody body: LinkedHashMap<String, String>): ResponseEntity<String> {
        body["email"]?.let {
            userService.createForgotPasswordCode(it)
            return ResponseEntity<String>("Password code generated, email was sent", HttpStatus.OK)
        }

        throw RequestBodyIsNotValidException("Email")
    }

    @PostMapping("/email/verify")
    fun verifyEmail(@RequestBody body: LinkedHashMap<String, String>): UserDTO {
        val code = body["code"]?.toLong() ?: throw RequestBodyIsNotValidException("Verification code")
        val username = body["username"] ?: throw RequestBodyIsNotValidException("Username")

        return userConverter.toUserDTO(userService.verifyEmail(username, code))
    }

    @PostMapping("/forgot/verify")
    fun verifyForgotPasswordCode(@RequestBody body: LinkedHashMap<String, String>): UserDTO {
        val code = body["code"]?.toLong() ?: throw RequestBodyIsNotValidException("Confirmation code")
        val email = body["email"] ?: throw RequestBodyIsNotValidException("Email")

        return userConverter.toUserDTO(userService.verifyForgotPasswordCode(email, code))
    }

    @PostMapping("/login")
    fun login(@RequestBody body: LinkedHashMap<String, String>): AuthenticatedUserDTO {
        val password = body["password"] ?: throw RequestBodyIsNotValidException("Password")
        val username = body["username"] ?: throw RequestBodyIsNotValidException("Username")

        try {
            val auth = authManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
            val token = tokenService.generateToken(auth)
            return AuthenticatedUserDTO(userConverter.toUserDTO(userService.getUserByUsername(username)), token)
        } catch (ex: AuthenticationException) {
            throw InvalidCredentialsException()
        }
    }

    @PutMapping("/reg/password")
    fun updatePasswordByUsername(@RequestBody body: LinkedHashMap<String, String>): UserDTO {
        val password = body["password"] ?: throw RequestBodyIsNotValidException("Password")
        val username = body["username"] ?: throw RequestBodyIsNotValidException("Username")

        return userConverter.toUserDTO(userService.updatePassword(username, password))

    }

    @PutMapping("/forgot/password")
    fun updatePasswordByEmail(@RequestBody body: LinkedHashMap<String, String>): UserDTO {
        val password = body["password"] ?: throw RequestBodyIsNotValidException("Password")
        val email = body["email"] ?: throw RequestBodyIsNotValidException("Email")

        return userConverter.toUserDTO(userService.updatePassword(userService.getUserByEmail(email).username, password))
    }

    @PostMapping("/login/find")
    fun verifyCredentials(@RequestBody body: LinkedHashMap<String, String>): ResponseEntity<String> {
        val httpHeaders: HttpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.TEXT_PLAIN
        val username = userService.verifyCredentials(body)
        return ResponseEntity<String>(username, HttpStatus.OK)
    }

    @GetMapping("/forgot/credentials")
    fun getUserEmail(@RequestParam username: String, @RequestParam email: String): ResponseEntity<String> {

        val user = username.takeIf { it.isNotBlank() }?.let {
            userService.getUserByUsername(it)
        } ?: email.takeIf { it.isNotBlank() }?.let {
            userService.getUserByEmail(it)
        }

        user?.let {
            return ResponseEntity<String>(user.email, HttpStatus.OK)
        }

        throw RequestBodyIsNotValidException("Credentials")
    }
}