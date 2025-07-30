# 🔄 CI/CD - Integração e Entrega Contínuas

Este projeto utiliza dois pipelines de CI/CD:

- **GitHub Actions**: para automação de build, testes, análise estática e segurança.
- **Jenkins**: para integração com ambientes de deploy locais ou gerenciados via Docker/Kubernetes.

---

## 🚀 GitHub Actions

Os workflows estão localizados em:

```text
	.github/workflows/
	├── ci-tests.yml # Executa build e testes automatizados
	├── code-quality.yml # Integração com SonarCloud
	├── dependency-check.yml # Verificação de dependências vulneráveis (OWASP)
	├── sonar-cloud.yml # Alternativa de integração com o Sonar separada
```

---

### Etapas comuns:
- Ação disparada a cada `push` ou `pull request`
- Build com Maven (`./mvnw clean install`)
- Execução dos testes unitários e de integração
- Publicação de cobertura no SonarCloud
- Verificação de vulnerabilidades com OWASP Dependency Check

> Esses workflows são úteis para manter a qualidade e segurança contínua do código antes do deploy.

---

## 🔧 Jenkins

O pipeline está definido no arquivo:
- clinic-api/Jenkinsfile

---

### Etapas típicas:
1. **Checkout do código**
2. **Build com Maven**
3. **Execução dos testes**
4. **Análise de qualidade (opcional)**
5. **Deploy com Docker ou Kubernetes**

Você pode integrar o Jenkins com agentes rodando em sua infraestrutura, usando o `docker-compose.yml` ou os arquivos Kubernetes disponíveis na pasta `k8s/`.

---

## 📌 Boas práticas utilizadas

- **Separação de pipelines** por finalidade
- **Execução paralela** de testes e análise de código
- **Fail-fast**: falhas no teste ou análise quebram o build
- **Ambiente reprodutível** com Maven Wrapper e containers

---

## 🔐 Segurança

- OWASP Dependency Check escaneia bibliotecas conhecidas por vulnerabilidades
- GitHub Actions pode ser integrado com `Code Scanning Alerts` e `Dependabot`