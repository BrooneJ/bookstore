package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookCreateRequest
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.util.fail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(
  private val bookRepository: BookRepository,
  private val userRepository: UserRepository,
  private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

  @Transactional
  fun saveBook(request: BookCreateRequest) {
    val book = Book(request.name, request.type)
    bookRepository.save(book)
  }

  @Transactional
  fun loanBook(request: BookLoanRequest) {
    val book = bookRepository.findByName(request.bookName) ?: fail()
    if (userLoanHistoryRepository.findByBookNameAndIsReturn(request.bookName, false) != null) {
      throw IllegalArgumentException("Book is already loaned")
    }

    val user = userRepository.findByName(request.userName) ?: fail()
    user.loanBook(book)
  }

  @Transactional
  fun returnBook(request: BookReturnRequest) {
    val user = userRepository.findByName(request.userName) ?: fail()
    user.returnBook(request.bookName)
  }
}