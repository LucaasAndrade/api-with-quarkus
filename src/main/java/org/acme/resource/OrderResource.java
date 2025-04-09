package org.acme.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.RollbackException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.OrderDTO;
import org.acme.entity.Loan;
import org.acme.service.OrderService;

@Path("/order")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    OrderService orderService;

    @POST
    public Response createOrder(OrderDTO dto) {
        try {
            Loan order = orderService.createOrder(dto);
            return Response.status(Response.Status.CREATED).entity(order).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (RollbackException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Transaction failed").build();
        }
    }

    @PATCH
    @Path("/{id}")
    public Response updateStatus(@PathParam("id") Long id, @QueryParam("status") String status) {
        try {
            Loan order = orderService.updateStaus(id, status);
            return Response.ok(order).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (RollbackException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Transaction failed").build();
        }
    }
}
