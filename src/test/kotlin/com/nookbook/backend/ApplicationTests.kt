package com.nookbook.backend

import com.nookbook.backend.models.RoleEntity
import com.nookbook.backend.models.UserEntity
import com.nookbook.backend.repositories.RoleRepository
import com.nookbook.backend.services.UserService
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
            "myemail.com", "myusername", "mypassword", "myname",
            "myislandname", HashSet()
        )

        userService.createUser(user)
    }

}
