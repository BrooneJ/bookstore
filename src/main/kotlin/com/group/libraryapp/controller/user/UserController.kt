package com.group.libraryapp.controller.user

import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.service.user.UserService
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
  val userService: UserService
) {

  @PostMapping("/user")
  fun saveUser(@RequestBody request: UserCreateRequest) {
    userService.saveUser(request)
  }

  @GetMapping("/user")
  fun getUsers(): List<UserResponse> {
    return userService.getUsers()
  }

  @PutMapping("/user")
  fun updateUser(@RequestBody request: UserUpdateRequest) {
    userService.updateUser(request)
  }

  @DeleteMapping("/user")
  fun deleteUser(@RequestParam name: String) {
    userService.deleteUser(name)
  }
}