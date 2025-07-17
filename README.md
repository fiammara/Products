# Products Application

A learning project showcasing microservices for product and sales management using Spring Boot. 
This project consists of multiple services working together with modern cloud-native technologies like Kubernetes, Kafka, and Prometheus.

---

## 🏗️ Project Structure

| Service           | Description                                                                 |
|-------------------|-----------------------------------------------------------------------------|
| **ui-service**      | Spring Boot service providing the UI layer and REST APIs for product management |
| **product-service** | Core service managing product data and business logic                       |
| **sales-service**   | Handles sales transactions, orders, and related operations                  |

---

## 🚀 Features & Technologies

- Microservices architecture with **Spring Boot**  
- Event-driven communication using **Apache Kafka**  
- Containerized deployment via **Docker** and orchestration with **Kubernetes**  
- Monitoring and alerting with **Prometheus**  
- Configuration management via environment variables and Docker Compose/Kubernetes manifests  
- RESTful APIs across services for clean separation of concerns  

---

## ⚙️ Prerequisites

- Docker (Docker Desktop recommended for Windows/macOS)  
- Docker Compose (usually included with Docker Desktop)  
- Kubernetes cluster (e.g., Minikube, kind, or cloud provider)  
- Kafka cluster (can be deployed with Kubernetes or run externally)  
- Prometheus (for monitoring, optional but recommended)

---

## 🛠️ Running the Application

### Running Locally with Docker Compose

Clone the repository and start all services:

```bash
git clone https://github.com/fiammara/Products.git
cd Products
docker compose up --build
```

This will launch **ui-service**, **product-service**, **sales-service**, Kafka, and other dependencies defined in `docker-compose.yml`.

---

### Deploying on Kubernetes

Use the manifests in the `k8s/` directory to deploy the services, Kafka, and Prometheus:

```bash
kubectl apply -f k8s/
```

Ensure your Kubernetes cluster is running and configured properly.

---


## 📡 Monitoring

Prometheus is set up to scrape metrics from all Spring Boot services exposing actuator endpoints.  
Access Prometheus UI and set up alerting as needed.

---

### Accessing Kafka UI and Prometheus UI

After starting the application with Docker Compose or Kubernetes, you can access the monitoring UIs at:

- **Kafka UI:** http://localhost:9000

- **Prometheus UI:** http://localhost:9090


---

## 📚 API Overview

Each service exposes REST endpoints, for example:
 
- `product-service`: `/api/products` CRUD endpoints  

Refer to each service’s documentation or Swagger UI for detailed API specs.

---

## 🧪 Testing

Unit and integration tests are included for **controllers and services** in each microservice.  
Tests can be run locally, inside Docker containers or as part of your CI/CD pipeline.

---


## 📄 License

MIT License

---

## 👤 Author

**fiammara** — https://github.com/fiammara
