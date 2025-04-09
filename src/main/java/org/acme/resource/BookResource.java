package org.acme.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.BookDTO;
import org.acme.entity.Book;
import org.acme.service.BookService;

import java.util.List;

@Path("/book")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    BookService bookService;

    @POST
    public Response createBook(BookDTO dto) {
        try{
            Book book = bookService.createBook(dto);
            return Response.status(Response.Status.CREATED).entity(book).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    public List<Book> list() {
        return Book.listAll();
    }

    @GET
    @Path("/{id}")
    public Response findOne(@PathParam("id") Long id) {
        Book entity = Book.findById(id);
        if (entity == null) {
            return Response.status(404).build();
        }
        return Response.ok(entity).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Book book) {
        Book entity = Book.findById(id);
        if (entity == null) {
            return Response.status(404).build();
        }
        entity.title = book.title;
        entity.author = book.author;
        entity.price = book.price;
        entity.persist();
        return Response.status(204).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Book entity = Book.findById(id);
        if (entity == null) {
            return Response.status(404).build();
        }
        entity.delete();
        return Response.status(204).build();
    }
}
