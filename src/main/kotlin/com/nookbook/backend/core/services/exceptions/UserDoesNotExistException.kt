package com.nookbook.backend.core.services.exceptions

class UserDoesNotExistException : RuntimeException("The user with the given username does not exist.") {
}