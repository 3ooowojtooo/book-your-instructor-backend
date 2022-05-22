# BookYourInstructor backend

## Local development

- Run PostgreSQL instance locally on port `5432`

- Create database name `quary` for user `quary` with password `cYsesem1`

- Build Docker image
```bash
docker build -t backend .
```

- Run Docker image
```bash
docker run -p 8080:8080 -e JWT_SECRET='di02hdf02d203h023n8H)@#0@$640348' -e DB_USERNAME='quary' -e DB_PASSWORD='cYsesem1' -e DB_URL='jdbc:postgresql://172.17.0.1:5432/quary' -e CORS_ALLOWED_ORIGINS='https://localhost:3000' --name=backend backend
```

On Mac / Windows use `host.docker.internal` db host instead of static address `172.17.0.1`.