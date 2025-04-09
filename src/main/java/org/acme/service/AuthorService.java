package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.dto.AuthorDTO;
import org.acme.entity.Author;

@ApplicationScoped
public class AuthorService {
    @Transactional
    public Author createAuthor(AuthorDTO dto) {
        boolean exists = Author.find("name", dto.name).firstResultOptional().isPresent();
        if (exists) {
            throw new IllegalArgumentException("Author already exists");
        }

        Author author = new Author();
        author.name = dto.name;
        author.nacionality = dto.nacionality;
        author.birthDate = dto.birthDate;

        author.persist();
        return author;
    }

    @Transactional
    public Author updateAuthor(Long id, AuthorDTO dto) {
        Author author = Author.findById(id);
        if (author == null) {
            throw new IllegalArgumentException("Author not found");
        }
        author.name = dto.name;
        author.nacionality = dto.nacionality;
        author.birthDate = dto.birthDate;

        author.persist();
        return author;
    }
}
