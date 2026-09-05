package com.mgiandia.library.domain;

import io.quarkus.arc.Unremovable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;

@Unremovable
@Singleton
public class BorrowerServiceImpl implements BorrowerService {
    @Inject
    private EntityManager entityManager;

    @Override
    public Borrower getBorrowerById(int id) {
        return entityManager.find(Borrower.class, id);
    }

}
