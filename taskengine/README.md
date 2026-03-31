# 🚀 Task Engine Application

A **microservices-based Task & Project Management System** built using **Spring Boot**, designed to handle **project workflows, task assignments, and team collaboration** efficiently.

---

## 📌 Project Overview

This application allows:

* Users to register and participate in projects
* Tasks to be created, assigned, and tracked
* Task priorities and statuses to be managed
* Dynamic workflow where tasks progress based on updates
* Supports **dependent task execution using tree structure**
* Uses **Topological Sort + Priority Queue** to manage execution order and avoid circular dependencies

The system is designed to simulate a **real-world task execution engine** using microservices architecture.

---

## 📦 Modules

* **taskengine-api-gateway** → Entry point for all client requests
* **taskengine-service-registry** → Eureka server for service discovery
* **taskengine-project-service** → Handles project management
* **taskengine-task-service** → Manages tasks, priorities, dependencies, and execution flow
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

### 🔹 Task Dependency (Tree-Based Tasks)

* Supports hierarchical (parent-child) task structure
* Tasks can depend on other tasks
* A task can start only after its parent task is completed
* Enables workflow execution similar to real-world systems

**Example:**

```
Parent Task
   ├── Sub Task 1
   │     └── Child Task
   └── Sub Task 2
```

### 🔹 Dependency Handling & Execution Engine

* Uses **Topological Sorting** to determine valid task execution order
* Prevents **circular dependencies (cycles)** in task graph
* Combines **Priority Queue** to execute higher priority tasks first
* Ensures both:

    * Correct dependency order
    * Optimal priority-based execution

---

## 🔄 Workflow

1. User registers
2. Project is created
3. Users are mapped to project
4. Tasks are created under project
5. Tasks are linked as **dependent (graph structure)**
6. System validates dependencies using **cycle detection**
7. Tasks are ordered using **Topological Sort**
8. Execution priority handled using **Priority Queue**
9. Tasks move through statuses
10. Queries fetch:

* Active tasks
* Tasks by status
* Tasks with deadlines
* Dependent task hierarchy

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

### 🔹 Priority Queue

```java
queue = new PriorityQueue<>(
    (a, b) -> b.getPriority().compareTo(a.getPriority())
);
```

### 🔹 Topological Sort (Concept)

```java
// Kahn’s Algorithm (simplified)
Queue<Task> q = new LinkedList<>();
while (!q.isEmpty()) {
    Task t = q.poll();
    for (Task dependent : t.getChildren()) {
        reduceIndegree(dependent);
        if (indegree == 0) q.add(dependent);
    }
}
```

### 🔹 Custom Query

```java
List<Task> findByIsActiveTrueAndStatusNotIn(TaskStatus status1, TaskStatus status2);
```

### 🔹 OAuth2 Feign Interceptor

Automatically attaches:

```
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

```
mvn spring-boot:run
```

---

## 🧠 Design Decisions

* Used **PriorityQueue** for task prioritization
* Implemented **Topological Sort** for dependency resolution
* Prevented **circular dependencies using graph cycle detection**
* Used **tree/graph-based structure** for task relationships
* Avoided recursion issues in entity mapping
* Used DTOs for clean API responses
* Secured APIs using OAuth2 and RBAC

---

## 🚧 Current Status

* Core features implemented
* Microservices communication working
* Security (OAuth2 + RBAC) integrated
* Dependency management with cycle prevention implemented

---

## 🔮 Future Improvements

* Notification system
* Dashboard analytics
* Real-time updates
* Advanced workflow automation

---

## 👨‍💻 Author

**Syed Abdul Rahman**

---

## 📌 Note

This project demonstrates **real-world microservices architecture using Java and Spring Boot**, including secure service-to-service communication, role-based access control, and advanced task execution using graph-based dependency resolution.
