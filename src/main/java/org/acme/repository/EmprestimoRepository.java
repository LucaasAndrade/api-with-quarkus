package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entitys.Emprestimo;

@ApplicationScoped
public class EmprestimoRepository implements PanacheRepository<Emprestimo> {
}
