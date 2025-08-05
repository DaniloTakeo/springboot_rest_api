# 🗄️ Estrutura do Banco de Dados

Este documento descreve o schema relacional utilizado pela API, com as tabelas e seus respectivos campos, chaves primárias e estrangeiras.

---

## 📍 Tabelas

### 🧑‍⚕️ Tabela: `pacientes`

| Campo            | Tipo           | Restrições                 |
|------------------|----------------|-----------------------------|
| `id`             | BIGINT         | PK, Auto Increment          |
| `nome`           | VARCHAR(255)   | NOT NULL                    |
| `email`          | VARCHAR(255)   | NOT NULL                    |
| `cpf`            | VARCHAR(20)    | NOT NULL                    |
| `telefone`       | VARCHAR(20)    | -                           |
| `data_nascimento`| DATE           | -                           |
| `ativo`          | BOOLEAN        | NOT NULL                    |

---

### 👨‍⚕️ Tabela: `medicos`

| Campo           | Tipo           | Restrições                 |
|-----------------|----------------|-----------------------------|
| `id`            | BIGINT         | PK, Auto Increment          |
| `nome`          | VARCHAR(255)   | NOT NULL                    |
| `crm`           | VARCHAR(20)    | NOT NULL                    |
| `especialidade` | VARCHAR(255)   | NOT NULL                    |
| `email`         | VARCHAR(255)   | -                           |
| `telefone`      | VARCHAR(20)    | -                           |
| `ativo`         | BOOLEAN        | NOT NULL                    |

---

### 📅 Tabela: `consultas`

| Campo               | Tipo           | Restrições                               |
|---------------------|----------------|-------------------------------------------|
| `id`                | BIGINT         | PK, Auto Increment                        |
| `paciente_id`       | BIGINT         | FK → `pacientes(id)`, NOT NULL           |
| `medico_id`         | BIGINT         | FK → `medicos(id)`, NOT NULL             |
| `data_hora`         | DATETIME       | NOT NULL                                  |
| `motivo_cancelamento`| VARCHAR(255) | -                                         |
| `status`            | VARCHAR(50)    | NOT NULL                                  |

---

### 🔐 Tabela: `usuarios`

| Campo     | Tipo          | Restrições              |
|-----------|---------------|--------------------------|
| `id`      | BIGINT        | PK, Auto Increment       |
| `login`   | VARCHAR(100)  | NOT NULL, UNIQUE         |
| `senha`   | VARCHAR(255)  | NOT NULL                 |
| `role`    | VARCHAR(50)   | NOT NULL                 |

### 🔁 Tabela: refresh_tokens

| Campo     | Tipo          | Restrições              |
|-----------|---------------|--------------------------|
| `id`      | BIGINT        | PK, Auto Increment       |
| `usuario_id` | BIGINT     | FK → usuarios(id), NOT NULL |
| `token`   | VARCHAR(255)  | NOT NULL, UNIQUE         |
| `data_expiracao` | DATETIME | NOT NULL               |

---

## 🔗 Relacionamentos

- `consultas.paciente_id` → 🔗 `pacientes.id`
- `consultas.medico_id` → 🔗 `medicos.id`
- `refresh_tokens.usuario_id` → 🔗 `usuarios.id`

> As relações são do tipo N:1 (muitas consultas para um paciente/médico).

---

## 📌 Observações

- As tabelas seguem o padrão de nomes em **português** com convenções SQL comuns.
- Todos os IDs são do tipo `BIGINT` com auto incremento.
- Os campos `ativo` permitem controle lógico de entidades (soft delete).
