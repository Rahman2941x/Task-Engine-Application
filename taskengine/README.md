# 🚀 Task Engine Application

A **microservices-based Task & Project Management System** built using **Spring Boot**, designed to handle **project workflows, task assignments, and team collaboration** efficiently.

---

## 📌 Project Overview

This application allows:

* Users to register and participate in projects
* Tasks to be created, assigned, and tracked
* Task priorities and statuses to be managed
* Dynamic workflow where tasks progress based on updates

The system is designed to simulate a **real-world task execution engine** using microservices architecture.

---

## 📦 Modules

* **taskengine-api-gateway** → Entry point for all client requests
* **taskengine-service-registry** → Eureka server for service discovery
* **taskengine-project-service** → Handles project management
* **taskengine-task-service** → Manages tasks, priorities, and status
* **taskengine-user-service** → Handles user data and mapping

---

## 📂 Project Structure

```
taskengine-parent/
│
├── taskengine-api-gateway/
├── taskengine-project-service/
├── taskengine-service-registry/
├── taskengine-task-service/
├── taskengine-user-service/
└── pom.xml
```

---

## 🏗️ Core Features

### 🔹 Project Management

* Create and manage projects
* Maintain project status
* Map users to projects

### 🔹 Task Management

* Create tasks under projects
* Manage:

    * Priority (`HIGH`, `MEDIUM`, `LOW`)
    * Status (`ACTIVE`, `IN_PROGRESS`, `COMPLETED`)
* Track deadlines and active tasks
* Fetch tasks based on filters

### 🔹 User Management

* Users linked with projects and tasks
* Used for assignment and tracking

---

## 🔄 Workflow

1. User registers
2. Project is created
3. Users are mapped to project
4. Tasks are created under project
5. Tasks are assigned priorities
6. Tasks move through statuses
7. Queries fetch:

    * Active tasks
    * Tasks by status
    * Tasks with deadlines

---

## 🔐 Security Implementation

* OAuth2 based authentication
* Feign Client for inter-service communication
* Access token automatically added using `RequestInterceptor`
* Uses **Client Credentials Flow** for secure internal communication

### 🛡️ Role-Based Access Control (RBAC)

* Implemented role-based authorization using Spring Security
* Roles supported:

    * `ADMIN` → Full access (create, update, delete)
    * `USER` → Limited access (view and assigned tasks)
* Endpoint-level restrictions applied using role checks
* Ensures secure and controlled access to APIs

---

## ⚙️ Tech Stack

### 🔸 Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA

### 🔸 Microservices Tools

* OpenFeign
* Eureka (Service Registry)

### 🔸 Database

* MySQL / PostgreSQL
* Hibernate (ORM)

---

## 📂 Key Code Highlights

### 🔹 Priority Handling

```java
queue = new PriorityQueue<>(
    (a, b) -> b.getPriority().compareTo(a.getPriority())
);
```

### 🔹 Custom Query

```java
List<Task> findByIsActiveTrueAndStatusNotIn(TaskStatus status1, TaskStatus status2);
```

### 🔹 OAuth2 Feign Interceptor

Automatically attaches:

```http
Authorization: Bearer <token>
```

---

## ▶️ Running the Project

1. Configure database in `application.yml`
2. Start services in order:

    * Service Registry
    * API Gateway
    * Other services
3. Run each service:

```bash
mvn spring-boot:run
```

---

## 🧠 Design Decisions

* Used **PriorityQueue** for task prioritization
* Avoided recursion in entity relationships
* Used DTOs for clean API responses
* Implemented dynamic queries using Spring Data JPA
* Secured APIs using OAuth2 and RBAC

---

## 🚧 Current Status

* Core features implemented
* Microservices communication working
* Security (OAuth2 + RBAC) integrated

---

## 🔮 Future Improvements

* Notification system
* Dashboard analytics
* Real-time updates
* Advanced reporting

---

## 👨‍💻 Author

**Syed Abdul Rahman**

---

## 📌 Note

This project demonstrates **real-world microservices architecture using Java and Spring Boot**, including secure service-to-service communication and role-based access control.
