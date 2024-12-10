package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.book.BookRepository;
import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceV2 {

  private final UserRepository userRepository;
  private final BookRepository bookRepository;
  private final UserLoanHistoryRepository userLoanHistoryRepository;

  public UserServiceV2(
      UserRepository userRepository,
      BookRepository bookRepository,
      UserLoanHistoryRepository userLoanHistoryRepository
  ) {
    this.userRepository = userRepository;
    this.bookRepository = bookRepository;
    this.userLoanHistoryRepository = userLoanHistoryRepository;
  }

  @Transactional
  public void saveUser(UserCreateRequest request) {
    userRepository.save(new User(request.getName(), request.getAge(), Collections.emptyList(), null));
  }

  @Transactional(readOnly = true)
  public List<UserResponse> getUsers() {
    return userRepository.findAll().stream()
        .map(UserResponse::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public void updateUser(UserUpdateRequest request) {
    User user = userRepository.findById(request.getId())
        .orElseThrow(IllegalArgumentException::new);

    user.updateName(request.getName());
  }

  @Transactional
  public void deleteUser(String name) {
    User user = userRepository.findByName(name)
        .orElseThrow(IllegalArgumentException::new);
    if (user == null) {
      throw new IllegalArgumentException();
    }

    userRepository.delete(user);
  }
}
