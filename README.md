## Build
```bash
./mvnw clean package
```

## Run
```bash
./mvnw spring-boot:run
```

Or run the packaged jar:

```bash
java -jar target/Physica-0.0.1.jar
```

## Main URLs
- App: `http://localhost:8080/kirchhoff`
- Swagger UI: `http://localhost:8080/kirchhoff/swagger-ui/index.html`

## Notes
- The application uses file-based user storage configured by `app.security.users-file`.
- Default runtime user data is stored under `data/`.
