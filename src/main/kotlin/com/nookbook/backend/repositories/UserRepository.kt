package com.nookbook.backend.repositories

import com.nookbook.backend.models.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<UserEntity, Long> {

    fun findByUsername(username: String): Optional<UserEntity>

}