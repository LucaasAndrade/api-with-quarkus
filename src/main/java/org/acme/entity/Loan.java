package org.acme.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.DefaultValue;
import org.acme.enums.StatusEnum;
import jakarta.persistence.Entity;

import java.util.Date;

@Entity
public class Loan extends PanacheEntity {

    @OneToOne
    @JoinColumn(name = "book_id")
    @Size(min = 3, max = 50, message = "book is required")
    @NotBlank(message = "book is required")
    public Book book;

    @OneToOne
    @JoinColumn(name = "reader_id")
    @NotBlank(message = "reader is required")
    @Size(min = 3, max = 50, message = "reader is required")
    public Reader reader;

    public StatusEnum statusEnum = StatusEnum.ACTIVE;

    public String orderDate = new Date().toString();
}
