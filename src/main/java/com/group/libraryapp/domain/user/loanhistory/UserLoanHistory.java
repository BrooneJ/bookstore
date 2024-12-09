package com.group.libraryapp.domain.user.loanhistory;

import com.group.libraryapp.domain.user.User;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
public class UserLoanHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id = null;

  @ManyToOne
  private User user;

  private String bookName;

  private boolean isReturn;

  protected UserLoanHistory() {
  }

  public UserLoanHistory(User user, String bookName, boolean isReturn) {
    this.user = user;
    this.bookName = bookName;
    this.isReturn = isReturn;
  }

  public void doReturn() {
    this.isReturn = true;
  }

  @NotNull
  public String getBookName() {
    return this.bookName;
  }

  @NotNull
  public User getUser() {
    return user;
  }

  public boolean isReturn() {
    return isReturn;
  }
}
