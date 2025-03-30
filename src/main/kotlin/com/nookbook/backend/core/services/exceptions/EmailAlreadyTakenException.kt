package com.nookbook.backend.core.services.exceptions

class EmailAlreadyTakenException : RuntimeException("The email provided is already taken.") {
}