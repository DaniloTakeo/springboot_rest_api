# 🚀 Deploy da Aplicação

Este documento descreve como subir a aplicação localmente com Docker Compose, além de apontar direções para ambientes de produção (com ou sem orquestração como Kubernetes).

---

## 🧩 Estrutura de Componentes

A aplicação é composta pelos seguintes serviços:

- `clinic-api`: API REST em Java (Spring Boot)
- `mysql`: Banco de dados relacional
- `adminer` (opcional): Interface de administração do banco

---

## 🐳 Deploy com Docker Compose (Local)

### Pré-requisitos

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

### 1. Subindo os serviços

Execute o seguinte comando na raiz do projeto:

```bash
docker-compose up --build
```

#### Isso irá:

- Criar os containers
- Executar a aplicação
- Subir o banco de dados MySQL
- Criar as tabelas via migrations (Flyway)

### 2. Verificando os serviços

- API: http://localhost:8080
- Adminer: http://localhost:8081 (usuário: root, senha: root, banco: clinic)

### 3. Finalizando

- Para parar os containers:

```bash
docker-compose down
```

---

## 🛠️ Deploy Manual (sem Docker)

Caso deseje rodar localmente com MySQL já instalado:
1. Crie o banco clinic no seu MySQL
2. Configure application.yml com as credenciais corretas
3. Execute:

```bash
./mvnw spring-boot:run
```

---

## ☁️ Considerações para Produção

- Utilizar variáveis de ambiente para configuração sensível
- HTTPS via certificado SSL (Spring Security + configuração de server.ssl)
- Banco de dados externo gerenciado (RDS, Cloud SQL etc.)
- Monitoramento com Prometheus, Grafana e/ou Actuator
- Logs enviados para Stackdriver, ELK ou Sentry

---

## 📦 Pipeline CI/CD

- O projeto possui integração contínua (CI) com GitHub Actions
- Também pode ser implantado em ambientes locais com Jenkins + Docker
- Ver ci/ ou Jenkinsfile na raiz (se configurado)
