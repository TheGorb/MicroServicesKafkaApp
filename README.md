# MicroServicesKafkaApp

This repository contains a microservices-based application for a hypothetical system

The application is structured into the following main components:

- **CommonMessaging**: A shared library providing common messaging functionalities across microservices.
- **customer-management-services**: A microservice responsible for managing customer information.
- **payment-processing-services**: A microservice responsible for processing payments.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Docker
- Docker Compose
- Java 11
- Maven
- Kafka
- MongoDB

### Building the Project

1. **Build the CommonMessaging Library**:

    Navigate to the `CommonMessaging` directory and run:
    
    ```mvn clean install```

    This command compiles the CommonMessaging library and installs it into your local Maven repository.


2. **Build the Microservices**:

    Each microservice can be built with Maven. For example, to build the customer-management-services, navigate to its directory and run:

    ```mvn clean install```

    This command compiles the microservice and installs it into your local Maven repository.

### Running the project with docker compose

To simplify the deployment of the microservices and their dependencies, Docker Compose is used.

Navigate to the root of the repository where the docker-compose.yml file is located.

Run the following command to build and start the containers:

```docker-compose up --build```

This command builds the Docker images for the microservices (if not already built) and starts the containers as defined in the docker-compose.yml.

### Accessing the Services

Customer Management Service: Accessible at http://localhost:8081
Payment Processing Service: Accessible at http://localhost:8082

