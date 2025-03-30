package com.nookbook.backend.persistence.repositories

import com.nookbook.backend.persistence.models.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<RoleEntity, Int> {

    fun findRoleByAuthority(authority: String): Optional<RoleEntity>

}