# 🧪 Testes Automatizados

O projeto adota uma abordagem mista de testes **unitários** e **de integração**, garantindo que tanto regras de negócio quanto integrações externas (como banco de dados e autenticação) sejam devidamente validadas.

---

## ✅ Tipos de Teste

### 1. Testes Unitários
- Foco na lógica de métodos isolados
- Utilizam mocks (com Mockito)
- Não dependem de infraestrutura externa (como banco de dados)

### 2. Testes de Integração
- Executam o ciclo completo da requisição (Controller → Service → Repository)
- Testam endpoints reais com `MockMvc` e banco via **Testcontainers**
- Utilizam uma instância real de **MySQL** isolada via container

---

## 🧰 Tecnologias e Anotações Usadas

| Recurso             | Descrição |
|---------------------|-----------|
| `@SpringBootTest`   | Inicia o contexto da aplicação completo |
| `@AutoConfigureMockMvc` | Habilita testes simulando requisições HTTP |
| `@Testcontainers`   | Cria containers temporários para dependências externas |
| `@Mock`, `@InjectMocks` | Utilizados em testes unitários com Mockito |
| `MockMvc`           | Utilitário para simular chamadas REST |
| `MySQLContainer`    | Container isolado para testes com banco real |

---

## 🧪 Execução dos Testes

```bash
./mvnw test
``` 

- Ou para executar apenas testes de uma classe específica:

```bash
./mvnw test -Dtest=PacienteServiceTest
```

---

## 📁 Organização dos Testes

```text
src/
└── test/
    └── java/
        └── com/
            └── danilotakeo/
                └── clinic/
                    ├── controller/    # Testes de endpoints (MockMvc)
                    ├── service/       # Testes unitários da lógica de negócio
                    └── integration/   # Testes de integração completa
```
---

## 💡 Boas práticas
- Separar testes unitários e de integração em pacotes distintos
- Testes de integração usam bancos reais via Testcontainers (evitam H2)
- Cobertura medida com ferramentas como JaCoCo e reportada no SonarCloud
- Nome dos métodos descreve o cenário (ex: shouldCreatePacienteSuccessfully())

---

## 📊 Cobertura

- A cobertura de testes é enviada para o SonarCloud a cada build contínuo via GitHub Actions ou Jenkins, e pode ser verificada em tempo real no painel do projeto.
- Também pode ser vista usando a insígnia do Codecov para onde o relatório do JaCoCo também é enviado via GitHub Actitons:

![Coverage](https://codecov.io/gh/DaniloTakeo/springboot_rest_api/branch/main/graph/badge.svg)