package org.acme.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.entitys.Emprestimo;
import org.acme.entitys.Leitor;
import org.acme.entitys.Livro;
import org.acme.repository.LeitorRepository;
import org.acme.repository.EmprestimoRepository;
import org.acme.repository.LivroRepository;

import java.util.ArrayList;
import java.util.List;

@Path("/api/v2/emprestimos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmprestimoResourceV2 {

    @Inject
    EmprestimoRepository emprestimoRepository;

    @Inject
    LeitorRepository leitorRepository;

    @Inject
    LivroRepository livroRepository;

    @GET
    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Emprestimo buscarPorId(@PathParam("id") Long id) {
        return emprestimoRepository.findById(id);
    }

    @POST
    @Transactional
    public Response salvar(Emprestimo emprestimo) {
        // Buscar cliente pelo ID
        Leitor leitor = leitorRepository.findById(emprestimo.getLeitor().getId());
        if (leitor == null) {
            throw new NotFoundException("Leitor não encontrado");
        }

        // Buscar livros pelo ID
        List<Livro> livros = new ArrayList<>();
        for (Livro livroId : emprestimo.getLivros()) {
            Livro livro = livroRepository.findById(livroId.getId());
            if (livro == null) {
                throw new NotFoundException("Livro com ID " + livroId + " não encontrado");
            }
            livros.add(livro);
        }


        emprestimo.setLeitor(leitor);
        emprestimo.setLivros(livros);

        emprestimoRepository.persist(emprestimo);

        // Retornar resposta
        return Response.status(Response.Status.CREATED).entity(emprestimo).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Long id, Emprestimo emprestimoAtualizado) {
        Emprestimo emprestimo = emprestimoRepository.findById(id);
        if (emprestimo == null) {
            throw new NotFoundException("Emprestimo não encontrado");
        }

        emprestimo.setDescricao(emprestimoAtualizado.getDescricao());
        emprestimo.setLeitor(emprestimoAtualizado.getLeitor());

        return Response.ok(emprestimo).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        boolean deletado = emprestimoRepository.deleteById(id);
        if (!deletado) {
            throw new NotFoundException("Emprestimo não encontrado");
        }
        return Response.noContent().build();
    }
}