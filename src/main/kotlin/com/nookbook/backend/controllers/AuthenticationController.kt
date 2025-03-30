package com.nookbook.backend.controllers

import com.nookbook.backend.models.UserEntity
import com.nookbook.backend.services.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthenticationController(private val userService: UserService) {

    //TODO: controller with mapping for Entities to DTO
    @PostMapping("/reg")
    fun registerUser(@RequestBody user: UserEntity): UserEntity {
        return userService.createUser(user)
    }

}