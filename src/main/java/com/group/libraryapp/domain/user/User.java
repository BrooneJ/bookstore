package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<UserLoanHistory> userLoanHistories = new ArrayList<>();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id = null;
  @Column(nullable = false, length = 20)
  private String name;
  private Integer age;

  protected User() {
  }

  public User(String name, Integer age) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }

    this.name = name;
    this.age = age;
  }

  @NotNull
  public String getName() {
    return name;
  }

  @Nullable
  public Integer getAge() {
    return age;
  }

  public Long getId() {
    return id;
  }

  public void updateName(String name) {
    this.name = name;
  }

  public void loanBook(String bookName) {
    this.userLoanHistories.add(new UserLoanHistory(this, bookName, false));
  }

  public void returnBook(String bookName) {
    UserLoanHistory targetHistory = this.userLoanHistories.stream()
        .filter(history -> history.getBookName().equals(bookName))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
    targetHistory.doReturn();
  }
}
