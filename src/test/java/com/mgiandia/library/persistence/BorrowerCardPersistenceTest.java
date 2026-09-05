package com.mgiandia.library.persistence;

import java.time.LocalDate;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import com.mgiandia.library.Fixture;
import com.mgiandia.library.IntegrationBase;
import com.mgiandia.library.domain.Borrower;
import com.mgiandia.library.domain.BorrowerCard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;

/**
 * Ελέγχει τη σχέση ένα-προς-ένα δανειζόμενου και κάρτας δανεισμού μέσα από τη βάση.
 *
 * Και οι δύο πλευρές διασχίζονται αφού καθαριστεί το persistence context, ώστε οι έλεγχοι
 * να μη βασίζονται σε αντικείμενα που βρίσκονται ήδη στη μνήμη. Μετά την αποσύνδεση της
 * σχέσης από το ORM οι ίδιοι έλεγχοι διασχίζουν τα παραγόμενα transient πεδία.
 */
@QuarkusTest
public class BorrowerCardPersistenceTest extends IntegrationBase {

    @Inject
    EntityManager em;

    @Test
    @TestTransaction
    public void navigateFromOwningSideToBorrower() {
        em.clear();

        BorrowerCard card = em.find(BorrowerCard.class, Fixture.BorrowerCards.GIAKOUMAKIS_CARD_ID);

        Assertions.assertNotNull(card);
        Assertions.assertEquals(Fixture.BorrowerCards.GIAKOUMAKIS_CARD_NO, card.getCardNumber());
        Assertions.assertNotNull(card.getBorrower());
        Assertions.assertEquals(Fixture.Borrowers.GIAKOUMAKIS_ID, card.getBorrower().getBorrowerNo());
    }

    @Test
    @TestTransaction
    public void navigateFromInverseSideToCard() {
        em.clear();

        Borrower borrower = em.find(Borrower.class, Fixture.Borrowers.GIAKOUMAKIS_ID);

        Assertions.assertNotNull(borrower);
        Assertions.assertNotNull(borrower.getCard());
        Assertions.assertEquals(Fixture.BorrowerCards.GIAKOUMAKIS_CARD_NO, borrower.getCard().getCardNumber());
    }

    @Test
    @TestTransaction
    public void borrowerWithoutACardReadsNull() {
        em.clear();

        Borrower borrower = em.find(Borrower.class, Fixture.Borrowers.DIAMANTIDIS_ID);

        Assertions.assertNotNull(borrower);
        Assertions.assertNull(borrower.getCard());
    }

    @Test
    @TestTransaction
    public void issueACardToABorrowerWithoutOne() {
        Borrower borrower = em.find(Borrower.class, Fixture.Borrowers.DIAMANTIDIS_ID);
        BorrowerCard card = new BorrowerCard("CARD-0002", LocalDate.of(2024, 5, 20));
        card.setBorrower(borrower);

        em.persist(card);
        em.flush();
        em.clear();

        Borrower reloaded = em.find(Borrower.class, Fixture.Borrowers.DIAMANTIDIS_ID);
        Assertions.assertNotNull(reloaded.getCard());
        Assertions.assertEquals("CARD-0002", reloaded.getCard().getCardNumber());
    }

    @Test
    @TestTransaction
    public void roundTripKeepsBothSidesConsistent() {
        em.clear();

        BorrowerCard card = em.find(BorrowerCard.class, Fixture.BorrowerCards.GIAKOUMAKIS_CARD_ID);
        Borrower borrower = card.getBorrower();

        Assertions.assertEquals(Fixture.BorrowerCards.GIAKOUMAKIS_CARD_ID, borrower.getCard().getId());
    }
}
