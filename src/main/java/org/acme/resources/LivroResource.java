package org.acme.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.entitys.Livro;
import org.acme.repository.LivroRepository;

import java.util.List;

@Path("/livros")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LivroResource {

    @Inject
    LivroRepository livroRepository;

    @GET
    public List<Livro> listarTodos() {
        List<Livro> livros = livroRepository.listAll();
        return livros;
    }


    @GET
    @Path("/{id}")
    public Livro buscarPorId(@PathParam("id") Long id) {
        Livro livro = livroRepository.findById(id);
        if (livro == null) {
            throw new NotFoundException("Livro não encontrado");
        }
        return livro;
    }

    @POST
    @Transactional
    public Response salvar(Livro livro) {
        livroRepository.persist(livro);
        return Response.status(Response.Status.CREATED).entity(livro).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Long id, Livro livroAtualizado) {
        Livro livro = livroRepository.findById(id);
        if (livro == null) {
            throw new NotFoundException("Livro não encontrado");
        }

        livro.setNome(livroAtualizado.getNome());

        return Response.ok(livro).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        boolean deletado = livroRepository.deleteById(id);
        if (!deletado) {
            throw new NotFoundException("Livro não encontrado");
        }
        return Response.noContent().build();
    }
}
