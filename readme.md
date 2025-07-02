# 📚 Booklyzer

Booklyzer é uma aplicação web desenvolvida com **Spring Boot**, que simula uma API REST protegida por autenticação JWT. O projeto conta com uma arquitetura limpa, integração com banco PostgreSQL, geração automática de documentação Swagger e mapeamentos com MapStruct.

---

## 🚀 Tecnologias Utilizadas

- ✅ Java 17
- ✅ Spring Boot 3.4.4
- ✅ Spring Security 6.2.5
- ✅ JPA + Hibernate
- ✅ PostgreSQL
- ✅ JWT (JSON Web Token)
- ✅ MapStruct 1.5.5
- ✅ SpringDoc OpenAPI
- ✅ Bean Validation
- ✅ Lombok
- ✅ Flyway (desabilitado por padrão)
- ✅ JUnit 5 + Mockito

---

## 📁 Estrutura do Projeto

```
booklyzer/
├── src/
│   ├── main/
│   │   ├── java/
│   │   ├── resources/
│   │   │   ├── application.properties
│   ├── test/
├── pom.xml
└── README.md
```

## 🛠️ Como Rodar o Projeto Localmente

1. **Clone o projeto:**

```bash
git clone https://github.com/seu-usuario/booklyzer.git
cd booklyzer
```

2. **Configure o banco PostgreSQL:**

- Crie um banco chamado `booklyzer` na porta `5432`.
- Altere o `application.properties` caso use outra porta, usuário ou senha.

3. **Execute o projeto com Maven:**

```bash
mvn clean install
mvn spring-boot:run
```

---

## 🌐 Endpoints e Documentação

- API Base: `http://localhost:8080/booklyzer`
- Swagger UI: `http://localhost:8080/booklyzer/swagger-ui-custom.html`

---

## 🔐 Autenticação JWT

A autenticação da API utiliza JWT. Para acessar rotas protegidas:

1. Faça login e receba um token.
2. Envie o token no header:

```http
Authorization: Bearer <seu_token>
```

O token expira em 24h (`jwt.expiration=86400000`).

---

## 🧪 Executar Testes

Para rodar os testes automatizados:

```bash
mvn test
```

---

## 📦 Empacotamento

O projeto gera um `.war` para deploy em servidores como Tomcat:

```bash
mvn clean package
```

O arquivo será gerado em:

```bash
target/booklyzer.war
```

---

## 📝 Contribuindo

Contribuições são muito bem-vindas! 💙  
Sinta-se livre para abrir **issues** e enviar **pull requests** com melhorias, correções ou novas funcionalidades.

---

## 👨‍💻 Autor

Desenvolvido com 💻 e ☕ por **Deivide Duarte**  
📬 Email: deivideduarte@outlook.com  
🔗 LinkedIn: [https://www.linkedin.com/in/deivide-duarte/](https://www.linkedin.com/in/deivide-duarte/)

---