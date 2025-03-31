package com.nookbook.backend.core.services

import com.nookbook.backend.core.converters.RegistrationDTOToUserEntityConverter
import com.nookbook.backend.core.services.exceptions.EmailAlreadyTakenException
import com.nookbook.backend.core.services.exceptions.IncorrectVerificationCodeException
import com.nookbook.backend.core.services.exceptions.UserDoesNotExistException
import com.nookbook.backend.core.services.exceptions.UsernameAlreadyTakenException
import com.nookbook.backend.persistence.models.RoleEntity
import com.nookbook.backend.persistence.models.UserEntity
import com.nookbook.backend.persistence.repositories.RoleRepository
import com.nookbook.backend.persistence.repositories.UserRepository
import com.nookbook.backend.web.models.RegistrationDTO
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.math.floor

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val converter: RegistrationDTOToUserEntityConverter,
    private val gmailService: GmailService,
    private val passwordEncoder: PasswordEncoder
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

    fun verifyEmail(username: String, code: Long): UserEntity {

        val user = getUserByUsername(username)

        if (code == user.verificationCode) {
            user.accountEnabled = true
            user.verificationCode = null
            return userRepository.save(user)
        } else
            throw IncorrectVerificationCodeException()

    }

    fun updatePassword(username: String, password: String): UserEntity {
        val user = getUserByUsername(username)

        val encodedPassword = passwordEncoder.encode(password)

        user.password = encodedPassword

        return userRepository.save(user)
    }

    private fun getUserByUsername(username: String): UserEntity {
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