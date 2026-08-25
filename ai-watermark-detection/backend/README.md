# AI Watermark Detection Backend (Spring Boot)

This is the Spring Boot backend service for the AI Watermark Detection platform.

## Features
- **Anonymous vs Authenticated Access Control**: Text detection is allowed anonymously; Image detection strictly requires authenticated users.
- **Token Balance Deductions**: Configurable token charges per text analysis (`1`) and image analysis (`5`).
- **FastAPI Model Integration**: Seamless forwarding of multipart image files and JSON text payloads to the downstream ML inference server.
- **Audit & Analytics Logging**: Automatic tracking of user detection requests and confidence scores in MySQL database.
- **Input Validation & Exception Handling**: Validates empty/blank inputs and provides structured HTTP responses.

## Setup & Running

1. **MySQL Database**: Ensure MySQL is running on `localhost:3306` with database `watermark_db` (or update `application.yml`).
2. **FastAPI Detector Service**: Ensure FastAPI is running on `http://localhost:8000`.
3. **Build and Run**:
```bash
mvn clean spring-boot:run
```

## Endpoints

- `POST /api/v1/detect/text` (JSON payload: `{"text": "..."}`)
- `POST /api/v1/detect/image` (Multipart Form Data: `file=@image.png`)
