package com.group.libraryapp.dto.book.request;

public class BookCreateRequest {

  private final String name;

  public BookCreateRequest(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
