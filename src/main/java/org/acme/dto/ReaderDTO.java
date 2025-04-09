package org.acme.dto;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

public class ReaderDTO extends PanacheEntity{
    public String name;
    public String email;
    public String CPF;
}
