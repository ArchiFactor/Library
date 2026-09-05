package com.mgiandia.library.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Η κάρτα δανεισμού ενός δανειζόμενου.
 * Κάθε δανειζόμενος έχει το πολύ μία κάρτα και κάθε κάρτα ανήκει σε έναν δανειζόμενο.
 */
@Entity
@Table(name="borrowercards")
public class BorrowerCard {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="cardno", length=20)
    private String cardNumber;

    @Column(name="issuedate")
    private LocalDate issueDate;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="borrowerno")
    private Borrower borrower;

    /**
     * Προκαθορισμένος κατασκευαστής.
     */
    public BorrowerCard() { }

    /**
     * Βοηθητικός κατασκευαστής.
     * @param cardNumber Ο αριθμός της κάρτας
     * @param issueDate Η ημερομηνία έκδοσης
     */
    public BorrowerCard(String cardNumber, LocalDate issueDate) {
        this.cardNumber = cardNumber;
        this.issueDate = issueDate;
    }

    public Integer getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    /**
     * Επιστρέφει τον δανειζόμενο στον οποίο ανήκει η κάρτα.
     * @return Ο δανειζόμενος
     */
    public Borrower getBorrower() {
        return borrower;
    }

    /**
     * Θέτει τον δανειζόμενο της κάρτας και ενημερώνει και τις δύο πλευρές της σχέσης.
     * @param borrower Ο δανειζόμενος
     * @see Borrower#getCard()
     */
    public void setBorrower(Borrower borrower) {
        if (this.borrower != null) {
            this.borrower.friendCard(null);
        }
        this.borrower = borrower;
        if (this.borrower != null) {
            this.borrower.friendCard(this);
        }
    }
}
