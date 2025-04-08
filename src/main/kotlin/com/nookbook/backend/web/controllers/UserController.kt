package com.nookbook.backend.web.controllers

import com.nookbook.backend.core.converters.UserDTOToUserEntityConverter
import com.nookbook.backend.core.services.TokenService
import com.nookbook.backend.core.services.UserImageService
import com.nookbook.backend.core.services.UserService
import com.nookbook.backend.web.models.UserDTO
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/user")
@CrossOrigin("*")
class UserController(
    private val userService: UserService,
    private val tokenService: TokenService,
    private val converter: UserDTOToUserEntityConverter,
    private val userImageService: UserImageService
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

    @GetMapping("/picture")
    fun getProfilePicture(@RequestParam username: String): ResponseEntity<ByteArray> {
        val file = userImageService.downloadPicture(username)
        val base64encodedData = userImageService.encodeFile(file)

        try {
            return ResponseEntity.ok()
                .header(
                    HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                            file.name + "\""
                )
                .body(base64encodedData)
        } finally {
            file.deleteRecursively();
        }

    }

    @PostMapping("/picture/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadFile(@RequestParam username: String, @RequestParam file: MultipartFile) {
        userImageService.uploadPicture(username, file)
    }
}