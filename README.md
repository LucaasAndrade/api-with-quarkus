# Railway Java Quarkus API

Esta API foi criada com **Quarkus** para gerenciar leitores, emprestimos e livros em um sistema de biblioteca. A aplicação usa **Hibernate ORM** para persistência de dados com um banco de dados em memória **H2**.

## Tecnologias Utilizadas

- **Quarkus**: Framework Java para microserviços.
- **JAX-RS**: API para criação de serviços RESTful.
- **Hibernate ORM**: Para mapeamento objeto-relacional.
- **H2**: Banco de dados em memória para desenvolvimento.
- **Jakarta Persistence (JPA)**: Para gerenciamento de entidades e interações com o banco de dados.
- **Jackson**: Para serialização e desserialização de objetos JSON.

## Requisitos- **Maven** ou **Gradle** para compilação e execução.


- **Java 17** ou superior.

## Como Executar o Projeto

### 1. Clonando o Repositório

Clone o repositório para sua máquina local:

```bash
git clone https://github.com/seu-usuario/railway-java-quarkus.git
```

## 2. Instalando as dependências:
```bash
./mvnw clean install
```

## 3. Executando a aplicação:
```bash
./mvnw quarkus:dev
```

## 3. ENDPOINTS da API:

Visualizar no Swagger: http://localhost:8080/q/swagger-ui/

### Descrição das Novidades na Versão **v2** da API
A versão **v2** da API introduziu diversas melhorias importantes em relação à segurança, controle de acesso, idempotência e novas funcionalidades nos endpoints de emprestimos e leitores. Abaixo está a explicação detalhada sobre essas adições:

### 1. **Configuração de CORS (Cross-Origin Resource Sharing)**
A nova versão da API suporta **CORS**, permitindo solicitações a partir de origens diferentes, como frontends hospedados em diferentes domínios/portas.
- Agora, o CORS está configurado para habilitar origens específicas, como `http://localhost:8000`.
- Permite os seguintes métodos HTTP: `GET`, `POST`, `PUT`, `DELETE`, e os **headers** esperados `Content-Type`, `Authorization`, `Idempotency-Key`.
- Exposição de **headers customizados**, como `X-Custom-Header`.

**Por que é importante?**
- Garante que aplicações externas tenham autorização para se comunicar com a API, respeitando modelos de segurança.
- Facilita o desenvolvimento em ambientes onde o leitor (frontend) e o servidor (backend) rodam em domínios diferentes.

**Exemplo de Configuração no :`CorsFilter`**
``` java
if (origin != null && origin.equals("http://localhost:8000")) {
    responseContext.getHeaders().add("Access-Control-Allow-Origin", origin);
    responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
    responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization,Idempotency-Key");
    responseContext.getHeaders().add("Access-Control-Expose-Headers", "X-Custom-Header");
    responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
}
```

### 2. **Rate Limiting (Limitação de Requisições por Leitor)**
Foi implementado controle de taxa de requisições por leitor (rate limiting) com base no endereço IP para evitar abuso da API.
- Para cada endereço IP, é permitido um máximo de **2 requisições por minuto**. Se ultrapassado, a resposta retorna com o **status 429 (Too Many Requests)**.

**Como funciona?**
- A lógica está controlada por uma estrutura `Map<String, ClientRequestInfo>`, vinculando cada leitor (IP) ao número de requisições feitas.
- Quando o limite é excedido, a API bloqueia temporariamente novas requisições desse leitor por 1 minuto.

**Por que é importante?**
- Garante que a API seja protegida contra abusos, como ataques de força bruta ou sobrecarga de requisições.
- Melhora a estabilidade e performance do sistema.

**Exemplo de Mensagem de Resposta:**
``` json
{
    "message": "Limite de requisições excedido. Tente novamente mais tarde."
}
```

### 3. **Idempotency Key**
A versão **v2** implementa suporte ao cabeçalho **`Idempotency-Key`** para operações POST. Isso ajuda a evitar processamentos duplicados em endpoints que criam entidades, como o registro de leitores ou emprestimos.
- O cabeçalho `Idempotency-Key` é obrigatório para todas as requisições POST.
- O sistema verifica se a chave **`Idempotency-Key`** já foi utilizada.
- Se a chave já foi consumida, a API retorna o registro associado à chave, ao invés de criar duplicados.

**Por que é importante?**
- Resolve o problema de requisições duplicadas causadas por falhas no leitor ou redes instáveis.
- Melhora a experiência do usuário, confirmando a criação do registro sem gerar redundância no banco.

**Exemplo de Resposta para ID Já Criado:** Se o leitor envia um **Idempotency-Key** já usado:
``` json
{
    "message": "Leitor já existe com a idempotencyKey informada"
}
```

**Exemplo de Validação no Código:**
``` java
if (idempotencyKey == null || idempotencyKey.isEmpty()) {
    return Response.status(Response.Status.BAD_REQUEST)
        .entity("Idempotency-Key é obrigatório").build();
}

// Verificar se existe um leitor com a mesma chave
Leitor leitorExistente = leitorRepository.find("idempotencyKey", idempotencyKey).firstResult();
if (leitorExistente != null) {
    return Response.status(Response.Status.OK)
        .entity("Leitor já existe com a idempotencyKey informada").build();
}
```

### 4. **Campo Quantidade em Livros**
A nova versão da API também introduziu suporte ao controle de **quantidade** nos livros de um emprestimo. Com isso, agora é possível:
- Especificar a quantidade de cada livro no momento de criar ou atualizar emprestimos.
- Calcular o **valor total do emprestimo** com base nos preços individuais dos livros ajustados pela quantidade.

**Por que é importante?**
- Garante maior flexibilidade no uso da API de emprestimos.
- Melhora os recursos de cálculo no sistema, permitindo a manipulação de múltiplos livros em diferentes quantidades.

**Exemplo de Requisição com Quantidade:**
``` json
{
    "descricao": "Emprestimo com quantidades",
    "leitor": {
        "id": 1
    },
    "livros": [
        {
            "id": 1,
            "quantidade": 2
        },
        {
            "id": 2,
            "quantidade": 3
        }
    ]
}
```

### 5. **Melhoria nos Endpoints**
A versão **v2** trouxe aprimoramentos importantes nos principais endpoints:
- **GET `/api/v2/emprestimos`**: Agora exibe os detalhes dos livros junto de suas quantidades.
- **POST `/api/v2/emprestimos`**: Calcula automaticamente o valor total com base nas quantidades.
- **PUT `/api/v2/emprestimos/{id}`**: Suporte para atualizar quantidades e recalcular valores.

### Exemplo de Fluxo na V2
1. **Criar Emprestimo (POST):** Requisição:
``` json
   {
       "descricao": "Novo emprestimo",
       "leitor": {
           "id": 1
       },
       "livros": [
           { "id": 1, "quantidade": 2 },
           { "id": 2, "quantidade": 5 }
       ]
   }
```
Resposta:
``` json
   {
       "id": 1,
       "descricao": "Novo emprestimo",
       "valor": 60.0,
       "livros": [
           { "id": 1, "nome": "Livro A", "preco": 10.0, "quantidade": 2 },
           { "id": 2, "nome": "Livro B", "preco": 5.0, "quantidade": 5 }
       ],
       "leitor": { "id": 1, "nome": "João" }
   }
```

2. **Requerer ID Duplicado (POST):** Requisição com **mesmo `Idempotency-Key`**
``` json
   {
       "descricao": "Novo emprestimo",
       "leitor": {
           "id": 1
       },
       "livros": [
           { "id": 1, "quantidade": 2 },
           { "id": 2, "quantidade": 5 }
       ],
       "Idempotency-Key": "abc123"
   }
```
Resposta:
``` json
   {
       "message": "Leitor já existe com a idempotencyKey informada"
   }
```

3. **Limite Excedido (Rate Limiting):** Após várias requisições:
``` json
   {
       "message": "Limite de requisições excedido. Tente novamente mais tarde."
   }
```
```