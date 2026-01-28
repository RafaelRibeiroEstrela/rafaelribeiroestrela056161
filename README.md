# 🚀 Desafio SEPLAG MT – API Backend Java

**Participante:** Rafael Ribeiro Estrela  \
**Nº de Inscrição:** 16428

---

## 📌 Visão Geral

Este repositório contém a implementação da **API RESTful** desenvolvida como parte do **PROCESSO SELETIVO CONJUNTO Nº 001/2026/SEPLAG/SEFAZ/SEDUC/SESP/PJC/PMMT/CBMMT/DETRAN/POLITEC/SEJUS/SEMA/SEAF/SINFRA/SECITECI/PGE/MTPREV**, para o cargo de **Engenheiro da Computação – Sênior**.

A solução foi projetada com foco em **qualidade de código**, **boas práticas de engenharia de software**, **segurança**, **escalabilidade** e **manutenibilidade**, atendendo integralmente aos requisitos técnicos propostos no desafio.

---

## 🧰 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**
  - Spring Web
  - Spring Data JPA
  - Spring Security
- **PostgreSQL** – Banco de dados relacional
- **Redis** – Cache e controle de rate limit
- **MinIO** – Armazenamento de arquivos (compatível com Amazon S3)
- **Flyway** – Versionamento e migração de banco de dados
- **Docker & Docker Compose** – Orquestração do ambiente
- **Swagger / OpenAPI** – Documentação dos endpoints

A aplicação segue o padrão arquitetural **MVC em camadas**, com aplicação de **SOLID**, **Clean Code** e **padrões de projeto**, visando clareza, baixo acoplamento e fácil evolução.

---

## 🐳 Preparação do Ambiente (Docker)

⚠️ **Atenção:** recomenda-se fortemente realizar a limpeza do ambiente Docker antes de executar a aplicação, evitando conflitos com containers, volumes ou redes pré-existentes.

### 🔄 Limpeza Completa do Docker

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

# Limpeza completa do sistema Docker
docker system prune -a --volumes -f
```

---

## ▶️ Executando a Aplicação

### 1️⃣ Clone o repositório

```bash
git clone https://github.com/RafaelRibeiroEstrela/rafaelribeiroestrela056161.git
cd rafaelribeiroestrela056161/processo-seletivo-api
```

### 2️⃣ Suba os containers

```bash
docker compose -f docker-compose.yml up -d --build
```

### 🔧 Serviços Inicializados

- API Java (Spring Boot)
- PostgreSQL
- Redis
- MinIO (com console web)

Todo o ambiente é inicializado automaticamente, incluindo a criação e versionamento do banco de dados via **Flyway**.

---

## 🧪 Testes e Documentação da API

A API está documentada via **Swagger/OpenAPI**.

📎 Acesse:
```
http://localhost:26000/swagger-ui.html
```

É possível testar todos os endpoints diretamente pelo Swagger ou utilizando ferramentas como **Postman** ou **curl**.

---

## 🔐 Autenticação e Segurança

A aplicação utiliza **JWT (JSON Web Token)** para autenticação.

### Fluxo de autenticação:
1. Execute o endpoint de **login** com o *username=admin* e *password=admin* pré-definidos.
2. Copie o token retornado.
3. Utilize o botão **Authorize** no Swagger para informar o token.

⏱️ O token possui **tempo de expiração de 5 minutos**.

Além disso, a aplicação conta com:
- **CORS configurado** para bloqueio de domínios externos
- **Rate Limit** baseado em Redis

---

## 🔄 WebSocket (Funcionalidade Sênior)

A API disponibiliza notificações em tempo real via **WebSocket**.

### Configuração:
```
WS_URL = ws://localhost:26000/ws
TOPIC  = /topic/albuns/novos
```

📂 Um **cliente de exemplo em Python** está disponível no diretório:
```
client-websocket/
```

---

## ❤️ Health Check e Liveness

A aplicação expõe endpoints de **health check**, permitindo validação de disponibilidade e prontidão do serviço, compatível com ambientes orquestrados (Docker Swarm, Kubernetes, OpenShift).

---

## 📋 Requisitos Atendidos

### 🏗️ Arquitetura e Estrutura
- Organização em camadas (Controller, Service, Repository, Model) ✅
- Endpoints versionados e documentados via Swagger ✅
- Migrations automáticas com Flyway ✅

### ⚙️ Funcionalidades Técnicas
- CRUD completo (POST, PUT, GET, DELETE) ✅
- Paginação, ordenação e filtros ✅
- Upload de arquivos e Presigned URLs (MinIO) ✅
- Autenticação JWT com expiração ✅
- CORS e Rate Limit ✅
- WebSocket e sincronização em tempo real ✅
- Health Check / Liveness ✅

### 🧠 Boas Práticas
- Clean Code e separação de responsabilidades ✅
- Aplicação de conceitos do livro *Clean Code* – Robert C. Martin ✅
- Testes unitários nas principais regras de negócio (Album, Artista e Regionais) ✅
- Histórico de commits coerente e incremental ✅
- Código preparado para evolução e escalabilidade ✅

---

## 📈 Escalabilidade e Manutenção

A solução está preparada para:
- Execução em **ambientes distribuídos**
- Escala horizontal com múltiplas instâncias
- Integração com **Docker Swarm**, **Kubernetes** ou **OpenShift**

O uso do **Redis** contribui diretamente para segurança, performance e controle de requisições em cenários de alta concorrência.

---

## ✅ Status do Projeto

> ✔️ **Projeto finalizado com sucesso**  \
> 📦 Pronto para avaliação técnica

---

📌 *Obrigado pela oportunidade de participar deste processo seletivo.*

