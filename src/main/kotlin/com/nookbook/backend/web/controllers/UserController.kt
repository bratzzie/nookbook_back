package com.nookbook.backend.web.controllers

import com.nookbook.backend.core.converters.UserDTOToUserEntityConverter
import com.nookbook.backend.core.services.TokenService
import com.nookbook.backend.core.services.UserService
import com.nookbook.backend.web.models.UserDTO
import org.apache.http.HttpHeaders
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
class UserController(
    private val userService: UserService,
    private val tokenService: TokenService,
    private val converter: UserDTOToUserEntityConverter
) {
    @GetMapping("/verify")
    fun getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) token: String): UserDTO? {
        var username = ""

        if (token.substring(0, 6) == "Bearer") {
            val strippedToken = token.substring(7)
            username = tokenService.getUsernameFromToken(strippedToken)
        }

        val user: UserDTO? = try {
            converter.toUserDTO(userService.getUserByUsername(username))
        } catch (e: Exception) {
            null
        }

        return user


    }
}