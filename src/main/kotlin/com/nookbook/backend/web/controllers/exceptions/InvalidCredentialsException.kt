package com.nookbook.backend.web.controllers.exceptions

class InvalidCredentialsException : RuntimeException("Username or password does not exist") {
}