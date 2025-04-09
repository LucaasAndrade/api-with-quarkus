package org.acme.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Author extends PanacheEntity {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    public String name;

    @NotBlank(message = "nacionality is required")
    @Size(min = 3, max = 50, message = "nacionality must be between 3 and 50 characters")
    public String nacionality;

    @NotBlank(message = "birthDate is required")
    @Size(min = 10, max = 10, message = "birthDate must be in the format YYYY-MM-DD")
    public String birthDate;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("author")
    public List<Book> books = new ArrayList<>();
}
