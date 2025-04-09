package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.dto.BookDTO;
import org.acme.entity.Author;
import org.acme.entity.Book;

@ApplicationScoped
public class BookService {

    @Transactional
    public Book createBook(BookDTO dto) {
        Author author = Author.findById(dto.authorId);
        if(author == null) {
            throw new IllegalArgumentException("Author not found");
        }

        Book book = new Book();
        book.title = dto.title;
        book.price = dto.price;
        book.author = author;

        book.persist();
        return book;
    }
}
