package com.nookbook.backend.web.models

class AuthenticatedUserDTO(val user: UserDTO, val token: String) {
    constructor() : this(UserDTO(), "")
}