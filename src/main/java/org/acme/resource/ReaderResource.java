package org.acme.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.ReaderDTO;
import org.acme.entity.Reader;
import org.acme.service.ReaderService;

@Path("/reader")
@ApplicationScoped()
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReaderResource {

    @Inject
    ReaderService readerService;

    @POST
    public Response createReader(ReaderDTO dto) {
        try{
            Reader reader = readerService.createReader(dto);
            return Response.status(Response.Status.CREATED).entity(reader).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response list() {
     try {
         return Response.ok(Reader.listAll()).build();
     }catch (Exception e) {
         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
     }
    }

    @GET
    @Path("/{id}")
    public Response findOne(@PathParam("id") Long id) {
        Reader entity = Reader.findById(id);
        if (entity == null) {
            return Response.status(404).build();
        }
        return Response.ok(entity).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, ReaderDTO dto) {
        try {
            Reader reader = readerService.updateReader(id, dto);
            return Response.ok(reader).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Transactional
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
       try {
           Reader reader = Reader.findById(id);
           if (reader == null) {
               return Response.status(Response.Status.NOT_FOUND).build();
           }
           reader.delete();
           return Response.status(Response.Status.NO_CONTENT).build();
       }catch (Exception e) {
           return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
       }
    }
}
