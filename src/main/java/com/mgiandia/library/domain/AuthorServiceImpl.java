package com.mgiandia.library.domain;

import io.quarkus.arc.Unremovable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

@Unremovable
@Singleton
public class AuthorServiceImpl implements AuthorService {
    @Inject
    private EntityManager entityManager;

    @Override
    public List<Author> getAuthorsByIds(Collection<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return entityManager.createQuery("SELECT e FROM Author e WHERE e.id IN :ids", Author.class)
                .setParameter("ids", ids)
                .getResultList();
    }

}
