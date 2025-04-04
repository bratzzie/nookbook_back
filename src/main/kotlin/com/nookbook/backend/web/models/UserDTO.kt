package com.nookbook.backend.web.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.nookbook.backend.core.enums.FruitEnum
import com.nookbook.backend.core.enums.HemisphereEnum

class UserDTO(
    var email: String,
    var name: String,
    var username: String,
    var authorities: MutableSet<RoleDTO> = HashSet(),
    @JsonIgnore var password: String? = "",
    var islandName: String? = "",
    var nativeFruit: FruitEnum? = FruitEnum.UNDEFINED,
    var islandId: String? = "",
    var hemisphereEnum: HemisphereEnum? = HemisphereEnum.UNDEFINED,
    var accountEnabled: Boolean? = false,
    var verificationCode: Long? = 0,
    var id: Long? = null
) {
    constructor() : this("", "", "") {

    }
}