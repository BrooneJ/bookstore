package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
  private val userRepository: UserRepository,
  private val userService: UserService
) {

  @AfterEach
  fun cleanUp() {
    userRepository.deleteAll()
  }

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

  @Test
  fun getUsersTest() {
    // given
    userRepository.saveAll(
      listOf(
        User("snoopy", null),
        User("woodstock", 3)
      )
    )

    // when
    val result = userService.getUsers()

    // then
    assertThat(result).hasSize(2)
    assertThat(result).extracting("name").containsExactlyInAnyOrder("snoopy", "woodstock")
    assertThat(result).extracting("age").containsExactlyInAnyOrder(null, 3)
  }

  @Test
  fun updateUserNameTest() {
    // given
    val saveUser = userRepository.save(User("snoopy", null))
    val request = UserUpdateRequest(saveUser.id!!, "woodstock")

    // when
    userService.updateUser(request)

    // then
    val result = userRepository.findAll()[0]
    assertThat(result.name).isEqualTo("woodstock")
  }

  @Test
  fun deleteUserTest() {
    // given
    userRepository.save(User("snoopy", null))

    // when
    userService.deleteUser("snoopy")

    // then
    val result = userRepository.findAll()
    assertThat(result).isEmpty()
  }
}