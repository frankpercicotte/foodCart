# 🛒 FoodCart - E-commerce Backend com Kotlin e Arquitetura Hexagonal

**FoodCart** é um projeto backend de e-commerce desenvolvido em Kotlin, com foco em aprendizado prático de arquitetura Hexagonal (Ports and Adapters) e Clean Architecture. O projeto faz parte do Plano de Desenvolvimento Individual (PDI) do autor, visando boas práticas, testes e simulações reais de serviços externos.

---

## 🔧 Arquitetura

O projeto adota a estrutura baseada em Hexagonal Architecture (Ports & Adapters):

```
com.foodcart
├── core
│   ├── domain          # Entidades, value objects e interfaces (ports)
│   └── usecase         # Casos de uso (application layer)
├── adapters
│   ├── in
│   │   └── web         # Entrada: Controllers REST
│   └── out
│       ├── persistence # Saída: Repositórios, JPA, MongoDB
│       └── external    # Integrações: e-mails, pagamentos, NF-e
├── config              # Beans, segurança, Keycloak
└── FoodCartApplication.kt
```

---

## 🎯 Objetivos

- Aplicar Clean Architecture com separação clara de responsabilidades
- Simular fluxo completo de e-commerce (compra, estoque, pagamento)
- Integrar serviços externos simulados (pagamento, nota fiscal, entrega)
- Utilizar bancos relacional e NoSQL (PostgreSQL e MongoDB)
- Autenticar usuários com Keycloak (OAuth2/OpenID)
- Adotar testes automatizados desde o início

---

## 🚀 Tecnologias

- Kotlin + Spring Boot
- Gradle Kotlin DSL
- PostgreSQL + MongoDB
- Keycloak
- Testcontainers + MockK
- Docker + Docker Compose
- OpenAPI (Swagger)
- Arquitetura Hexagonal

---

## 📦 Casos de uso planejados

- Cadastro e visualização de produtos
- Fechamento de pedido com cupom de desconto
- Pagamento (simulado)
- Emissão de nota fiscal (simulada)
- Baixa de estoque e envio para transportadora (simulado)
- Comunicação por e-mail em cada etapa

---

## 🛠️ Como rodar

**Pré-requisitos:**

- Docker e Docker Compose
- JDK 17+
- IntelliJ IDEA (recomendado)

**Comandos:**
```bash
./gradlew clean build
docker-compose up
```

---

## 📁 Estrutura futura

O projeto está em fase de construção. O planejamento completo e roadmap estão disponíveis no Notion e no README principal.

---

## 👨‍💻 Autor

Franklin Percicotte – Projeto de PDI

---

## 📄 Licença

MIT – uso livre para fins de aprendizado e evolução profissional.
