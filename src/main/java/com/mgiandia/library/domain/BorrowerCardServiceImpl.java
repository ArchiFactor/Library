package com.mgiandia.library.domain;

import io.quarkus.arc.Unremovable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import java.util.List;

@Unremovable
@Singleton
public class BorrowerCardServiceImpl implements BorrowerCardService {
    @Inject
    private EntityManager entityManager;

    @Override
    public BorrowerCard getBorrowerCardByBorrowerno(int borrowerno) {
        List<BorrowerCard> results = entityManager.createQuery("SELECT e FROM BorrowerCard e WHERE e.borrowerno = :borrowerno", BorrowerCard.class)
                .setParameter("borrowerno", borrowerno)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

}
