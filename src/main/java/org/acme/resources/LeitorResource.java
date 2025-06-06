package org.acme.resources;

import jakarta.inject.Inject;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.entitys.Leitor;
import org.acme.entitys.Emprestimo;
import org.acme.repository.LeitorRepository;
import jakarta.ws.rs.container.ContainerRequestContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/leitores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LeitorResource {

    private final Map<String, ClientRequestInfo> requestsPerClient = new ConcurrentHashMap<>();
    private final int maxRequestsPerMinute = 2; // Aqui você define o limite de requisições

    @OneToMany(mappedBy = "leitor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emprestimo> emprestimos = new ArrayList<>();

    @Inject
    LeitorRepository leitorRepository;

    @GET
    public List<Leitor> listarTodos() {
        return leitorRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Leitor buscarPorId(@PathParam("id") Long id) {
        return leitorRepository.findById(id);
    }

    @POST
    @Transactional
    public Response adicionar(Leitor leitor, @HeaderParam("Idempotency-Key") String idempotencyKey, @HeaderParam("X-Real-IP") String clientIp) {
        // Verificar se o IP foi fornecido (se não, obter diretamente do contexto)
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = "127.0.0.1";
        }

        // Verificar se o cliente já excedeu o limite de requisições
        if (!isAllowed(clientIp)) {
            return Response.status(Response.Status.TOO_MANY_REQUESTS)
                       .entity("Limite de requisições excedido. Tente novamente mais tarde.")
                       .build();
        }

        if (idempotencyKey == null || idempotencyKey.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Idempotency-Key é obrigatório").build();
        }

        // Verificar se já existe um cliente com a mesma Idempotency-Key
        Leitor leitorExistente = leitorRepository.find("idempotencyKey", idempotencyKey).firstResult();
        if (leitorExistente != null) {
            return Response.status(Response.Status.OK)
                .entity("Leitor já existe com a idempotencyKey informada").build();
        }

        leitor.setIdempotencyKey(idempotencyKey);
        leitorRepository.persist(leitor);
        return Response.status(Response.Status.CREATED).entity(leitor).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Long id, Leitor leitorAtualizado) {
        Leitor leitor = leitorRepository.findById(id);
        if (leitor == null) {
            throw new NotFoundException("Leitor não encontrado");
        }

        leitor.setNome(leitorAtualizado.getNome());
        leitor.setEmail(leitorAtualizado.getEmail());

        return Response.ok(leitor).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        Leitor leitor = leitorRepository.findById(id);
        if (leitor == null) {
            throw new NotFoundException("Leitor não encontrado");
        }

        leitorRepository.delete(leitor);
        return Response.noContent().build();
    }

    private boolean isAllowed(String clientIp) {
        ClientRequestInfo clientRequestInfo = requestsPerClient.computeIfAbsent(clientIp, k -> new ClientRequestInfo());

        synchronized (clientRequestInfo) {
            // Verifica se passou um minuto desde a última requisição
            if (clientRequestInfo.getTimestamp().plusSeconds(60).isBefore(Instant.now())) {
                clientRequestInfo.resetCounter();
            }

            // Atualiza o contador e verifica se ainda está no limite
            if (clientRequestInfo.incrementAndGet() > maxRequestsPerMinute) {
                return false;
            }
        }

        return true;
    }

    private static class ClientRequestInfo {
        private Instant timestamp;
        private AtomicInteger counter;

        public ClientRequestInfo() {
            this.timestamp = Instant.now();
            this.counter = new AtomicInteger(0);
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public int incrementAndGet() {
            return counter.incrementAndGet();
        }

        public void resetCounter() {
            this.timestamp = Instant.now();
            this.counter.set(0);
        }
    }

private String getClientIp(ContainerRequestContext context) {
    return context.getHeaders().getFirst("X-Forwarded-For") != null
            ? context.getHeaders().getFirst("X-Forwarded-For")
            : "127.0.0.1"; // Default para testes
}
}