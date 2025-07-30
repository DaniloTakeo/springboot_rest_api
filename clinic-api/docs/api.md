# 📘 Documentação da API

Esta seção descreve como interagir com os endpoints da API REST da aplicação Clinic, cobrindo autenticação, cabeçalhos padrão e exemplos de uso por recurso.

---

## 🔐 Autenticação

A API utiliza **JWT** (JSON Web Token) para autenticação.

### 1. Login

**Endpoint:** `POST /auth`

**Body:**
```json
{
  "login": "admin",
  "senha": "admin123"
}
```

`Resposta:`

```
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6..."
}
```

- Guarde o token para incluir nas próximas requisições como Bearer Token.
 
---

## 📎 Cabeçalhos

Todas as requisições autenticadas devem conter:

```text
Authorization: Bearer <token>
Content-Type: application/json
```

## 🔍 Endpoints Principais

A seguir, os principais recursos expostos pela API REST da aplicação:

### 🚑 Pacientes

| Método | Endpoint           | Descrição               |
|--------|--------------------|--------------------------|
| GET    | `/pacientes`       | Lista todos os pacientes |
| GET    | `/pacientes/{id}`  | Busca paciente por ID    |
| POST   | `/pacientes`       | Cria um novo paciente    |
| PUT    | `/pacientes/{id}`  | Atualiza um paciente     |
| DELETE | `/pacientes/{id}`  | Inativa o paciente       |

**Exemplo de criação:**

```http
POST /pacientes
Authorization: Bearer <token>
Content-Type: application/json

{
  "nome": "Maria Souza",
  "cpf": "12345678901",
  "dataNascimento": "1990-01-01",
  "email": "maria@example.com"
}
```


### 🩺 Consultas

| Método | Endpoint             | Descrição                  | Autenticação |
|--------|----------------------|-----------------------------|--------------|
| GET    | `/consultas`         | Lista todas as consultas    | ✅ JWT       |
| GET    | `/consultas/{id}`    | Busca uma consulta por ID   | ✅ JWT       |
| POST   | `/consultas`         | Marca uma nova consulta     | ✅ JWT       |
| DELETE | `/consultas/{id}`    | Cancela uma consulta        | ✅ JWT       |

**Exemplo de criação:**

```http
POST /consultas
Authorization: Bearer <token>
Content-Type: application/json

{
  "pacienteId": 6,
  "medicoId": 4,
  "dataHora": "2025-05-09T16:00:00"
}
```

### 🧑‍⚕️ Médicos

| Método | Endpoint              | Descrição                    | Autenticação |
|--------|-----------------------|-------------------------------|--------------|
| GET    | `/medicos`            | Lista todos os médicos        | ✅ JWT       |
| GET    | `/medicos/{id}`       | Busca um médico por ID        | ✅ JWT       |
| POST   | `/medicos`            | Cria um novo médico           | ✅ JWT       |
| PUT    | `/medicos/{id}`       | Atualiza os dados do médico   | ✅ JWT       |
| DELETE | `/medicos/{id}`       | Inativa um médico             | ✅ JWT       |

**Exemplo de criação:**

```http
POST /medicos
Authorization: Bearer <token>
Content-Type: application/json

{
  "nome": "Dra. Vanessa Martins",
  "crm": "22334",
  "especialidade": "DERMATOLOGIA",
  "email": "vanessa.martins@gmail.com",
  "telefone": "+55 71 94433-8888",
  "ativo": true
}
```



