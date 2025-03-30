package com.nookbook.backend.core.services.exceptions

class UsernameAlreadyTakenException : RuntimeException("The username provided is already taken.") {
}