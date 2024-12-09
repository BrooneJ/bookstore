package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
  private val userRepository: UserRepository,
  private val userService: UserServiceV2
) {
  @Test
  fun saveUserTest() {
    // given
    val userCreateRequest = UserCreateRequest("snoopy", null)

    // when
    userService.saveUser(userCreateRequest)

    // then
    val result = userRepository.findAll()
    assertThat(result).hasSize(1)
    assertThat(result[0].name).isEqualTo("snoopy")
    assertThat(result[0].age).isNull()
  }
}