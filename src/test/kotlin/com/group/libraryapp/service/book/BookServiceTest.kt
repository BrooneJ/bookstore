package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookCreateRequest
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class BookServiceTest @Autowired constructor(
  private val bookRepository: BookRepository,
  private val bookService: BookService,
  private val userRepository: UserRepository,
  private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

  @AfterEach
  fun cleanUp() {
    bookRepository.deleteAll()
    userRepository.deleteAll()
  }

  @Test
  @DisplayName("saveBookTest")
  fun saveBookTest() {
    // given
    val request = BookCreateRequest("Harry potter", BookType.COMPUTER)

    // when
    bookService.saveBook(request)

    // then
    val result = bookRepository.findAll()
    assertThat(result).hasSize(1)
    assertThat(result[0].name).isEqualTo("Harry potter")
    assertThat(result[0].type).isEqualTo(BookType.COMPUTER)
  }

  @Test
  @DisplayName("getBooksTest")
  fun loanBookTest() {
    // given
    bookRepository.save(Book.fixture("Harry potter"))
    val savedUser = userRepository.save(User("snoopy", null, Collections.emptyList(), null))
    val request = BookLoanRequest("snoopy", "Harry potter")

    // when
    bookService.loanBook(request)

    // then
    val result = userLoanHistoryRepository.findAll()
    assertThat(result).hasSize(1)
    assertThat(result[0].bookName).isEqualTo("Harry potter")
    assertThat(result[0].user.id).isEqualTo(savedUser.id)
    assertThat(result[0].status).isEqualTo(UserLoanStatus.RETURNED)
  }

  @Test
  @DisplayName("When loaned book is not returned, it should not be available")
  fun loanedBookNotAvailableTest() {
    // given
    bookRepository.save(Book.fixture("Harry potter"))
    val savedUser = userRepository.save(User("snoopy", null))
    userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "Harry potter"))
    val request = BookLoanRequest("snoopy", "Harry potter")

    // when & then
    val message = assertThrows<IllegalArgumentException> {
      bookService.loanBook(request)
    }.message
    assertThat(message).isEqualTo("Book is already loaned")
  }

  @Test
  @DisplayName("return book test")
  fun returnBookTest() {
    // given
    val savedUser = userRepository.save(User("snoopy", null))
    userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "Harry potter"))
    val request = BookReturnRequest("snoopy", "Harry potter")

    // when
    bookService.returnBook(request)

    // then
    val result = userLoanHistoryRepository.findAll()
    assertThat(result).hasSize(1)
    assertThat(result[0].status).isEqualTo(UserLoanStatus.RETURNED)
  }
}