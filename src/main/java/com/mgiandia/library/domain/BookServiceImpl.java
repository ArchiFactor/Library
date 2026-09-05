package com.mgiandia.library.domain;

import io.quarkus.arc.Unremovable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import java.util.List;

@Unremovable
@Singleton
public class BookServiceImpl implements BookService {
    @Inject
    private EntityManager entityManager;

    @Override
    public List<Book> getBooksByAuthorId(Integer id) {
        if (id == null) {
            return List.of();
        }
        return entityManager.createQuery("SELECT e FROM Book e JOIN e.authorids ec WHERE ec = :id", Book.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Book getBookById(Integer id) {
        return entityManager.find(Book.class, id);
    }

}
