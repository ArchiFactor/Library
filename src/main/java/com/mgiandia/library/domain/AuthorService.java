package com.mgiandia.library.domain;

import java.util.Collection;
import java.util.List;

public interface AuthorService {
    List<Author> getAuthorsByIds(Collection<Integer> ids);
}
