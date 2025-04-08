package com.nookbook.backend.web.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.nookbook.backend.core.enums.FruitEnum
import com.nookbook.backend.core.enums.HemisphereEnum
import com.nookbook.backend.persistence.models.UserEntity

class UserDTO(
    var email: String,
    var name: String,
    var username: String,
    var authorities: MutableSet<RoleDTO> = HashSet(),
    @JsonIgnore
    var friends: MutableSet<UserEntity> = HashSet(),
    @JsonIgnore var password: String? = "",
    var islandName: String? = "",
    var nativeFruit: FruitEnum? = FruitEnum.UNDEFINED,
    var creatorId: String? = "",
    var hemisphere: HemisphereEnum? = HemisphereEnum.UNDEFINED,
    var accountEnabled: Boolean? = false,
    var verificationCode: Long? = 0,
    var id: Long? = null
) {
    constructor() : this("", "", "") {

    }
}