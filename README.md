# 🚖 RideShare Backend – Spring Boot & MongoDB

This project is a backend system for a simple ride-sharing application built using **Spring Boot**, **MongoDB**, and **JWT Authentication**. It supports passenger and driver roles, secure login, ride requests, ride acceptance, and ride completion.

---

## ✅ Features

- User & Driver Registration
- Secure Login with JWT Authentication
- Encrypted Password Storage (BCrypt)
- Passenger can Request Rides
- Driver can View & Accept Ride Requests
- Passenger or Driver can Complete Rides
- View Passenger’s Own Ride History
- Input Validation using Jakarta Validation
- Global Exception Handling
- Role-based Authorization using Spring Security

---

## 🛠 Tech Stack

- Java 17+
- Spring Boot
- Spring Web
- Spring Data MongoDB
- Spring Security
- JWT (JSON Web Token)
- MongoDB
- Maven

---

## 📁 Project Structure

org.example.rideshare
│
├── config → Security & JWT configuration
├── controller → All REST APIs
├── dto → Request & Response objects
├── exception → Global exception handling
├── model → User & Ride entities
├── repository → MongoDB repositories
├── service → Business logic
├── util → JWT utility & filter
└── RideshareApplication.java

yaml
Copy code

---

## 🔐 API Access Rules

| Role | Endpoint | Action |
|------|----------|--------|
| Public | `/api/auth/register` | Register User/Driver |
| Public | `/api/auth/login` | Login & Get JWT |
| USER | `/api/v1/rides` | Create Ride |
| USER | `/api/v1/user/rides` | View Own Rides |
| DRIVER | `/api/v1/driver/rides/requests` | View Pending Rides |
| DRIVER | `/api/v1/driver/rides/{id}/accept` | Accept Ride |
| USER / DRIVER | `/api/v1/rides/{id}/complete` | Complete Ride |

---

## ▶ How to Run the Project

1. Make sure **MongoDB is running** on:
mongodb://localhost:27017

markdown
Copy code

2. Clone the repository:
git clone https://github.com/shivansh023023/Springboot-rideshare.git

markdown
Copy code

3. Open the project in IntelliJ or any IDE.

4. Run:
mvn spring-boot:run

arduino
Copy code

5. Server will start on:
http://localhost:8081

yaml
Copy code

---

## 🔑 Authentication Flow

1. Register user or driver
2. Login to get JWT token
3. Pass token in headers:
Authorization: Bearer <YOUR_TOKEN>

yaml
Copy code
4. Access secured APIs

---

## 🧾 Sample API Requests (cURL)

### Register User
```bash
curl -X POST http://localhost:8081/api/auth/register \
-H "Content-Type: application/json" \
-d '{"username":"john","password":"1234","role":"ROLE_USER"}'
Register Driver
bash
Copy code
curl -X POST http://localhost:8081/api/auth/register \
-H "Content-Type: application/json" \
-d '{"username":"driver1","password":"abcd","role":"ROLE_DRIVER"}'
Login
bash
Copy code
curl -X POST http://localhost:8081/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"john","password":"1234"}'
Create Ride
bash
Copy code
curl -X POST http://localhost:8081/api/v1/rides \
-H "Authorization: Bearer <TOKEN>" \
-H "Content-Type: application/json" \
-d '{"pickupLocation":"A","dropLocation":"B"}'
