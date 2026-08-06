<div align="center">

# 🎬 Catálogo de Filmes e Séries

**Organize, avalie e descubra filmes e séries — tudo em um só lugar.**

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Quarkus](https://img.shields.io/badge/Quarkus-3-4695EB?style=for-the-badge&logo=quarkus&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-21-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)

</div>

<br>

Projeto pessoal de estudo: um catálogo completo de filmes e séries, com elenco, gêneros, avaliações e um dashboard com estatísticas. Sistema aberto, sem login — é só entrar e usar.

<br>

## ✨ O que dá pra fazer

- 🎞️ **Cadastrar filmes e séries** com pôster, sinopse, elenco e gêneros
- ⭐ **Avaliar** com nota e comentário — a média é recalculada na hora, sempre
- 🔍 **Buscar e filtrar** por título, gênero, tipo, ano e nota mínima
- 📊 **Acompanhar um dashboard** com estatísticas gerais e gráficos
- 🎭 **Gerenciar** gêneros e o elenco (atores, diretores, roteiristas)

<br>

## 🚀 Como rodar

Com [Docker](https://www.docker.com/) instalado, um único comando sobe o banco, a API e o site:

```bash
docker compose up --build
```

<div align="center">

| | |
|---|---|
| 🖥️ **App** | http://localhost:4200 |
| ⚙️ **API** | http://localhost:8080/api |
| 📘 **Swagger** | http://localhost:8080/q/swagger-ui |

</div>

O banco já sobe populado com alguns filmes, séries e avaliações de exemplo.

<br>

<details>
<summary><strong>Prefere rodar sem Docker? (modo desenvolvimento)</strong></summary>

<br>

**Backend** — requer Java 21 e um PostgreSQL local (banco `catalogo`, usuário `postgres`, senha `1234` por padrão):

```bash
cd backend
./mvnw quarkus:dev
```

Se seu Postgres usa outras credenciais, exporte antes de rodar:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/catalogo
export DB_USERNAME=meu_usuario
export DB_PASSWORD=minha_senha
```

Sem Postgres instalado? Tendo Docker disponível, remova as três linhas `%dev.quarkus.datasource.*` de `backend/src/main/resources/application.properties` — o Quarkus sobe um banco descartável sozinho.

**Frontend** — requer Node.js 20+:

```bash
cd frontend
npm install
npm start
```

</details>

<br>

## 🛠️ Tecnologias

**Backend:** Java 21 · Quarkus · Hibernate + Panache · MapStruct · Flyway · OpenAPI/Swagger
**Frontend:** Angular (standalone) · PrimeNG · Chart.js
**Infra:** PostgreSQL · Docker Compose

<br>

<div align="center">

*Feito para fins de estudo e prática de full-stack.* 💛

</div>
