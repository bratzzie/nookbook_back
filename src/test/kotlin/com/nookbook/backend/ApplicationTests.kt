package com.nookbook.backend

import com.nookbook.backend.core.enums.FruitEnum
import com.nookbook.backend.core.enums.HemisphereEnum
import com.nookbook.backend.core.services.UserService
import com.nookbook.backend.persistence.models.RoleEntity
import com.nookbook.backend.persistence.models.UserEntity
import com.nookbook.backend.persistence.repositories.RoleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationTests {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Test
    fun shouldUserServiceCreateNewUser_WhenGivenIdentityAndBasicUserInfo() {
        roleRepository.save(RoleEntity("USER"))
        val user = UserEntity(
            "myemail.com", "myusername", "",
            "myname", "",
            HashSet(),
            FruitEnum.UNDEFINED,
            "",
            HemisphereEnum.UNDEFINED,
            false,
            0L,
            0L
        )

        assertThat(
            userService.createUser(user)
        ).usingRecursiveComparison().ignoringFields("authorities").isEqualTo(
            UserEntity(
                "myemail.com",
                "myusername",
                "",
                "myname",
                "",
                HashSet(),
                FruitEnum.UNDEFINED,
                "",
                HemisphereEnum.UNDEFINED,
                false,
                0L,
                1L
            )
        )
    }

}
