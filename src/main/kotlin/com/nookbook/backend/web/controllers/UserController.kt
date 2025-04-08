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
    fun getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) token: String): UserDTO {
        val username = tokenService.getUsernameFromToken(token)
        return converter.toUserDTO(userService.getUserByUsername(username))
    }

    @GetMapping("/picture")
    fun getProfilePicture(@RequestHeader(HttpHeaders.AUTHORIZATION) token: String): ResponseEntity<ByteArray> {
        val username = tokenService.getUsernameFromToken(token)
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
            file.deleteRecursively()
        }

    }

    @PostMapping("/picture/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadProfilePicture(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @RequestParam file: MultipartFile
    ) {
        val username = tokenService.getUsernameFromToken(token)
        userImageService.uploadPicture(username, file)
    }
}