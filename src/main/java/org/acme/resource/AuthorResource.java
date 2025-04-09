package org.acme.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.AuthorDTO;
import org.acme.entity.Author;
import org.acme.service.AuthorService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/author")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Author", description = "Operações relacionadas a autores")
public class AuthorResource {
    @Inject
    AuthorService authorService;

    @Operation(summary = "Criar um novo autor", description = "Cria um novo autor no sistema.")
    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "201",
                        description = "Autor criado com sucesso.",
                        content = @Content(mediaType = "application/json")
                ),
                @APIResponse(
                        responseCode = "400",
                        description = "Erro de validação.",
                        content = @Content(mediaType = "application/json")
                )
            }
    )
    @POST
    public Response createAuthor(AuthorDTO dto) {
        try{
            Author author = authorService.createAuthor(dto);
            return Response.status(Response.Status.CREATED).entity(author).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Operation(summary = "Listar todos os autores", description = "Retorna uma lista de todos os autores cadastrados.")
    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "200",
                        description = "Lista de autores retornada com sucesso.",
                        content = @Content(mediaType = "application/json")
                ),
                @APIResponse(
                        responseCode = "500",
                        description = "Erro interno no servidor.",
                        content = @Content(mediaType = "application/json")
                )
            }
    )
    @GET
    public Response listAuthor(){
        try {
            return Response.ok(Author.listAll()).build();
        } catch (InternalServerErrorException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


    @Operation(summary = "Buscar autor por ID", description = "Retorna um autor específico com base no ID fornecido.")
    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "200",
                        description = "Autor encontrado com sucesso.",
                        content = @Content(mediaType = "application/json")
                ),
                @APIResponse(
                        responseCode = "404",
                        description = "Autor não encontrado.",
                        content = @Content(mediaType = "application/json")
                )
            }
    )
    @GET
    @Path("/{id}")
    public Response authorById(@PathParam("id") Long id) {
        try{
            Author entity = Author.findById(id);
            if (entity == null) {
            return Response.status(404).entity("Author not found.").build();
            }
            return Response.ok(entity).build();
        }catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Operation(summary = "Atualizar autor", description = "Atualiza as informações de um autor existente com base no ID fornecido.")
    @APIResponses(
    value = {
        @APIResponse(
                responseCode = "200",
                description = "Autor atualizado com sucesso.",
                content = @Content(mediaType = "application/json")
        ),
        @APIResponse(
                responseCode = "400",
                description = "Erro de validação.",
                content = @Content(mediaType = "application/json")
        ),
        @APIResponse(
                responseCode = "404",
                description = "Autor não encontrado.",
                content = @Content(mediaType = "application/json")
        )
    })
    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") Long id, AuthorDTO dto) {
        try{
            Author author = authorService.updateAuthor(id, dto);
            return Response.ok(author).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


    @Operation(summary = "Deletar autor", description = "Remove um autor do sistema com base no ID fornecido.")
    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "204",
                        description = "Autor deletado com sucesso.",
                        content = @Content(mediaType = "application/json")
                ),
                @APIResponse(
                        responseCode = "404",
                        description = "Autor não encontrado.",
                        content = @Content(mediaType = "application/json")
                )
            }
    )

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") Long id) {
        try{
            Author author = Author.findById(id);
            if (author == null) {
                return Response.status(404).entity("Author not found.").build();
            }
            author.delete();
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
