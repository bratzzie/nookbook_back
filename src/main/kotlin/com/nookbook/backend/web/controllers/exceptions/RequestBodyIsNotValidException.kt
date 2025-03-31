package com.nookbook.backend.web.controllers.exceptions

class RequestBodyIsNotValidException(val param: String) : RuntimeException("$param is not parsable.") {
}