# 🛒 FoodCart - E-commerce Backend API

**FoodCart** é uma API RESTful para um sistema de e-commerce desenvolvida em **Kotlin** com **Spring Boot 3.5.3**, seguindo os princípios do **Clean Architecture**.

[![Kotlin](https://img.shields.io/badge/Kotlin-1.8+-blue.svg)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-6DB33F.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.3-02303A.svg)](https://gradle.org/)

## 🚀 Funcionalidades Principais

- **Gestão de Categorias**
  - Criação, listagem e consulta de categorias
  - Configuração de margem de lucro e desconto máximo por categoria

- **Gestão de Produtos**
  - Cadastro de produtos com preços dinâmicos baseados na categoria
  - Cálculo automático de preço final considerando margem de lucro
  - Aplicação de descontos respeitando limites por categoria

- **Arquitetura Limpa**
  - Separação clara entre domínio, casos de uso e adaptadores
  - Tratamento centralizado de exceções
  - Código testável e desacoplado

## 🛠️ Tecnologias

- **Linguagem**: Kotlin 1.8+
- **Framework**: Spring Boot 3.5.3
- **Banco de Dados**: H2 (desenvolvimento) / PostgreSQL (produção)
- **Build**: Gradle com Kotlin DSL
- **Testes**: JUnit 5, MockK, Testcontainers
- **Documentação**: OpenAPI (Swagger UI)

## 🏗️ Estrutura do Projeto

```
com.foodcart.ecommerce/
├── core/                  # Lógica de negócio independente de frameworks
│   ├── domain/            # Entidades e regras de negócio
│   └── usecase/           # Casos de uso da aplicação
├── adapters/              # Adaptadores para frameworks externos
│   ├── inbound/           # Controladores e DTOs
│   └── out/               # Implementações de portas (repositórios, clientes)
└── config/                # Configurações da aplicação
```

## 📚 Documentação da API

A documentação interativa da API está disponível em:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Convenções de Respostas de Erro

A API segue um padrão consistente para respostas de erro:

```json
{
  "code": "CODIGO_DO_ERRO",
  "message": "Mensagem descritiva do erro",
  "campo1": "valor do campo relacionado",
  "campo2": "valor adicional de contexto"
}
```

#### Códigos de Erro Comuns

| Código HTTP | Código do Erro                  | Descrição                                      |
|-------------|----------------------------------|------------------------------------------------|
| 400         | INVALID_CATEGORY_NAME           | Nome da categoria inválido                     |
| 400         | INVALID_PROFIT_MARGIN           | Margem de lucro inválida (deve ser ≥ 0)        |
| 400         | INVALID_DISCOUNT_PERCENTAGE     | Percentual de desconto inválido                |
| 400         | NEGATIVE_DISCOUNT_PERCENTAGE    | Percentual de desconto não pode ser negativo   |
| 404         | CATEGORY_NOT_FOUND              | Categoria não encontrada                       |
| 409         | PRODUCT_NAME_ALREADY_EXISTS     | Já existe um produto com este nome             |
| 422         | CALCULATION_ERROR               | Erro em cálculo de preço ou desconto           |
| 500         | INTERNAL_SERVER_ERROR           | Erro interno do servidor                       |

## 🚀 Como Executar

### Pré-requisitos

- JDK 17 ou superior
- Docker e Docker Compose (opcional, para banco de dados)

### Configuração do Ambiente

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/foodcart-ecommerce.git
   cd foodcart-ecommerce
   ```

2. Inicie os serviços dependentes (banco de dados):
   ```bash
   docker-compose up -d
   ```

3. Execute a aplicação:
   ```bash
   ./gradlew bootRun
   ```

4. Acesse a aplicação em: http://localhost:8080

### Executando Testes

```bash
# Executar todos os testes
./gradlew test

# Executar testes com cobertura
./gradlew test jacocoTestReport
```

## 📦 Endpoints Principais

### Categorias

- `GET /api/v1/categories` - Lista todas as categorias
- `GET /api/v1/categories/{id}` - Busca uma categoria por ID
- `POST /api/v1/categories` - Cria uma nova categoria
- `PUT /api/v1/categories/{id}` - Atualiza uma categoria existente

### Produtos

- `GET /api/v1/products` - Lista todos os produtos
- `POST /api/v1/products` - Cria um novo produto
- `GET /api/v1/products/{id}` - Busca um produto por ID

## 🧪 Testes

O projeto possui cobertura de testes abrangente, incluindo:

- Testes unitários para domínio e casos de uso
- Testes de integração para controladores
- Testes de aceitação com Testcontainers

Para executar os testes:

```bash
# Executar todos os testes
./gradlew test

# Executar testes com cobertura de código
./gradlew jacocoTestReport
```

## 🤝 Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e enviar pull requests.

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/amazing-feature`)
3. Commit suas alterações (`git commit -m 'Add some amazing feature'`)
4. Faça push para a branch (`git push origin feature/amazing-feature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

Desenvolvido com ❤️ por Franklin Percicotte como parte do Plano de Desenvolvimento Individual (PDI)
