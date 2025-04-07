package com.nookbook.backend

import com.nookbook.backend.config.RSAKeyProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyProperties::class)
class Application {
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}


