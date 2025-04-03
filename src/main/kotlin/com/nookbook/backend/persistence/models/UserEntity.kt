package com.nookbook.backend.persistence.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.nookbook.backend.core.enums.FruitEnum
import com.nookbook.backend.core.enums.HemisphereEnum
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false, unique = true)
    var username: String,

    @Column(nullable = false)
    var password: String,

    @Column
    var name: String,

    @Column(name = "island_name")
    var islandName: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role_junction",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var authorities: MutableSet<RoleEntity> = HashSet(),

    @Column(name = "native_fruit") @Enumerated(EnumType.STRING)
    var nativeFruit: FruitEnum,

    @Column(name = "island_id")
    var islandId: String,

    @Column @Enumerated(EnumType.STRING)
    var hemisphere: HemisphereEnum,

    var accountEnabled: Boolean,

    @Column(name = "verification_code")
    @JsonIgnore
    var verificationCode: Long?,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    var id: Long
) {

    override fun toString(): String {
        return "UserEntity(email='$email', username='$username', password='$password', name='$name', islandName='$islandName', authorities=$authorities, nativeFruit=$nativeFruit, islandId=$islandId, hemisphere=$hemisphere, accountEnabled=$accountEnabled, verificationCode=$verificationCode, id=$id)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        if (accountEnabled != other.accountEnabled) return false
        if (verificationCode != other.verificationCode) return false
        if (id != other.id) return false
        if (email != other.email) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (name != other.name) return false
        if (islandName != other.islandName) return false
        if (authorities != other.authorities) return false
        if (nativeFruit != other.nativeFruit) return false
        if (islandId != other.islandId) return false
        if (hemisphere != other.hemisphere) return false

        return true
    }

    override fun hashCode(): Int {
        var result = accountEnabled.hashCode()
        result = 31 * result + verificationCode.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + islandName.hashCode()
        result = 31 * result + authorities.hashCode()
        result = 31 * result + nativeFruit.hashCode()
        result = 31 * result + islandId.hashCode()
        result = 31 * result + hemisphere.hashCode()
        return result
    }


}