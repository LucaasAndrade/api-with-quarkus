package org.acme.config;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        info = @Info(
                title = "SISTEMA DE BIBLIOTECA",
                version = "1.0.0",
                description = "API para gerenciamento de livros, autores e leitores em um sistema de biblioteca.",
                contact = @Contact(
                        name = "By Lucas Andrade",
                        url = "https://www.linkedin.com/in/lucaas-andrade/",
                        email = "lucas.masilva11@senacsp.edu.br"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        tags = {
                @Tag(name = "Book", description = "Operações relacionadas a livros"),
                @Tag(name = "Reader", description = "Operações de gerenciamento de leitores"),
                @Tag(name = "Order", description = "Operações relacionadas a pedidos/empréstimos"),
        }
)
public class OpenAPIConfig extends Application {
    // Esta classe é necessária para ativar a configuração do OpenAPI no Quarkus.
    // Não é necessário adicionar nenhum código aqui, pois as anotações acima já configuram o OpenAPI.

}