# Task Sync Backend (Spring Boot)

Runnable Spring Boot Maven project implementing a Task CRUD + Sync API (offline sync support).

## Requirements
- Java 17+
- Maven 3.6+

## Run
From project root:

```bash
mvn spring-boot:run
```

Server runs at http://localhost:8080

## Endpoints
- `GET /api/tasks` - get all tasks
- `GET /api/tasks/{id}` - get task by id
- `POST /api/tasks` - create task
- `PUT /api/tasks/{id}` - update task
- `DELETE /api/tasks/{id}` - delete task
- `POST /api/sync` - sync list of tasks from client (returns synced, conflicts, errors)

Example create:
```bash
curl -X POST http://localhost:8080/api/tasks -H "Content-Type: application/json" -d '{
  "id": "1111-2222-3333",
  "title": "Buy milk",
  "description": "At grocery store",
  "completed": false,
  "updatedAt": "2025-10-29T10:00:00Z"
}'
```

Example sync:
```bash
curl -X POST http://localhost:8080/api/sync -H "Content-Type: application/json" -d '[{ "id": "1111-2222-3333", "title": "Edited offline", "description": "2L", "completed": false, "updatedAt": "2025-10-29T11:00:00Z" }]'
```
