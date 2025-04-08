package com.nookbook.backend.core.services

import com.nookbook.backend.core.services.exceptions.*
import com.nookbook.backend.persistence.models.RoleEntity
import com.nookbook.backend.persistence.models.UserEntity
import com.nookbook.backend.persistence.repositories.RoleRepository
import com.nookbook.backend.persistence.repositories.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.math.floor

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val gmailService: GmailService,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    fun createUser(user: UserEntity): UserEntity {
        if (userRepository.findByEmail(user.email).isPresent)
            throw EmailAlreadyTakenException()
        if (userRepository.findByUsername(user.username).isPresent)
            throw UsernameAlreadyTakenException()

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
            "Verification code for Nookbook!",
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

    fun getUserByUsername(username: String): UserEntity {
        val optionalUser = userRepository.findByUsername(username)

        if (optionalUser.isPresent)
            return optionalUser.get()
        else
            throw UserDoesNotExistException()
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        val user: UserEntity? = username?.let { getUserByUsername(it) }

        val authorities: Set<GrantedAuthority> =
            user!!.authorities.map { roleEntity: RoleEntity -> SimpleGrantedAuthority(roleEntity.authority) }.toSet()

        val userDetails: UserDetails = User(user.username, user.password, authorities)

        return userDetails
    }


    fun updateUser(userEntity: UserEntity): UserEntity {
        return userRepository.save(userEntity)
    }

    fun befriendUser(currentUsername: String, targetUsername: String): Set<UserEntity> {
        val currentUser = getUserByUsername(currentUsername)
        val currentUserFriends = currentUser.friends

        val targetUser = getUserByUsername(targetUsername)

        if (currentUsername == targetUsername) {
            throw UserFriendException("You cannot befriend yourself")
        }

        if (currentUserFriends.contains(targetUser)) {
            throw UserFriendException("You already sent friend request to ${targetUser.name}")
        }

        currentUserFriends.add(targetUser)

        userRepository.save(currentUser)

        return currentUser.friends
    }

    fun getMutualFriends(username: String): Set<UserEntity> {
        // TODO:
        // return userRepository.findMutualFriendsByUsername(username)
        val currentUser = getUserByUsername(username)
        val currentUserFriends = currentUser.friends
        val mutualFriends = mutableSetOf<UserEntity>()

        for (friend in currentUserFriends) {
            val potentialFriend = getUserByUsername(friend.username)
            if (potentialFriend.friends.contains(currentUser)) {
                mutualFriends.add(friend)
            }
        }

        return mutualFriends
    }

    private fun createCode(): Long = floor(Math.random() * 100_000_000).toLong()

}