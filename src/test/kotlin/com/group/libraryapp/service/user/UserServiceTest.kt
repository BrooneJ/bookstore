package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
  private val userRepository: UserRepository,
  private val userService: UserService,
  private val userLoanHistoryRepository: UserLoanHistoryRepository
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

  @Test
  @DisplayName("Users with no loan history are also included in the response.")
  fun getUserLoanHistoriesTest1() {
    // given
    userRepository.save(User("A", null))

    // when
    val result = userService.getUserLoanHistories()

    // then
    assertThat(result).hasSize(1)
    assertThat(result[0].name).isEqualTo("A")
    assertThat(result[0].books).isEmpty()
  }

  @Test
  @DisplayName("The response works normally for user with extensice loan history.")
  fun getUserLoanHistoriesTest2() {
    // given
    val saveUser = userRepository.save(User("A", null))
    userLoanHistoryRepository.saveAll(
      listOf(
        UserLoanHistory.fixture(saveUser, "Book1", UserLoanStatus.LOANED),
        UserLoanHistory.fixture(saveUser, "Book2", UserLoanStatus.LOANED),
        UserLoanHistory.fixture(saveUser, "Book3", UserLoanStatus.RETURNED),
      )
    )

    // when
    val result = userService.getUserLoanHistories()

    // then
    assertThat(result).hasSize(1)
    assertThat(result[0].name).isEqualTo("A")
    assertThat(result[0].books).hasSize(3)
    assertThat(result[0].books).extracting("name")
      .containsExactlyInAnyOrder("Book1", "Book2", "Book3")
    assertThat(result[0].books).extracting("isReturn")
      .containsExactlyInAnyOrder(false, false, true)
  }
}