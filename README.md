# 🏥 Clinic API - Spring Boot REST

Esta é uma API REST desenvolvida com Spring Boot para fins de estudo e prática de arquitetura em camadas, testes, CI/CD, observabilidade e deploy com Docker/Kubernetes.

---

## 🚀 Tecnologias

- Java 17 + Spring Boot
- Maven
- JUnit + Mockito + Testcontainers
- Docker + Docker Compose
- Kubernetes (YAMLs para k8s/)
- Jenkins + GitHub Actions (CI/CD)
- SonarCloud + OWASP Dependency Check
- Grafana + Loki (logs e observabilidade)
- MySQL (banco de dados)

---

## 🛠️ Como rodar localmente

```bash
cd clinic-api
./mvnw clean install
docker-compose -f docker-compose/docker-compose-ubuntu.yml up
```
- Para Windows, utilize docker-compose-windows.yml

---

## 📂 Documentação

- [Arquitetura da Aplicação](clinic-api/docs/architecture.md)
- [CI/CD (Jenkins e GitHub Actions)](clinic-api/docs/ci-cd.md)
- [Testes Automatizados](clinic-api/docs/testing.md)
- [Deploy com Docker/Kubernetes](clinic-api/docs/deployment.md)
- [Observabilidade (Grafana/Loki)](clinic-api/docs/monitoring.md)

---

## 👨‍💻 Autor
Danilo Takeo Kanizawa


