# Team 24 – Cookbook Application

- **Team members**: Thomas Boone (r1039745), Rijensh Limbu (r0884125), Rik Vanbrabant (r1044068)  
- **Repository**: https://github.com/ThomasbooneUCLL/Team-24.git  

## 1. Application overview

This project is a simple **cookbook / recipe** application:
- View cookbooks and their recipes
- Add cookbooks and recipes
- Simple “login” page in the front-end (hardcoded demo users, see below)

## 2. Tech stack

- **Front-end**: Next.js (React) + TailwindCSS  
- **Back-end**: Java 21, Spring Boot, Spring Data JPA  
- **Database**: PostgreSQL  

## 3. Prerequisites

- Node.js + npm (for the front-end)
- Java JDK 21 (for the back-end)
- PostgreSQL (local install or Docker)

## 4. Database setup (connection, schema, database user)

### 4.1 Create a database user (PostgreSQL)
This project expects a PostgreSQL user. The backend README suggests using the default user:

- **DB user**: `postgres`
- **DB password**: `postgres`

If you installed PostgreSQL and do not yet have this password set, you can set it:

```sql
ALTER USER postgres WITH PASSWORD 'postgres';
```

### 4.2 Create a database
Create a database in PostgreSQL (e.g. using pgAdmin):

- **Database name (example)**: `cookbookdb`
- **Schema**: uses the default **`public`** schema

### 4.3 Schema (tables)
The schema is initialized automatically at application start because Spring is configured with:

- `spring.sql.init.mode: always`
- `spring.jpa.hibernate.ddl-auto: none`

The SQL file that creates the tables is:

- `back-end-project-group6-1/src/main/resources/schema.sql`

It creates:
- `cookbooks`
- `recipes` (with FK to `cookbooks`)

### 4.4 Seed data
On startup, the back-end inserts sample data via:

- `back-end-project-group6-1/src/main/java/be/ucll/fs/project/DbInitializer.java`

## 5. Back-end: build & run

From the project root:

```bash
cd back-end-project-group6-1
```

### 5.1 Configure the datasource
Create a file **`application.yaml`** in `back-end-project-group6-1/` (same folder as `pom.xml`) with:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cookbookdb
    username: postgres
    password: postgres
```

> Adjust `cookbookdb` if you used a different database name.

### 5.2 Run the back-end
```bash
./mvnw spring-boot:run
```

The API runs on:

- **http://localhost:3000**

Useful endpoints:
- `GET /cookbooks`
- `GET /cookbooks/{id}`
- `POST /cookbooks`
- `GET /recipes`

## 6. Front-end: build & run

From the project root:

```bash
cd front-end-project-group6-1
npm install
```

### 6.1 Configure the API URL
Create a **`.env`** file in `front-end-project-group6-1/`:

```properties
NEXT_PUBLIC_API_URL=http://localhost:3000
```

### 6.2 Run the front-end (dev)
```bash
npm run dev
```

The front-end runs on:

- **http://localhost:8080**

## 7. Application “users” (demo login)

The front-end login is **hardcoded** (not stored in the database). You can log in with:

- **user**: `user1` / **password**: `pass1`
- **user**: `admin` / **password**: `admin`

Defined in:
- `front-end-project-group6-1/services/UserService.ts`

## 8. Notes / troubleshooting

### CORS
If you see CORS errors in the browser console:
- The front-end runs on `http://localhost:8080`
- The back-end should allow that origin

There are two places related to CORS:
- Controller annotations: `@CrossOrigin(origins = "http://localhost:8080")`
- `WebConfig.java` currently allows `http://localhost:3000`

If needed, update `WebConfig.java` to allow `http://localhost:8080` as well.


| Topic | Blue 1 | Blue 2 | Red |
|----------|----------|----------|----------|
| Cryptography  |          |          |          |
| Injections   |          |          |          |
| Vulnerable & Outdated Components  |          |          |          |
| Authentication & Sessions  |          |          |          |
| Authorization   |          |          |          |
| Secure CI / CD   |          |          |          |
| Logging & Monitoring  |          |          |          |
| SSRF & Clickjacking  |          |          |          |



---
