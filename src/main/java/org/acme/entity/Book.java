package org.acme.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
public class Book extends PanacheEntity {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    public String title;

    @NotBlank(message = "price is required")
    @Size(min = 1, max = 10, message = "price must be between 1 and 10 characters")
    @Positive(message = "price must be positive")
    public Double price;

    @ManyToOne
    @JoinColumn(name =  "author_id")
    @JsonIgnoreProperties("books")
    @NotBlank(message = "author is required")
    @Size(min = 3, max = 50, message = "author must be between 3 and 50 characters")
    public Author author;
}
