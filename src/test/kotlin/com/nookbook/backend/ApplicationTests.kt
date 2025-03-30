package com.nookbook.backend

import com.nookbook.backend.core.services.UserService
import com.nookbook.backend.persistence.models.RoleEntity
import com.nookbook.backend.persistence.models.UserEntity
import com.nookbook.backend.persistence.repositories.RoleRepository
import com.nookbook.backend.web.models.RegistrationDTO
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
        val user = RegistrationDTO(
            "myemail.com", "myname", "myusername"
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
                id = 1
            )
        )
    }

}
