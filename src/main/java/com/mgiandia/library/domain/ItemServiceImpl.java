package com.mgiandia.library.domain;

import io.quarkus.arc.Unremovable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import java.util.List;

@Unremovable
@Singleton
public class ItemServiceImpl implements ItemService {
    @Inject
    private EntityManager entityManager;

    @Override
    public List<Item> getItemsByBookno(Integer bookno) {
        if (bookno == null) {
            return List.of();
        }
        return entityManager.createQuery("SELECT e FROM Item e WHERE e.bookno = :bookno", Item.class)
                .setParameter("bookno", bookno)
                .getResultList();
    }

}
