# QR Code Generator

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.x-brightgreen)
![AWS SDK](https://img.shields.io/badge/AWS%20SDK-2.24.12-yellow)
![Google ZXing](https://img.shields.io/badge/Google%20ZXing-3.5.2-blue)
![Docker](https://img.shields.io/badge/Docker-✓-blue)
![Maven](https://img.shields.io/badge/Maven-3.9.x-red)

A Spring Boot application that generates QR Codes from text input and stores them in an **S3-compatible object storage**.
By default, the project runs **locally using MinIO via Docker Compose**, without requiring an AWS account.

The project demonstrates:

* QR Code generation using **Google ZXing**
* Storage abstraction via a simple `StoragePort`
* S3-compatible storage using **MinIO**
* Easy local setup using **Docker Compose**

---

## Table of Contents

* [How to Use](#how-to-use)

  * [Prerequisites](#prerequisites)
  * [Running the Application](#running-the-application)

    * [Docker (Recommended)](#docker-recommended)
    * [Local Development](#local-development)
* [Configuration](#configuration)
* [Application Flow](#application-flow)
* [API Endpoints](#api-endpoints)
* [License](#license)

---

## How to Use

### Prerequisites

* Docker & Docker Compose
* (Optional) Java 21 and Maven — only required for local development without Docker

> ❗ You **do not need an AWS account** to run this project locally.

---

## Running the Application

### Docker (Recommended)

This is the **simplest and recommended way** to run the application.

The Docker Compose setup includes:

* The Spring Boot application
* A MinIO instance (S3-compatible storage)
* Automatic bucket creation and public access configuration

#### 1️⃣ Build and start everything

```bash
docker compose up --build
```

#### 2️⃣ Verify services

* Application: [http://localhost:8080](http://localhost:8080)
* MinIO Console: [http://localhost:9001](http://localhost:9001)

  * User: `minioadmin`
  * Password: `minioadmin`

---

### Local Development (Optional)

You can run the application outside Docker while keeping MinIO in Docker.

#### 1️⃣ Start MinIO only

```bash
docker compose up minio minio-init
```

#### 2️⃣ Run the application locally

```bash
./mvnw spring-boot:run
```

The application will connect to MinIO using the default configuration in `application.properties`.

---

## Configuration

### Default (Local / MinIO)

Configuration is handled via `application.properties` and environment variables.

```properties
aws.s3.region=us-east-1
aws.s3.bucket-name=qrcode-bucket
aws.s3.endpoint=http://localhost:9000
aws.s3.access-key=minioadmin
aws.s3.secret-key=minioadmin

storage.public-base-url=http://localhost:9000/qrcode-bucket
```

### Running inside Docker

When running via Docker Compose, these variables are overridden automatically:

```yaml
AWS_S3_ENDPOINT=http://minio:9000
STORAGE_PUBLIC_BASE_URL=http://localhost:9000/qrcode-bucket
```

### Using AWS S3 (Optional)

To use AWS S3 instead of MinIO:

1. Remove `aws.s3.endpoint`
2. Set AWS credentials via environment variables or IAM role
3. Update the public base URL:

```properties
storage.public-base-url=https://your-bucket.s3.your-region.amazonaws.com
```

No code changes are required.

---

## Application Flow

1. Client sends text to the API
2. Application generates a QR Code PNG using ZXing
3. The image is uploaded to an S3-compatible storage
4. The API returns a public URL to access the QR Code

---

## API Endpoints

### POST `/qrcode`

Generate a QR Code from text and return its public URL.

#### Request Body

```json
{
  "text": "https://example.com"
}
```

#### Response

```json
{
  "url": "http://localhost:9000/qrcode-bucket/uuid.png"
}
```

#### Example Request

```bash
curl -X POST http://localhost:8080/qrcode \
  -H "Content-Type: application/json" \
  -d '{"text":"Hello MinIO"}'
```

---

## Swagger (API Documentation)

Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

---

## License

This project is licensed under the MIT License.

---

### Final note (importante)

Este projeto foi pensado como **ferramenta simples, portátil e fácil de subir**, priorizando:

* baixo acoplamento com cloud providers
* setup rápido via Docker
* clareza arquitetural sem overengineering

Se quiser, no próximo passo posso:

* revisar o README em inglês técnico “enterprise”
* ou simplificar ainda mais para um README estilo *tool / OSS*
