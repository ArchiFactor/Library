package com.mgiandia.library.domain;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


import jakarta.persistence.*;




/**
 * Το βιβλίο.
 * @author Νίκος Διαμαντίδης
 */
@Entity
@Table(name="books")
public class Book {
    @Id 
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;    
    
    @org.hibernate.annotations.Type(
            value=com.mgiandia.library.persistence.ISBNCustomType.class)
    @Column(name="isbn", length = 20, nullable=false)
    private ISBN isbn;  
    
    @Column(name="title", length=200, nullable=false)
    private String title;
    
    @Column(name="publication", length=10)
    private String publication;
    
    @Column(name="pubyear")
    private int publicationyear;
    
    @ManyToOne(fetch=FetchType.LAZY, 
            cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="publisherid")
    private Publisher publisher;
    
    
    @Transient
	private Set<Item> items = new HashSet<Item>();
    
    @Transient
	private Set<Author> authors = new SyncingAuthorSet();

	@ElementCollection
	@CollectionTable(name = "bookauthors", joinColumns = @JoinColumn(name = "bookid"))
	@Column(name = "authorid")
	private Set<Integer> authorids = new HashSet<>();

    /**
     * Προκαθορισμένος κατασκευαστής.
     */
    public Book() { }

    /**
     * Βοηθητικός κατασκευαστής που αρχικοποιεί τα βασικά στοιχεία ενός βιβλίου.
     * @param title Τίτλος
     * @param isbn ISBN
     * @param publication Αριθμός έκδοσης
     * @param publicationYear Έτος έκδοσης
     * @param publisher Εκδοτικός οίκος
     */
    public Book(String title, ISBN isbn, String publication,
            int publicationYear, Publisher publisher) {
        this.title = title;
        this.isbn = isbn;
        this.publication = publication;
        this.publicationyear = publicationYear;
        this.publisher = publisher;
    }


    public Integer getId() {
        return id;
    }
    
    /**
     * Θέτει τον τίτλο του βιβλίου.
     * @param title Ο τίτλος του βιβλίου
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Επιστρέφει τον τίτλο του βιβλίου.
     * @return Ο Τίτλος του βιβλίου
     */
    public String getTitle() {
        return title;
    }

    /**
     * Θέτει το ISBN του βιβλίου.
     * @param isbn Το ISBN
     */

    public void setIsbn(ISBN isbn) {
        this.isbn = isbn;
    }

    /**
     * Επιστρέφει το ISBN του βιβλίου.
     * @return Το ISBN
     */
    public ISBN getIsbn() {
        return isbn;
    }

    /**
     * Θέτει τον αριθμό της έκδοσης του βιβλίου.
     * @param publication Ο αριθμός της έκδοσης
     */

    public void setPublication(String publication) {
        this.publication = publication;
    }


    /**
     * Επιστρέφει τον αριθμό της έκδοσης του βιβλίου.
     * @return Ο αριθμός της έκδοσης
     */
    public String getPublication() {
        return publication;
    }

    /**
     * Θέτει το έτος έκδοσης του βιβλίου.
     * @param publicationyear Το έτος έκδοσης
     */
    public void setPublicationYear(int publicationyear) {
        this.publicationyear = publicationyear;
    }

    /**
     * Επιστρέφει το έτος έκδοσης του βιβλίου.
     * @return Το έτος έκδοσης
     */
    public int getPublicationYear() {
        return publicationyear;
    }


    /**
     * Θέτει τον εκδότη ({@link Publisher})του βιβλίου.
     * @param publisher Ο Εκδοτικός οίκος του βιβλίου
     */
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Επιστρέφει τον εκδότη ({@link Publisher})του βιβλίου.
     * @return Ο Εκδοτικός οίκος του βιβλίου
     */
    public Publisher getPublisher() {
        return publisher;
    }

    /**
     * Επιστρέφει τα αντίτυπα ({@link Item}) για κάποιο βιβλίο.
     * Η συλλογή των αντιτύπων είναι αντίγραφο.
     * Για την προσθήκη κάποιου αντιτύπου
     * στη συλλογή χρησιμοποιείστε τη μέθοδο {@link Book#addItem(Item)}
     * και για την απομάκρυνση ενός αντιτύπου
     * τη μέθοδο {@link Book#removeItem(Item)}.
     * @return Αντίγραφο της συλλογής των αντιτύπων του βιβλίου
     */
    public Set<Item> getItems() {
        return new HashSet<Item>(this.friendItems());
    }


    /**
     * Επιστρέφει τους συγγραφείς ({@link Author}) για κάποιο βιβλίο.
     * Η συλλογή των αντιτύπων είναι αντίγραφο. Για την
     * προσθήκη κάποιου συγγραφέα στη συλλογή χρησιμοποιείστε
     * τη μέθοδο {@link Book#addAuthor(Author)} και για την
     * απομάκρυνση ενός αντιτύπου τη μέθοδο {@link Book#removeAuthor(Author)}.
     * @return Αντίγραφο της συλλογής των συγγραφέων του βιβλίου
     */
    public Set<Author> getAuthors() {
        return new HashSet<Author>(this.friendAuthors());
    }

    /**
     * Προσθήκη ενός αντιτύπου ({@link Item}) στη συλλογή αντιτύπων του βιβλίου.
     * @param item Το αντίτυπο
     */
    public void addItem(Item item) {
        if (item != null) {
            item.setBook(this);
        }
    }

    /**
     * Απομάκρυνση ενός αντιτύπου ({@link Item}) από τη συλλογή αντιτύπων του βιβλίου.
     * @param item Το αντίτυπο
     */
    public void removeItem(Item item) {
        if (item != null) {
            item.setBook(null);
        }
    }

    /**
     * Μη ενθυλακωμένη συλλογή των αντιτύπων του βιβλίου.
     * @return Τα αντίτυπα του βιβλίου
     */
    Set<Item> friendItems() {
		if (items.isEmpty() && id != null && ServiceFactory.isContainerAvailable()) {
			ItemService itemService = ServiceFactory.getItemService();
			items.addAll(itemService.getItemsByBookno(id));
		}
		return items;
	}

    /**
     * Προσθήκη ενός συγγραφέα ({@link Author}) στους συγγραφείς του βιβλίου.
     * @param author Ο συγγραφέας
     */
    public void addAuthor(Author author) {
        if (author != null) {
            author.addBook(this);
        }
    }

    /**
     * Απομάκρυνση ενός συγγραφέα ({@link Author}) από τους συγγραφείς του βιβλίου.
     * @param author Ο συγγραφέας
     */
    public void removeAuthor(Author author) {
        if (author != null) {
            author.removeBook(this);
        }
    }

    /**
     * Μη ενθυλακωμένη συλλογή των συγγραφέων του βιβλίου.
     * @return Οι συγγραφείς του βιβλίου
     */
    Set<Author> friendAuthors() {
		if (authors.isEmpty() && !authorids.isEmpty() && ServiceFactory.isContainerAvailable()) {
			AuthorService authorService = ServiceFactory.getAuthorService();
			authors.addAll(authorService.getAuthorsByIds(authorids));
		}
		return authors;
	}
    
    public boolean equals(Object other) {
        if ( other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (! (other instanceof Book)) {
            return false;
        }
        
        Book theBook = (Book) other;
        if (! (getIsbn() == null ? theBook.getIsbn()
                == null : getIsbn().equals(theBook.getIsbn()))) {
            return false;
        }   
        
        return true;
    }
    
    public int hashCode() {
        return isbn == null ? 0 : isbn.hashCode();
    }

	private final class SyncingAuthorSet extends AbstractSet<Author> {
		private final Set<Author> delegate;

		SyncingAuthorSet() {
			this.delegate = new HashSet<>();
		}

		@Override
		public int size() {
			return delegate.size();
		}

		@Override
		public boolean contains(Object o) {
			return delegate.contains(o);
		}

		@Override
		public Iterator<Author> iterator() {
			Iterator<Author> it = delegate.iterator();
			return new Iterator<Author>() {
				Author current;

				@Override
				public boolean hasNext() {
					return it.hasNext();
				}

				@Override
				public Author next() {
					current = it.next();
					return current;
				}

				@Override
				public void remove() {
					it.remove();
					if (current != null && current.getId() != null) {
						authorids.remove(current.getId());
					}
				}
			};
		}

		@Override
		public boolean add(Author e) {
			if (e == null) {
				return false;
			}
			boolean changed = delegate.add(e);
			if (changed && e.getId() != null) {
				authorids.add(e.getId());
			}
			return changed;
		}

		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Author)) {
				return false;
			}
			Author e = (Author) o;
			boolean changed = delegate.remove(o);
			if (changed && e.getId() != null) {
				authorids.remove(e.getId());
			}
			return changed;
		}

		@Override
		public void clear() {
			delegate.clear();
			authorids.clear();
		}

		@Override
		public boolean addAll(Collection<? extends Author> c) {
			boolean changed = false;
			for (Author e : c) {
				changed |= add(e);
			}
			return changed;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			boolean changed = false;
			for (Object o : c) {
				changed |= remove(o);
			}
			return changed;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			boolean changed = false;
			Iterator<Author> it = iterator();
			while (it.hasNext()) {
				Author e = it.next();
				if (!c.contains(e)) {
					it.remove();
					changed = true;
				}
			}
			return changed;
		}
	}

	@PrePersist
	@PreUpdate
	private void syncUnmappedForeignKeys() {
		for (Author e : this.authors) {
			if (e.getId() != null) {
				this.authorids.add(e.getId());
			}
		}
	}
 }