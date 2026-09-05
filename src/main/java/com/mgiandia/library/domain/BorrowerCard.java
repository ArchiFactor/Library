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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

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

    @Transient
	private Borrower borrower;

	@Column(name = "borrowerno", unique = true)
	private int borrowerno;

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
		if (borrower == null && borrowerno != 0 && ServiceFactory.isContainerAvailable()) {
			BorrowerService borrowerService = ServiceFactory.getBorrowerService();
			borrower = borrowerService.getBorrowerById(borrowerno);
		}
		return borrower;
	}

    /**
     * Θέτει τον δανειζόμενο της κάρτας και ενημερώνει και τις δύο πλευρές της σχέσης.
     * @param borrower Ο δανειζόμενος
     * @see Borrower#getCard()
     */
    public void setBorrower(Borrower borrower) {
        if (this.getBorrower() != null) {
            this.getBorrower().friendCard(null);
        }
        this.borrower = borrower;
		this.borrowerno = borrower != null ? borrower.getBorrowerNo() : 0;
        if (this.getBorrower() != null) {
            this.getBorrower().friendCard(this);
        }
    }

	@PrePersist
	@PreUpdate
	private void syncUnmappedForeignKeys() {
		if (this.borrower != null) {
			this.borrowerno = this.borrower.getBorrowerNo();
		}
	}
}
