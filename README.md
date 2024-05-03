## VotaçãoApp

VotaçãoApp é uma aplicação de votação que permite a criação de novos usuários votantes e administradores, além da criação de pautas e sessões de votação.

### Objetivo
O objetivo principal da VotaçãoApp é fornecer uma plataforma para administradores gerenciarem pautas e sessões de votação, enquanto os usuários podem votar nas pautas e explorar as pautas ativas.

### Funcionalidades Principais
- Administração de Usuários: Administradores podem criar novos usuários votantes e outros administradores.
- Gestão de Pautas: Administradores podem criar pautas para votação.
- Sessões de Votação: Administradores podem abrir sessões de votação para as pautas criadas.
- Votação em Pautas: Tanto os usuários votantes quanto os administradores podem votar nas pautas abertas pelos administradores.
- Exploração de Pautas: Usuários podem visualizar as pautas ativas.

### Tecnologias Utilizadas
- Spring Boot
- Spring Data JPA
- Spring Validation
- Spring Web
- Spring Security
- Java JWT
- PostgreSQL
- H2 Database (runtime)
- Flyway Database Migrations
- Springdoc OpenAPI
- Lombok
- Spring Boot DevTools
- JUnit


### Como Usar
1. **Instalação:** Clone este repositório e instale as dependências necessárias.
2. **Configuração:** Crie um arquivo `.env` baseado no arquivo `.env.example` fornecido. Nele, insira as seguintes propriedades:
    ```
    DB_URL= Aqui deve ser inserida a URL de conexão com o banco de dados PostgreSQL que a aplicação poderá utilizar
    DB_USERNAME= Insira o username do seu banco de dados PostgreSQL
    DB_PASSWORD= Insira a senha do seu banco de dados PostgreSQL
    JWT_SECRET= Insira uma senha secreta para a geração de JWT na aplicação
    ```
3. **Execução:** Inicie a aplicação utilizando o Gradle:
    ```bash
    ./gradlew bootRun
    ```

### Conta Admin Padrão
A aplicação já possui uma conta admin cadastrada com os seguintes dados de acesso:
- **Email:** admin@email.com
- **Senha:** admin123


### Documentação da API
- Para explorar a documentação da API construída com Swagger, execute a aplicação e acesse: [Swagger UI](http://localhost:8080/swagger-ui/index.html#/)
  

## Endpoints

### Autenticação

#### POST /auth/login
- Rota não autenticada, utilizada para autenticar um usuário na aplicação.

#### POST /auth/votoExterno
-Rota não autenticada, utilizada para validar se um usuário cadastrado pode inserir um voto de forma externa.

### Usuário

#### POST /usuario
- Rota acessada somente por administradores, permite criar novos usuários, sejam eles administradores ou não.

#### GET /usuario/usuarioLogado
- Rota autenticada, permite buscar o usuário que está atualmente logado na aplicação.

#### GET /usuario/existe
- Rota não autenticada, permite verificar se um usuário com o CPF fornecido já existe na base de dados.

### Pauta

#### POST /pauta
- Rota acessada somente por administradores, permite criar uma nova pauta sem sessão de votação.

#### GET /pauta/usuarioLogado
- Rota acessada somente por administradores, permite listar todas as pautas que foram criadas pelo usuário logado na aplicação.

#### GET /pauta/ativas
- Rota autenticada, permite listar todas as pautas que estão com sessão de votação aberta.

#### GET /pauta/{id}
- Rota não autenticada, permite buscar uma sessão ativa pelo id.

#### GET /pauta/detalhes/{id}
- Rota acessada somente por administradores, permite verificar detalhes a respeito de uma pauta que possui ou possuiu sessão de votação aberta.

### Votação

#### POST /votacao/abrir
- Rota acessada somente por administradores, permite abrir sessão de votação em uma pauta.

#### PATCH /votacao/votoInterno
- Rota autenticada, permite inserir um voto em uma pauta.

#### PATCH /votacao/votoExterno
- Rota não autenticada, permite inserir um voto sem precisar estar autenticado na aplicação.


