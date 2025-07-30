# 📊 Observabilidade e Monitoramento

Esta API possui suporte à observabilidade através da integração com ferramentas populares para logs, métricas e rastreamento.

---

## 📈 Métricas com Prometheus + Grafana

A API expõe métricas no formato Prometheus através do actuator do Spring Boot:

- Endpoint: `http://localhost:8080/actuator/prometheus`

### Integração com Grafana

1. Configure o Prometheus como data source no Grafana.
2. Importe dashboards prontos ou crie os seus com base nas métricas expostas, como:
   - Uso de CPU/memória
   - Taxa de requisições
   - Tempo de resposta por endpoint

> As métricas seguem o padrão do Micrometer com integração ao Spring Boot Actuator.

---

## 📃 Logs com Loki + Grafana

O projeto pode ser configurado para enviar logs estruturados para o Loki usando um `promtail` como agente.

### Exemplo de Configuração

1. Configure o **Logback** para gerar logs no formato JSON.
2. Configure o `promtail` para ler os arquivos de log e enviá-los ao Loki.
3. No Grafana, configure o Loki como data source para visualizar e filtrar logs.

---

## 🧭 Health Check com Actuator

A aplicação expõe endpoints de saúde para monitoramento externo:

- `/actuator/health` – Verifica o status geral da aplicação.
- `/actuator/info` – Exibe informações customizadas da aplicação (configurável).

---

## 🔎 Possibilidades de Extensão

- [ ] Adicionar **tracing distribuído** com OpenTelemetry + Jaeger/Zipkin.
- [ ] Integrar com alertas via Slack, Discord, Email ou PagerDuty.
- [ ] Criar dashboards customizados por domínio de negócio (Consultas, Médicos etc).

---

## 📦 Stack de Monitoramento Recomendado (Exemplo com Docker Compose)

```yaml
services:
  prometheus:
    image: prom/prometheus
    ports:
      - "9090:9090"

  grafana:
    image: grafana/grafana
    ports:
      - "3000:3000"

  loki:
    image: grafana/loki

  promtail:
    image: grafana/promtail
```