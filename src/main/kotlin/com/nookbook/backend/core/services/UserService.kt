package com.nookbook.backend.core.services

import com.nookbook.backend.core.converters.RegistrationDTOToUserEntityConverter
import com.nookbook.backend.core.services.exceptions.EmailAlreadyTakenException
import com.nookbook.backend.core.services.exceptions.UserDoesNotExistException
import com.nookbook.backend.core.services.exceptions.UsernameAlreadyTakenException
import com.nookbook.backend.persistence.models.RoleEntity
import com.nookbook.backend.persistence.models.UserEntity
import com.nookbook.backend.persistence.repositories.RoleRepository
import com.nookbook.backend.persistence.repositories.UserRepository
import com.nookbook.backend.web.models.RegistrationDTO
import org.springframework.stereotype.Service
import kotlin.math.floor

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val converter: RegistrationDTOToUserEntityConverter,
    private val gmailService: GmailService
) {

    fun createUser(userDTO: RegistrationDTO): UserEntity {
        if (userRepository.findByEmail(userDTO.email).isPresent)
            throw EmailAlreadyTakenException()
        if (userRepository.findByUsername(userDTO.username).isPresent)
            throw UsernameAlreadyTakenException()

        val user = converter.toUserEntity(userDTO)
        val roles: MutableSet<RoleEntity> = user.authorities
        //TODO: remove from here
        if (roleRepository.findRoleByAuthority("USER").isEmpty)
            roleRepository.save(RoleEntity("USER"))

        roles.add(roleRepository.findRoleByAuthority("USER").get())
        user.authorities = roles

        return userRepository.save(user)
    }

    fun createEmailVerification(username: String) {
        val user = getUserByUsername(username)

        user.verificationCode = createCode()

        gmailService.sendEmail(
            user.email,
            "Verificaton code for Nookbook!",
            "Check your verification code: ${user.verificationCode}"
        )
        userRepository.save(user)
    }


    fun getUserByUsername(username: String): UserEntity {
        val optionalUser = userRepository.findByUsername(username)

        if (optionalUser.isPresent)
            return optionalUser.get()
        else
            throw UserDoesNotExistException()
    }

    private fun createCode(): Long = floor(Math.random() * 100_000_000).toLong()

//    fun updateUser(user: UserEntity) {
//
//    }
}