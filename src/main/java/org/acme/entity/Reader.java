package org.acme.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Reader extends PanacheEntity{
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    public String name;

    @NotBlank(message = "email is required")
    @Size(min = 3, max = 50, message = "email must be between 3 and 50 characters")
    @Email(message = "email must be a valid email")
    public String email;

    @Size(min = 11, max = 11, message = "CPF must be 11 characters")
    @NotBlank(message = "CPF is required")
    public String CPF;

    public Boolean active = true;
}
