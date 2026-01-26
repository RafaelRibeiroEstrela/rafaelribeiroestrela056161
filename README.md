Nome do participante: Rafael Ribeiro Estrela

N° Inscrição : 16428

Documentação:

# 🛠️ API – Desafio Seplag MT (Desenvolvedor Java Backend)

Este repositório contém a implementação da API desenvolvida para o desafio técnico do ** PROCESSO SELETIVO CONJUNTO Nº 001/2026/SEPLAG/SEFAZ/SEDUC/SESP/PJC/PMMT/CBMMT/DETRAN/POLITEC/SEJUS/SEMA/SEAF/SINFRA/SECITECI/PGE/MTPREV** para o cargo de **Engenheiro da Computação/SÊNIOR**.

---

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Framework** (Spring Boot, Spring Data)
- **PostgreSQL** – Banco de dados relacional
- **MinIO** – Armazenamento de arquivos (compatível com S3)
- **Docker & Docker Compose**
- Arquitetura **MVC**, com aplicação de **padrões de projeto** para melhor organização e manutenção

---

## 🐳 Preparando o Ambiente com Docker

Antes de subir os containers da aplicação, recomenda-se **limpar o ambiente Docker** para evitar conflitos com containers, volumes e redes antigas.

### 🔄 Limpeza do Docker (opcional, mas recomendada)

```bash
# Parar todos os containers em execução
docker stop $(docker ps -q)

# Remover todos os containers
docker rm $(docker ps -a -q)

# Remover todas as imagens
docker rmi -f $(docker images -q)

# Remover todos os volumes
docker volume rm $(docker volume ls -q)

# Remover todas as redes
docker network rm $(docker network ls -q)

# Limpeza completa e segura
docker system prune -a --volumes -f
```

---

## ▶️ Executando a Aplicação

### 1. Clone o repositório

```bash
git clone https://github.com/RafaelRibeiroEstrela/rafaelribeiroestrela056161.git
cd processo-seletivo-api
```

### 2. Suba os containers com Docker Compose

Na pasta principal do projeto, execute o comando:

```bash
docker compose -f "docker-compose.yml" up -d --build
```

O sistema irá subir os seguintes serviços:

- API Java Spring Boot
- Banco de dados PostgreSQL
- Servidor MinIO (acessível via browser)

---

## 🧪 Testando a API

A API expõe endpoints RESTful documentados via Swagger (ou Postman, se aplicável).

- Acesse a documentação:

```
http://localhost:26000/swagger-ui.html
```

- Utilize ferramentas como **Postman** ou **curl** para testar os endpoints.

---

## 🧩 Arquitetura & Padrões

- Separação clara entre camadas: **Controller**, **Service**, **Repository**
- Aplicação de princípios **SOLID**
- Uso de **DTOs** e mapeamentos com ModelMapper
- Tratamento de exceções com `@ControllerAdvice`
- Logs com **SLF4J**

---

## Observações:

Datas devem ser informadas no padrão: dd/MM/yyyy
Exemplo: 10/10/1990

## Itens atendidos:

A. Arquitetura e Estrutura

A1 - Organização e camadas - Projeto estruturado em camadas (controller, service, repository, model). ✅
A2 - Versionamento e documentação - Endpoints versionados e descritos via Swagger/OpenAPI. ✅
A3 - Migrations e README - Uso de Flyway e documentação com instruções de execução. ✅

B. Funcionalidades Técnicas

B1 - CRUD e endpoints REST - Implementação funcional dos verbos POST, PUT, GET, DELETE. - ✅
B2 - Paginação e Filtros - Consultas com ordenação e filtros de nome. - ✅
B3 - Upload/MinIO - Upload de arquivos e geração de presigned URLs. - ✅
B4 - Autenticação JWT - Implementação com expiração e renovação de token. - ✅
B5 - Segurança (CORS e Rate Limit) - Bloqueio de domínios externos e limite de requisições. - ✅
B6 - WebSocket e Sincronização (Sênior) - Notificações em tempo real e sincronização de regionais. - ✅
B7 - Health Checks / Liveness - Endpoints de verificação do serviço. - ✅

C. Boas Práticas e Qualidade

C1 - Clean Code e legibilidade - Código limpo, nomeações adequadas e separação de responsabilidades. - ✅
C2 - Testes unitários e integração - Cobertura mínima de testes nos módulos principais. - ✅
C3 - Commits e versionamento - Histórico coerente e incremental. - ✅
C4 - README técnico e justificativas - Clareza nas decisões e priorização. - ✅
C5 - Escalabilidade e manutenção - Soluções preparadas para evolução. - ✅

## ✅ Status

> ✅ Projeto concluído com sucesso  
> Pronto para avaliação técnica
