package guiaalvarez.biblioteca.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String title;
    private String author;
    private String category;
    private Integer quantity;

    public Book() {
    }

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<Borrow> borrow = new HashSet<>();
    //@JsonManagedReference(value = "book")
    @JsonIgnore
    public Set<Borrow> getIBorrow() {
        return borrow;
    }

    public Book(String title, String author, String category, Integer quantity) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void borrowedOne() {
        this.quantity--;
    }

    public void returnedOne() {
        this.quantity++;
    }
}
