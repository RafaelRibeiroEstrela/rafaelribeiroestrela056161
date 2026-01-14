Nome do participante: Rafael Ribeiro Estrela

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
git clone https://github.com/RafaelRibeiroEstrela/Java-desafio-processo-seletivo-seplag.git
cd seplag-api
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

## Autenticação e Segurança

A aplicação utiliza OAuth token JWT para autenticação.

Para realizar login, acesse o url:

```
Método: POST
URL: http://localhost:26000/auth/login
Request: {
  "username":"admin",
  "password":"12345"
}
```

Para atualizar token, acesse o url:

```
Método: POST
URL: http://localhost:26000/auth/refresh-token
Request: {
   "token":"{token}" 
}
```

Para realizar logout, acesse a url:

```
Método: POST
URL: http://localhost:26000/auth/logout
Request: {
   "token":"{token}" 
}
```

## 📁 Upload de Arquivos

Para realizar upload de fotos:

```
http://localhost:26000/upload-fotos
```

Ao realizar o upload, será retornado um json com a URL para recuperar o arquivo.
Lembre-se que a URL irá expirar após 5 minutos.

Para download, utilize a URL retornada do upload ou utilize o endpoint de download informado no Swagger.

---

## 🧩 Arquitetura & Padrões

- Separação clara entre camadas: **Controller**, **Service**, **Repository**
- Aplicação de princípios **SOLID**
- Uso de **DTOs** e mapeamentos com ModelMapper (se aplicável)
- Tratamento de exceções com `@ControllerAdvice`
- Logs com **SLF4J**

---

## Observações:

Datas devem ser informadas no padrão: dd/MM/yyyy
Exemplo: 10/10/1990

Alguns atributos são enums, utilize os valores abaixo:

tipoLogradouro:
```
    AEROPORTO,
    ALAMEDA,
    AREA,
    AVENIDA,
    CAMPO,
    CHACARA,
    COLONIA,
    CONDOMINIO,
    CONJUNTO,
    DISTRITO,
    ESPLANADA,
    ESTACAO,
    ESTRADA,
    FAVELA,
    FAZENDA,
    FEIRA,
    JARDIM,
    LADEIRA,
    LAGO,
    LAGOA,
    LARGO,
    LOTEAMENTO,
    MORRO,
    NUCLEO,
    PARQUE,
    PASSARELA,
    PATIO,
    PRACA,
    QUADRA,
    RECANTO,
    RESIDENCIAL,
    RODOVIA,
    RUA,
    SETOR,
    SITIO,
    TRAVESSA,
    TRECHO,
    TREVO,
    VALE,
    VEREDA,
    VIA,
    VIADUTO,
    VIELA,
    VILA;
```

sexo:

```
    MASCUNILO
    FEMININO
```

unidadeFederativa:

```
    AC,
    AL,
    AP,
    AM,
    BA,
    CE,
    DF,
    ES,
    GO,
    MA,
    MT,
    MS,
    MG,
    PA,
    PB,
    PR,
    PE,
    PI,
    RJ,
    RN,
    RS,
    RO,
    RR,
    SC,
    SP,
    SE,
    TO
```

## ✅ Status

> ✅ Projeto concluído com sucesso  
> Pronto para avaliação técnica
