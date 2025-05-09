package com.nookbook.backend.persistence.repositories

import com.nookbook.backend.persistence.models.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {

    fun findByUsername(username: String): Optional<UserEntity>

    fun findByEmail(email: String): Optional<UserEntity>


    //TODO:
//    @Query(
//        ("")
//    )
//    fun findMutualFriendsByUsername(username: String): Set<UserEntity>
}