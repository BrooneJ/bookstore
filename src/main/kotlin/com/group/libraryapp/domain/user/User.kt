package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import javax.persistence.*

@Entity
@Table(name = "users")
class User(

  var name: String,

  val age: Int?,

  @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
  val userLoanHistory: MutableList<UserLoanHistory> = mutableListOf(),

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,
) {

  init {
    if (name.isBlank()) {
      throw IllegalArgumentException("Name cannot be blank.")
    }
  }

  fun updateName(name: String) {
    this.name = name
  }

  fun loanBook(book: Book) {
    this.userLoanHistory.add(UserLoanHistory(this, book.name, UserLoanStatus.RETURNED))
  }

  fun returnBook(bookName: String) {
    this.userLoanHistory.first { history -> history.bookName == bookName }.doReturn()
  }
}