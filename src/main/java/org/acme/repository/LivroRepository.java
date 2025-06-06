package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entitys.Livro;

@ApplicationScoped
public class LivroRepository implements PanacheRepository<Livro> {
    // Nada precisa ser implementado aqui manualmente
    // listAll(), findById(), persist(), delete() já são herdados de PanacheRepository
}
