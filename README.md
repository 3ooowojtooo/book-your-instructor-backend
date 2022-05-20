# BookYourInstructor backend

## Local development

- Run PostgreSQL instance locally on port `5432`

- Create database name `db` for user `user` with password `password`

- Build Docker image
```bash
docker build -t backend .
```

- Run Docker image
```bash
docker run -p 8080:8080 -e JWT_SECRET='di02hdf02d203h023n8H)@#0@$640348' -e DB_USERNAME='user' -e DB_PASSWORD='password'
-e DB_URL='jdbc:postgresql://host.docker.internal:5432/db' -e CORS_ALLOWED_ORIGINS='https://localhost:3000' --name=backend backend
```