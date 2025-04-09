# Projeto de API com Quarkus
Este projeto é uma API desenvolvida com Quarkus que gerencia usuários, livros, leitores e pedidos.

## Entidades
Users
Representa os usuários do sistema.

ID: Identificador único do usuário.
Name: Nome do usuário.
Email: Email do usuário.
Password: Senha do usuário.
Books
Representa os livros disponíveis.

ID: Identificador único do livro.
Title: Título do livro.
Author: Autor do livro.
ISBN: Código ISBN do livro.
PublishedDate: Data de publicação do livro.
Readers
Representa os leitores que utilizam o sistema.

ID: Identificador único do leitor.
UserID: Identificador do usuário associado ao leitor.
MembershipDate: Data de adesão do leitor.
Orders
Representa os pedidos de livros feitos pelos leitores.

ID: Identificador único do pedido.
ReaderID: Identificador do leitor que fez o pedido.
BookID: Identificador do livro solicitado.
OrderDate: Data do pedido.
Status: Status do pedido (e.g., Pendente, Concluído).

### Como Executar
Navegue até o diretório do projeto:
cd seu-repositorio
Compile e execute o projeto:
./mvnw compile quarkus:dev
Endpoints
Users
GET /users: Lista todos os usuários.
POST /users: Cria um novo usuário.
GET /users/{id}: Obtém um usuário pelo ID.
PUT /users/{id}: Atualiza um usuário pelo ID.
DELETE /users/{id}: Deleta um usuário pelo ID.
Books
GET /books: Lista todos os livros.
POST /books: Cria um novo livro.
GET /books/{id}: Obtém um livro pelo ID.
PUT /books/{id}: Atualiza um livro pelo ID.
DELETE /books/{id}: Deleta um livro pelo ID.
Readers
GET /readers: Lista todos os leitores.
POST /readers: Cria um novo leitor.
GET /readers/{id}: Obtém um leitor pelo ID.
PUT /readers/{id}: Atualiza um leitor pelo ID.
DELETE /readers/{id}: Deleta um leitor pelo ID.
Orders
GET /orders: Lista todos os pedidos.
POST /orders: Cria um novo pedido.
GET /orders/{id}: Obtém um pedido pelo ID.
PUT /orders/{id}: Atualiza um pedido pelo ID.
DELETE /orders/{id}: Deleta um pedido pelo ID.
Contribuição
Faça um fork do projeto.
Crie uma nova branch:
git checkout -b minha-nova-feature
Faça suas alterações e commit:
git commit -m 'Adiciona nova feature'
Envie para a branch original:
git push origin minha-nova-feature
Crie um Pull Request.
Licença
Este projeto está licenciado sob a Licença MIT. Veja o arquivo LICENSE para mais detalhes.
# quarkus-projectapi-with-quarkus
