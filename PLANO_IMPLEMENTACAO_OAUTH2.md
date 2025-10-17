# 📋 Plano de Implementação: Sistema de Autenticação OAuth2 com Google

## 🎯 Objetivo Geral

Implementar autenticação com Google OAuth2 para dois tipos de usuários:
- **Customer**: Pode visualizar produtos sem login, mas precisa autenticar para comprar
- **Admin**: Precisa estar sempre autenticado para acessar funcionalidades administrativas

---

## 📊 Fases da Implementação (com Testes)

### **FASE 1: Estrutura de Domínio 👥**
**Objetivo**: Criar a base sólida para usuários no domínio

**Passos:**
1. **Criar entidade `User`** no domínio com:
   - `id`, `email`, `name`, `role` (CUSTOMER/ADMIN)
   - `googleId`, `createdAt`, `updatedAt`
   - Validações básicas

2. **Definir enum `UserRole`**:
   - `CUSTOMER`, `ADMIN`

3. **Criar portas (interfaces)**:
   - `UserRepository` (para persistência)
   - `UserDomainService` (regras de negócio)

**Testes/Checkpoints:**
- ✅ Testes unitários da entidade User
- ✅ Testes do enum UserRole
- ✅ Testes das regras de negócio básicas

---

### **FASE 2: Camada de Dados 💾**
**Objetivo**: Implementar persistência e acesso aos dados

**Passos:**
1. **Criar entidade JPA** `UserEntity` com mapeamentos
2. **Implementar `UserRepository`** com Spring Data JPA
3. **Criar `UserJpaRepository`** (interface)
4. **Implementar `UserPersistenceAdapter`** (adapter out)

**Testes/Checkpoints:**
- ✅ Testes de integração com banco H2
- ✅ Testes do repository JPA
- ✅ Testes do adapter de persistência

---

### **FASE 3: Casos de Uso 🔧**
**Objetivo**: Implementar lógica de negócio

**Passos:**
1. **CreateUserUseCase**: Criar usuário a partir de dados do Google
2. **GetUserByGoogleIdUseCase**: Buscar usuário por ID do Google
3. **GetUserByEmailUseCase**: Buscar usuário por email
4. **UpdateUserRoleUseCase**: Alterar role do usuário (apenas admin)

**Testes/Checkpoints:**
- ✅ Testes unitários de cada use case
- ✅ Testes de integração entre use cases
- ✅ Testes de regras de negócio

---

### **FASE 4: Camada de Apresentação 🌐**
**Objetivo**: Criar endpoints para gerenciamento de usuários

**Passos:**
1. **Criar DTOs**:
   - `CreateUserRequest`, `UserResponse`
   - `LoginResponse`, `UserInfoResponse`

2. **Implementar `UserController`** com endpoints:
   - `GET /api/v1/users/me` - Perfil do usuário logado
   - `GET /api/v1/users/{id}` - Buscar usuário (admin only)
   - `PUT /api/v1/users/{id}/role` - Alterar role (admin only)

**Testes/Checkpoints:**
- ✅ Testes unitários do controller
- ✅ Testes de integração dos endpoints
- ✅ Testes de validação de DTOs

---

### **FASE 5: Configuração OAuth2 🔐**
**Objetivo**: Integrar com Google OAuth2

**Passos:**
1. **Atualizar `application.yml`** com configurações do Google:
   ```yaml
   spring:
     security:
       oauth2:
         client:
           registration:
             google:
               client-id: ${GOOGLE_CLIENT_ID}
               client-secret: ${GOOGLE_CLIENT_SECRET}
   ```

2. **Criar configuração OAuth2**:
   - Cliente Google Console setup
   - Configurar redirect URIs
   - Definir scopes necessários

**Testes/Checkpoints:**
- ✅ Testes de configuração OAuth2
- ✅ Testes de variáveis de ambiente
- ✅ Testes de integração com Google (mock)

---

### **FASE 6: Integração com Spring Security 🛡️**
**Objetivo**: Conectar autenticação com autorização

**Passos:**
1. **Criar `CustomUserDetailsService`**:
   - Implementar `loadUserByUsername`
   - Converter `User` do domínio para `UserDetails`

2. **Atualizar `SecurityConfig`**:
   - Configurar autenticação OAuth2
   - Definir regras de autorização por role
   - Proteger endpoints específicos

3. **Criar filtros personalizados** (se necessário):
   - `RoleAuthenticationFilter`

**Testes/Checkpoints:**
- ✅ Testes do CustomUserDetailsService
- ✅ Testes de configuração de segurança
- ✅ Testes de autenticação/autorização

---

### **FASE 7: Regras de Autorização 🚦**
**Objetivo**: Aplicar segurança nas rotas existentes

**Regras definidas:**
- **Rotas públicas**: `GET /api/v1/products` (visualização)
- **Rotas autenticadas**:
  - `POST /api/v1/products` (criar produto - admin only)
  - `PUT /api/v1/products/{id}` (admin only)
  - `DELETE /api/v1/products/{id}` (admin only)
- **Rotas futuras**: `POST /api/v1/orders` (customer authenticated)

**Testes/Checkpoints:**
- ✅ Testes de autorização por role
- ✅ Testes de proteção de endpoints
- ✅ Testes de acesso negado

---

### **FASE 8: Testes e Validação Final ✅**
**Objetivo**: Garantir que tudo funciona corretamente

**Passos:**
1. **Testes end-to-end** completos
2. **Testes manuais** com Google OAuth2
3. **Validação de segurança** em cada endpoint
4. **Testes de carga e performance**

---

## 🔄 Fluxo de Autenticação OAuth2

```
1. Usuário acessa página de produto
   ↓
2. Sistema mostra produtos (sem login)
   ↓
3. Usuário clica em "Comprar"
   ↓
4. Redireciona para Google OAuth2 (/oauth2/authorization/google)
   ↓
5. Usuário autentica no Google
   ↓
6. Google retorna com código de autorização
   ↓
7. Sistema troca código por access token
   ↓
8. Sistema busca/extrai informações do usuário do Google
   ↓
9. Sistema cria/busca usuário no banco local
   ↓
10. Sistema estabelece sessão autenticada
   ↓
11. Usuário é redirecionado para página de checkout
```

---

## 📋 Próximos Passos Sugeridos

**Para começar, sugiro:**
1. **Começar pela FASE 1** (estrutura de domínio) - mais isolada
2. **Depois FASE 2** (persistência) - testar criação básica de usuários
3. **Em paralelo, configurar conta Google** para OAuth2
4. **Então partir para FASE 5** (OAuth2 config) quando estrutura estiver sólida

---

## 🛠️ Tecnologias Envolvidas

- **Kotlin** + **Spring Boot 3.5.3**
- **Spring Security** com OAuth2 Client
- **Google OAuth2** (Google Identity Services)
- **H2 Database** (dev) / **PostgreSQL** (prod)
- **JUnit 5** + **MockK** para testes
- **Gradle Kotlin DSL** para build

---

## ⚠️ Considerações Importantes

1. **Segurança**: Sempre usar variáveis de ambiente para credenciais
2. **Testes**: Implementar testes em cada fase para validar funcionalidade
3. **Clean Architecture**: Manter separação clara entre domínio, casos de uso e adapters
4. **OAuth2 Flow**: Implementar corretamente o fluxo de autorização do Google
5. **Error Handling**: Tratar adequadamente erros de autenticação e autorização

---

## 📚 Recursos Úteis

- [Google OAuth2 Documentation](https://developers.google.com/identity/protocols/oauth2)
- [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

**✏️ Documento criado em:** 16 de outubro de 2025
**📝 Projeto:** FoodCart E-commerce API
**👨‍💻 Responsável:** Franklin Percicotte
