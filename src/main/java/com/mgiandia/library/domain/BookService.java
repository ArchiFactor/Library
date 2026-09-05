package com.mgiandia.library.domain;

import java.util.List;

public interface BookService {
    List<Book> getBooksByAuthorId(Integer id);
    Book getBookById(Integer id);
}
