package com.nookbook.backend.core.converters

import com.nookbook.backend.persistence.models.UserEntity
import com.nookbook.backend.web.models.RegistrationDTO
import org.springframework.stereotype.Component

@Component
class RegistrationDTOToUserEntityConverter {
    fun toUserEntity(dto: RegistrationDTO): UserEntity {
        return UserEntity(dto.email, dto.username, "", dto.name, "")
    }
}