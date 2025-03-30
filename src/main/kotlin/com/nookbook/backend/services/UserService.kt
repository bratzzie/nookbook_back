package com.nookbook.backend.services

import com.nookbook.backend.models.RoleEntity
import com.nookbook.backend.models.UserEntity
import com.nookbook.backend.repositories.RoleRepository
import com.nookbook.backend.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val roleRepository: RoleRepository) {
    fun createUser(user: UserEntity): UserEntity {
        val roles: MutableSet<RoleEntity> = user.authorities
        if (roleRepository.findRoleByAuthority("USER").isEmpty)
            roleRepository.save(RoleEntity("USER"))

        roles.add(roleRepository.findRoleByAuthority("USER").get())
        user.authorities = roles
        return userRepository.save(user)
    }
}