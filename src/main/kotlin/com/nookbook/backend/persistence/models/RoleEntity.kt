package com.nookbook.backend.persistence.models

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class RoleEntity(
    @Column(nullable = false)
    var authority: String,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    val id: Int? = null
) {
    constructor() : this("") {

    }

    override fun toString(): String {
        return "RoleEntity(id=$id, authority='$authority')"
    }
}