# Spring Hotel Management System

## Overview
Spring Hotel is a comprehensive hotel management system built with Spring Boot. This system provides REST APIs for managing hotel operations including bookings, rooms, and customer management.

## Setup Instructions

### Tools & Technologies:

[![technologies](https://skillicons.dev/icons?i=java,spring,maven,mysql,aws,docker,git,postman)](#backend)

### Prerequisites
- Java 17+
- Maven
- Docker
- AWS Account

### Local Development Setup

1. Clone the repository:
```bash
$  git clone https://github.com/MohamedAEmara/Spring-Hotel.git

$  cd Spring-Hotel
```

2. Database Setup (Docker):
```bash
$  docker-compose up -d
```

3. Configure Application Properties:
Create `application.properties` in `src/main/resources/`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hoteldb
spring.datasource.username=admin
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### AWS Setup

1. Create an AWS Account if you don't have one

2. Configure AWS Services:
    - Set up an S3 bucket for file storage
    - Configure bucket permissions
    - Set up IAM user with S3 access

3. Update AWS credentials in `application.properties`:
```properties
cloud.aws.credentials.access-key=your_access_key
cloud.aws.credentials.secret-key=your_secret_key
cloud.aws.s3.bucket=your_bucket_name
cloud.aws.region=your_region
```

## Running the Application

1. Build the project:
```bash
$  mvn clean install
```

2. Run the application:
```bash
$  mvn spring-boot:run
```

## API Documentation

Complete API documentation & test are available at [Spring Hotel API Documentation](https://www.postman.com/learnovate/spring-hotel/documentation/e1rkc4y/spring-hotel)

The API documentation includes:
- Endpoint details
- Request/Response formats
- Authentication requirements
- Example requests

