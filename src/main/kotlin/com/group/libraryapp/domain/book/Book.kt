package com.group.libraryapp.domain.book

import javax.persistence.*

@Entity
class Book(
  val name: String,

  @Enumerated(EnumType.STRING)
  val type: BookType,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,
) {

  init {
    if (name.isBlank()) {
      throw IllegalArgumentException("Name not allowed blank.")
    }
  }

  companion object {
    fun fixture(
      name: String = "Book Name",
      type: BookType = BookType.COMPUTER,
      id: Long? = null,
    ): Book {
      return Book(
        name = name,
        type = type,
        id = id,
      )
    }
  }
}