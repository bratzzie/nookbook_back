package com.nookbook.backend.core.converters

import com.nookbook.backend.persistence.models.UserEntity
import com.nookbook.backend.web.models.UserDTO
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Component

@Component
class UserDTOToUserEntityConverter(private val modelMapper: ModelMapper) {
    fun toUserEntity(dto: UserDTO): UserEntity {
        return modelMapper.map<UserEntity>(dto, UserEntity::class.java)
    }

    fun toUserDTO(entity: UserEntity): UserDTO {
        return modelMapper.map<UserDTO>(entity, UserDTO::class.java)
    }

}