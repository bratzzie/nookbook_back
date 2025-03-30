package com.nookbook.backend.repositories

import com.nookbook.backend.models.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface RoleRepository: JpaRepository<RoleEntity, Int> {

    fun findRoleByAuthority(authority: String): Optional<RoleEntity>

}