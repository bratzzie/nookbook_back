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

    @Column(name = "creator_id")
    var creatorId: String,

    @Column @Enumerated(EnumType.STRING)
    var hemisphere: HemisphereEnum,

    var accountEnabled: Boolean,

    @Column(name = "verification_code")
    @JsonIgnore
    var verificationCode: Long?,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    var id: Long,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "followings",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "following_id")]
    )
    var followingUsers: MutableSet<UserEntity>,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "followers",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "follower_id")]
    )
    var followerUsers: MutableSet<UserEntity>
) {

}