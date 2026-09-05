package com.mgiandia.library.domain;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Ελέγχει τη σχέση ένα-προς-ένα ανάμεσα στον δανειζόμενο και την κάρτα δανεισμού
 * αποκλειστικά μέσα στη μνήμη, χωρίς βάση δεδομένων.
 */
public class BorrowerCardTest {

    @Test
    public void borrowerHasNoCardInitially() {
        Borrower borrower = new Borrower();
        Assertions.assertNull(borrower.getCard());
    }

    @Test
    public void cardHasNoBorrowerInitially() {
        BorrowerCard card = new BorrowerCard("CARD-9001", LocalDate.of(2024, 3, 1));
        Assertions.assertNull(card.getBorrower());
    }

    @Test
    public void setBorrowerUpdatesBothSides() {
        Borrower borrower = new Borrower();
        BorrowerCard card = new BorrowerCard("CARD-9001", LocalDate.of(2024, 3, 1));

        card.setBorrower(borrower);

        Assertions.assertSame(borrower, card.getBorrower());
        Assertions.assertSame(card, borrower.getCard());
    }

    @Test
    public void setNullBorrowerDetachesBothSides() {
        Borrower borrower = new Borrower();
        BorrowerCard card = new BorrowerCard("CARD-9001", LocalDate.of(2024, 3, 1));
        card.setBorrower(borrower);

        card.setBorrower(null);

        Assertions.assertNull(card.getBorrower());
        Assertions.assertNull(borrower.getCard());
    }

    @Test
    public void reassigningTheCardClearsThePreviousBorrower() {
        Borrower first = new Borrower();
        Borrower second = new Borrower();
        BorrowerCard card = new BorrowerCard("CARD-9001", LocalDate.of(2024, 3, 1));
        card.setBorrower(first);

        card.setBorrower(second);

        Assertions.assertNull(first.getCard());
        Assertions.assertSame(card, second.getCard());
        Assertions.assertSame(second, card.getBorrower());
    }

    @Test
    public void bidirectionalAssociationInvariant() {
        Borrower borrower = new Borrower();
        BorrowerCard card = new BorrowerCard("CARD-9001", LocalDate.of(2024, 3, 1));
        card.setBorrower(borrower);

        Assertions.assertSame(card, card.getBorrower().getCard());
        Assertions.assertSame(borrower, borrower.getCard().getBorrower());
    }
}
