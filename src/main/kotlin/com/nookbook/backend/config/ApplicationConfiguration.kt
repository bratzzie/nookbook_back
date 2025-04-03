package com.nookbook.backend.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
open class ApplicationConfiguration {
    @Bean
    open fun modelMapper(): ModelMapper {
        return ModelMapper()
    }
}