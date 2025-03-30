package com.nookbook.backend.models

import com.nookbook.backend.models.enums.FruitEnum
import com.nookbook.backend.models.enums.HemisphereEnum
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    var password: String,

    @Column
    var name: String,

    @Column(name = "island_name")
    val islandName: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role_junction",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var authorities: MutableSet<RoleEntity> = HashSet(),

    @Column(name = "native_fruit") @Enumerated(EnumType.STRING)
    var nativeFruit: FruitEnum? = FruitEnum.UNDEFINED,

    @Column(name = "island_id")
    var islandId: String? = "",

    @Column @Enumerated(EnumType.STRING)
    var hemisphere: HemisphereEnum? = HemisphereEnum.UNDEFINED,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    val id: Long? = null
) {

}