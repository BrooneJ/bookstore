package com.group.libraryapp.domain.user.loanhistory

import org.springframework.data.jpa.repository.JpaRepository

interface UserLoanHistoryRepository : JpaRepository<UserLoanHistory, Long> {

  fun existsByBookNameAndIsReturn(name: String, isReturn: Boolean): Boolean

  fun findByBookNameAndIsReturn(name: String, isReturn: Boolean): UserLoanHistory?

}