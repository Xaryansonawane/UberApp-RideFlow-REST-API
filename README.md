# 🚗 RideFlow-REST-API

> A **production-grade, fully featured RESTful backend** for a ride-hailing platform — engineered in the spirit of **Uber, Ola, and Rapido** — built with **Spring Boot 3.x**, **Spring Security 6**, **JWT Authentication with Refresh Token Rotation**, **Haversine-based Driver Location Tracking**, **Spring Data JPA**, and **MySQL**.
>
> This is not a tutorial project. Every design decision — from the layered architecture to the security token lifecycle — mirrors how real ride-hailing backends are structured at scale. Secure multi-role authentication, live driver proximity detection, ride lifecycle management, JWT token rotation and revocation, and a clean standardized API contract: all of it, in one codebase.

<br/>

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-blue?style=flat-square&logo=springsecurity)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JJWT-0.11.5-purple?style=flat-square&logo=jsonwebtokens)](https://github.com/jwtk/jjwt)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.x-red?style=flat-square&logo=apachemaven)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](LICENSE)

---

## 📋 Table of Contents

- [What is this project?](#-what-is-this-project)
- [Why this project stands out](#-why-this-project-stands-out)
- [Tech Stack](#-tech-stack)
- [Features](#-features)
- [How it Works — Architecture](#-how-it-works--architecture)
- [Project Structure](#-project-structure)
- [Database Schema](#-database-schema)
- [Security — JWT Lifecycle Deep Dive](#-security--jwt-lifecycle-deep-dive)
- [Driver Location Tracking — How Haversine Works Here](#-driver-location-tracking--how-haversine-works-here)
- [API Reference](#-api-reference)
- [Request and Response Examples](#-request-and-response-examples)
- [Business Rules and Validations](#-business-rules-and-validations)
- [Error Handling](#-error-handling)
- [DTOs — Why We Never Expose Raw Entities](#-dtos--why-we-never-expose-raw-entities)
- [Getting Started — Run it Locally](#-getting-started--run-it-locally)
- [Configuration Reference](#-configuration-reference)
- [Roadmap](#-roadmap)
- [Author](#-author)

---

## 🌐 What is this project?

**RideFlow** is the server-side engine that powers a complete ride-hailing experience — the kind of backend that Uber, Ola, or Rapido runs behind their mobile apps.

When a rider opens the app and sees nearby drivers — this API finds them using real geographic math.
When a driver accepts a ride — this API manages the state machine from `REQUESTED` → `ACCEPTED` → `ONGOING` → `COMPLETED`.
When a token expires — this API rotates it securely without forcing a re-login.
When anyone calls an endpoint — this API returns the same predictable response contract, every single time.

This project was built to demonstrate how backend systems for real-world platforms are actually architected — not the watered-down version you see in tutorials.

---

## 💡 Why this project stands out

Most Spring Boot projects implement basic CRUD with JWT. RideFlow goes further:

| What most projects do | What RideFlow does |
|---|---|
| JWT login with a single token | Full token lifecycle: issue → rotate on refresh → revoke on logout |
| Static role definitions | Role-based security routing with `RIDER` and `DRIVER` access scopes |
| Simple user location fields | Real-time driver location with Haversine distance query in native SQL |
| Basic exception handling | `GlobalExceptionHandler` with categorized HTTP status codes and ALL CAPS error messages |
| Raw entity responses | Complete DTO separation — no entity ever touches the HTTP response |
| Hardcoded validations | Service-layer business rules with self-ride prevention, duplicate booking guards |

---

## 🚀 Tech Stack

| Layer | Technology | Purpose |
|-------|-----------|---------|
| Language | Java 21 | Core language |
| Framework | Spring Boot 3.x | Application backbone |
| Web Layer | Spring MVC | REST endpoint routing |
| Security | Spring Security 6 + JWT (JJWT 0.11.5) | Authentication, authorization, token management |
| Token Signing | HMAC-SHA256 (HS256) | Stateless JWT signing algorithm |
| ORM | Spring Data JPA + Hibernate | Entity-to-table mapping and query execution |
| Database | MySQL 8.0 | Persistent data store |
| Location Math | Haversine Formula (native SQL) | Real-world geographic distance between coordinates |
| Build Tool | Apache Maven 3.x | Dependency management and build lifecycle |

---

## ✨ Features

### 🔐 Security — JWT with Full Token Lifecycle

- ✅ **Access Token Generation** — HS256 signed JWT issued on login, expires in 24 hours
- ✅ **Refresh Token Rotation** — Each `/auth/refresh` call issues a new refresh token and invalidates the old one — prevents replay attacks
- ✅ **Refresh Token Revocation** — `/auth/logout` marks the token as revoked in the database — token can never be reused even if unexpired
- ✅ **RefreshToken Entity** — Stored in DB with `token`, `expiryDate`, `revoked` fields — full lifecycle tracking
- ✅ **JwtAuthenticationFilter** — Intercepts every request, extracts and validates the Bearer token before it reaches any controller
- ✅ **CustomUserDetailsService** — Loads user by email from the database for Spring Security's authentication context
- ✅ **Role-Based Route Protection** — SecurityConfig separates routes by `RIDER` and `DRIVER` roles
- ✅ **BCrypt Password Hashing** — Passwords are hashed before storage and never exposed in any response
- ✅ **Stateless Session** — No server-side session. Token carries all identity information

### 📍 Driver Location Tracking

- ✅ **Real-Time Location Updates** — Drivers update their GPS coordinates via a dedicated endpoint
- ✅ **DriverLocation Entity** — Stores `latitude`, `longitude`, `updatedAt` per driver in the database
- ✅ **Haversine Distance Query** — Native SQL query computes great-circle distance between rider and all drivers using the Haversine formula
- ✅ **Nearest Drivers Fetch** — Returns all drivers within a configurable radius, sorted by distance in ascending order
- ✅ **Location DTOs** — `DriverLocationDTO` and `NearbyDriverDTO` ensure clean data shapes in and out

### 👤 Authentication and Users

- ✅ **Rider Registration** — Sign up as a rider with full name, email, phone, password
- ✅ **Driver Registration** — Register as a driver with vehicle details, license info
- ✅ **Login** — Returns access token + refresh token on successful authentication
- ✅ **Token Refresh** — `/auth/refresh` validates the refresh token, rotates it, and returns a new access token
- ✅ **Logout** — `/auth/logout` revokes the refresh token in the database
- ✅ **Get Current User** — `/auth/me` returns the authenticated user's profile from the JWT context

### 🚖 Ride Management

- ✅ **Request a Ride** — Rider submits pickup and drop coordinates
- ✅ **Ride State Machine** — Ride progresses through: `REQUESTED` → `ACCEPTED` → `ONGOING` → `COMPLETED` / `CANCELLED`
- ✅ **Driver Accepts Ride** — Driver picks up a pending ride request
- ✅ **Start Ride** — Marks the ride as `ONGOING`
- ✅ **End Ride** — Marks the ride as `COMPLETED`
- ✅ **Cancel Ride** — Rider or driver can cancel before the ride starts
- ✅ **Ride History** — Fetch all past rides for a rider or driver

### 🧱 Code Quality and Standards

- ✅ **ApiResponse Wrapper** — Every endpoint returns `{ success, message, data, error }` — no surprises for the client
- ✅ **GlobalExceptionHandler** — `@ControllerAdvice` catches all exceptions and maps them to clean JSON
- ✅ **ALL CAPS Error Messages** — Consistent error string convention: `"USER NOT FOUND"`, `"INVALID TOKEN"`, `"RIDE ALREADY ACCEPTED"`
- ✅ **Canonical Package Structure** — `dto`, `controller`, `security`, `model`, `service`, `repository` — strict separation maintained throughout
- ✅ **No Raw Entity Responses** — Every HTTP response uses a purpose-built DTO

---

## 🏗️ How it Works — Architecture

```
📱 Client (Rider App / Driver App / Postman)
           │
           │  HTTP Request with Bearer Token in Authorization header
           ▼
┌──────────────────────────────────────────────┐
│          JwtAuthenticationFilter             │  ← Validates token on EVERY request
│  Extracts email → loads user → sets context  │    before the request reaches anything else
└──────────────────────┬───────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────┐
│            Controller Layer                  │  ← Receives HTTP, calls service,
│          (@RestController)                   │    wraps result in ApiResponse, returns
└──────────────────────┬───────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────┐
│             Service Layer                    │  ← ALL business logic lives here:
│            (@Service)                        │    validation, state transitions,
│                                              │    DTO mapping, rule enforcement
└──────────────────────┬───────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────┐
│           Repository Layer                   │  ← JpaRepository + custom native SQL
│    (JpaRepository + @Query)                  │    (including Haversine location query)
└──────────────────────┬───────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────┐
│              MySQL Database                  │  ← Persistent storage for all data
└──────────────────────────────────────────────┘
```

**Key design decisions:**

Controllers have zero business logic. They call a service method and return the result wrapped in `ApiResponse`. All decisions — who can accept a ride, whether a token is revoked, whether coordinates are valid — happen in services. DTOs are always the contract boundary between the service layer and the HTTP layer. Raw JPA entities never cross that line.

---

## 📁 Project Structure

```
UberApp/
│
├── src/main/java/com/example/UberApp/
│   │
│   ├── UberAppApplication.java                  ← Entry point
│   │
│   ├── security/                                ← All JWT security components
│   │   ├── JwtService.java                      ← Generates and validates HS256 JWT tokens
│   │   ├── JwtAuthenticationFilter.java         ← Intercepts every request, validates Bearer token
│   │   ├── JwtAuthenticationEntryPoint.java     ← Returns 401 JSON when token is missing/invalid
│   │   ├── CustomUserDetailsService.java        ← Loads UserDetails from DB by email
│   │   └── SecurityConfig.java                  ← Filter chain, BCrypt bean, role-based route rules
│   │
│   ├── controller/                              ← HTTP layer — no business logic
│   │   ├── AuthController.java                  ← /auth/register, /auth/login, /auth/refresh, /auth/logout, /auth/me
│   │   ├── RideController.java                  ← /rides/** endpoints
│   │   ├── DriverLocationController.java        ← /drivers/location/** endpoints
│   │   └── UserController.java                  ← /users/** endpoints
│   │
│   ├── service/                                 ← Business logic layer
│   │   ├── AuthService.java                     ← Register, login, token issuance, refresh, revoke
│   │   ├── RideService.java                     ← Ride lifecycle, state machine, validations
│   │   ├── DriverLocationService.java           ← Location update, nearest driver search
│   │   └── UserService.java                     ← User CRUD, profile management
│   │
│   ├── repository/                              ← Database interfaces
│   │   ├── UserRepository.java                  ← findByEmail, existsByEmail
│   │   ├── RideRepository.java                  ← findByRider_Id, findByDriver_Id, findByStatus
│   │   ├── DriverLocationRepository.java        ← Native Haversine SQL query for nearest drivers
│   │   └── RefreshTokenRepository.java          ← findByToken, deleteByUser
│   │
│   ├── model/                                   ← JPA entities (map to MySQL tables)
│   │   ├── User.java                            ← users table — shared base for riders and drivers
│   │   ├── Ride.java                            ← rides table — full ride record with status
│   │   ├── DriverLocation.java                  ← driver_locations table — lat/long + updatedAt
│   │   └── RefreshToken.java                    ← refresh_tokens table — token, expiry, revoked flag
│   │
│   ├── dto/                                     ← Data Transfer Objects
│   │   ├── LoginRequestDTO.java                 ← email + password
│   │   ├── AuthResponseDTO.java                 ← accessToken + refreshToken + user on login/register
│   │   ├── LoginResponseDTO.java                ← message + accessToken + refreshToken + userId + role
│   │   ├── RefreshTokenRequestDTO.java          ← refreshToken string
│   │   ├── DriverLocationDTO.java               ← latitude + longitude for update request
│   │   ├── NearbyDriverDTO.java                 ← driverId + name + distanceKm for nearby response
│   │   ├── RideRequestDTO.java                  ← pickupLat, pickupLng, dropLat, dropLng
│   │   └── RideResponseDTO.java                 ← full ride details with status, rider, driver info
│   │
│   └── utility/
│       ├── ApiResponse.java                     ← Generic wrapper: { success, message, data, error }
│       └── GlobalExceptionHandler.java          ← @ControllerAdvice — catches all exceptions
│
├── pom.xml
└── README.md
```

---

## 🗄️ Database Schema

### `users` table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | Auto-generated unique ID |
| full_name | VARCHAR | Full name of the user |
| email | VARCHAR UNIQUE | Used for login and JWT subject |
| password | VARCHAR | BCrypt hashed — never returned in any response |
| phone | VARCHAR | Contact number |
| role | ENUM | `RIDER` or `DRIVER` |
| vehicle_details | VARCHAR | Driver-only: vehicle model, plate number |
| is_active | BOOLEAN | Account status, default true |
| created_at | DATETIME | Account creation timestamp |

### `rides` table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | Auto-generated unique ID |
| rider_id | BIGINT FK → users | Who requested the ride |
| driver_id | BIGINT FK → users | Who accepted the ride (nullable until accepted) |
| pickup_lat | DOUBLE | Pickup point latitude |
| pickup_lng | DOUBLE | Pickup point longitude |
| drop_lat | DOUBLE | Drop point latitude |
| drop_lng | DOUBLE | Drop point longitude |
| status | ENUM | `REQUESTED`, `ACCEPTED`, `ONGOING`, `COMPLETED`, `CANCELLED` |
| fare | DECIMAL | Calculated fare for the ride |
| created_at | DATETIME | When the ride was requested |
| updated_at | DATETIME | Last status change timestamp |

### `driver_locations` table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | Auto-generated unique ID |
| driver_id | BIGINT FK → users | Which driver this location belongs to |
| latitude | DOUBLE | Current GPS latitude |
| longitude | DOUBLE | Current GPS longitude |
| updated_at | DATETIME | When the driver last updated their position |

### `refresh_tokens` table
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT PK | Auto-generated unique ID |
| user_id | BIGINT FK → users | Which user this token belongs to |
| token | VARCHAR UNIQUE | The raw refresh token string (UUID-based) |
| expiry_date | DATETIME | When this token expires (7 days) |
| revoked | BOOLEAN | Set to true on logout — blocks reuse even if not expired |

---

## 🔒 Security — JWT Lifecycle Deep Dive

This is where RideFlow differs from most projects. There are two tokens in play — an **access token** and a **refresh token** — and they are managed with a full rotation and revocation strategy.

### Token Configuration
```properties
jwt.expiration=86400000          # Access token — 24 hours (milliseconds)
jwt.refresh-expiration=604800000 # Refresh token — 7 days (milliseconds)
```

### The Complete Token Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                         LOGIN                                       │
│                                                                     │
│  POST /auth/login { email, password }                               │
│           │                                                         │
│           ▼                                                         │
│  AuthService verifies password via BCrypt                           │
│           │                                                         │
│           ▼                                                         │
│  JwtService.generateAccessToken(email)  → HS256 JWT, 24hr expiry   │
│  JwtService.generateRefreshToken(user)  → UUID stored in DB, 7day  │
│           │                                                         │
│           ▼                                                         │
│  Response: { accessToken, refreshToken, userId, role }             │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                    AUTHENTICATED REQUEST                            │
│                                                                     │
│  GET /rides  →  Authorization: Bearer <accessToken>                 │
│           │                                                         │
│           ▼                                                         │
│  JwtAuthenticationFilter intercepts                                 │
│  JwtService.extractEmail(token)                                     │
│  CustomUserDetailsService.loadUserByUsername(email)                 │
│  JwtService.isTokenValid(token, userDetails)                        │
│           │                                                         │
│  ✅ Valid  → SecurityContext is set → request proceeds to controller │
│  ❌ Invalid → 401 UNAUTHORIZED JSON via JwtAuthenticationEntryPoint │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                    TOKEN REFRESH (ROTATION)                         │
│                                                                     │
│  POST /auth/refresh { refreshToken: "uuid-string" }                │
│           │                                                         │
│           ▼                                                         │
│  RefreshTokenRepository.findByToken(token)                         │
│  Check: is it expired? is it revoked?                               │
│           │                                                         │
│  ✅ Valid:                                                           │
│    1. OLD refresh token → revoked = true (saved to DB)             │
│    2. NEW refresh token → generated and saved                       │
│    3. NEW access token → generated                                  │
│    4. Response: { newAccessToken, newRefreshToken }                 │
│           │                                                         │
│  ❌ Invalid/Expired/Revoked → 401 INVALID OR EXPIRED REFRESH TOKEN  │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                         LOGOUT (REVOCATION)                         │
│                                                                     │
│  POST /auth/logout { refreshToken: "uuid-string" }                 │
│           │                                                         │
│           ▼                                                         │
│  RefreshTokenRepository.findByToken(token)                         │
│  token.setRevoked(true) → saved to DB                               │
│  Response: { "LOGGED OUT SUCCESSFULLY" }                           │
│                                                                     │
│  Even if the refresh token hasn't expired — it can never be used   │
│  again. The access token is short-lived (24hr) and self-expires.   │
└─────────────────────────────────────────────────────────────────────┘
```

### Role-Based Route Protection in SecurityConfig

```java
// Simplified — actual implementation in SecurityConfig.java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/auth/**").permitAll()
    .requestMatchers("/rides/request", "/rides/history").hasRole("RIDER")
    .requestMatchers("/rides/accept/**", "/drivers/location/**").hasRole("DRIVER")
    .anyRequest().authenticated()
)
```

---

## 📍 Driver Location Tracking — How Haversine Works Here

The Haversine formula computes the shortest distance over the Earth's surface between two GPS coordinates. This is what powers the "find nearby drivers" feature.

### How it is implemented

Driver location updates arrive at `PATCH /drivers/location` and are persisted to the `driver_locations` table. When a rider requests a ride or wants to see nearby drivers, a native SQL query runs the Haversine calculation directly in MySQL — no post-processing in Java required.

```java
// DriverLocationRepository.java
@Query(value = """
    SELECT d.id, u.full_name, d.latitude, d.longitude,
    (6371 * acos(
        cos(radians(:lat)) * cos(radians(d.latitude)) *
        cos(radians(d.longitude) - radians(:lng)) +
        sin(radians(:lat)) * sin(radians(d.latitude))
    )) AS distance_km
    FROM driver_locations d
    JOIN users u ON d.driver_id = u.id
    HAVING distance_km < :radiusKm
    ORDER BY distance_km ASC
    """, nativeQuery = true)
List<NearbyDriverProjection> findNearbyDrivers(
    @Param("lat") double lat,
    @Param("lng") double lng,
    @Param("radiusKm") double radiusKm
);
```

The result is mapped to `NearbyDriverDTO` — `{ driverId, driverName, distanceKm }` — and returned to the rider sorted closest-first.

### Why native SQL instead of Java-side filtering?

Doing this in Java would mean loading ALL driver locations into memory first, then filtering. The native SQL approach pushes the distance math down to the database, which can index on coordinates and execute far more efficiently — exactly how production systems handle it.

---

## 📡 API Reference

### Base URL
```
http://localhost:8080
```

### 🔑 Auth Endpoints — Public

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| POST | /auth/register | Register a new user (RIDER or DRIVER) | 201 |
| POST | /auth/login | Login and receive access + refresh tokens | 200 |
| POST | /auth/refresh | Rotate refresh token, get new access token | 200 |
| POST | /auth/logout | Revoke refresh token (invalidate session) | 200 |
| GET | /auth/me | Get current authenticated user's profile | 200 |

### 🚖 Ride Endpoints — Token required

| Method | Endpoint | Role | Description | Status |
|--------|----------|------|-------------|--------|
| POST | /rides/request | RIDER | Request a new ride with pickup and drop coords | 201 |
| GET | /rides/{id} | RIDER/DRIVER | Get ride details by ID | 200 |
| PATCH | /rides/{id}/accept | DRIVER | Accept a REQUESTED ride | 200 |
| PATCH | /rides/{id}/start | DRIVER | Start the ride (ACCEPTED → ONGOING) | 200 |
| PATCH | /rides/{id}/end | DRIVER | End the ride (ONGOING → COMPLETED) | 200 |
| PATCH | /rides/{id}/cancel | RIDER/DRIVER | Cancel a ride before it starts | 200 |
| GET | /rides/my | RIDER | Get all rides for the current logged-in rider | 200 |
| GET | /rides/driver/my | DRIVER | Get all rides assigned to the current driver | 200 |

### 📍 Driver Location Endpoints — Token required

| Method | Endpoint | Role | Description | Status |
|--------|----------|------|-------------|--------|
| PATCH | /drivers/location | DRIVER | Update driver's current GPS coordinates | 200 |
| GET | /drivers/nearby | RIDER | Get all drivers within a given radius | 200 |

### 👤 User Endpoints — Token required

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| GET | /users/{id} | Get user profile by ID | 200 |
| GET | /users | Get all users (admin use) | 200 |

---

## 📦 Request and Response Examples

> Every response in this API follows the same contract — no matter the endpoint, no matter the error:
>
> **Success:**  `{ "success": true,  "message": "...", "data": { ... }, "error": null }`
> **Failure:**  `{ "success": false, "message": "...", "data": null,  "error": "..." }`

---

### Register as a Rider

```http
POST /auth/register
Content-Type: application/json
```
```json
{
    "fullName": "Aryan Sonawane",
    "email": "aryanvipinsonawane@gmail.com",
    "password": "Aryan@2008",
    "phone": "9876543210",
    "role": "RIDER"
}
```
**Response:**
```json
{
    "success": true,
    "message": "USER REGISTERED SUCCESSFULLY",
    "data": {
        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnlhbnZpcGluc29uYXdhbmVAZ21haWwuY29tIn0...",
        "refreshToken": "f3c2a1b0-9e8d-7c6b-5a4f-3e2d1c0b9a8f",
        "userId": 1,
        "role": "RIDER"
    },
    "error": null
}
```

---

### Login

```http
POST /auth/login
Content-Type: application/json
```
```json
{
    "email": "aryanvipinsonawane@gmail.com",
    "password": "Aryan@2008"
}
```
**Response:**
```json
{
    "success": true,
    "message": "LOGIN SUCCESSFUL",
    "data": {
        "message": "LOGIN SUCCESSFUL",
        "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
        "refreshToken": "f3c2a1b0-9e8d-7c6b-5a4f-3e2d1c0b9a8f",
        "userId": 1,
        "username": "Aryan Sonawane",
        "email": "aryanvipinsonawane@gmail.com",
        "role": "RIDER"
    },
    "error": null
}
```

> ⚠️ Save the `accessToken`. Every subsequent request needs:
> ```
> Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
> ```

---

### Refresh the Access Token

```http
POST /auth/refresh
Content-Type: application/json
```
```json
{
    "refreshToken": "f3c2a1b0-9e8d-7c6b-5a4f-3e2d1c0b9a8f"
}
```
**Response:**
```json
{
    "success": true,
    "message": "TOKEN REFRESHED SUCCESSFULLY",
    "data": {
        "accessToken": "eyJhbGciOiJIUzI1NiJ9.NEW_TOKEN...",
        "refreshToken": "a9b8c7d6-e5f4-3210-abcd-ef9876543210"
    },
    "error": null
}
```
> The old refresh token is now revoked. Use only the new one going forward.

---

### Logout

```http
POST /auth/logout
Content-Type: application/json
Authorization: Bearer <accessToken>
```
```json
{
    "refreshToken": "a9b8c7d6-e5f4-3210-abcd-ef9876543210"
}
```
**Response:**
```json
{
    "success": true,
    "message": "LOGGED OUT SUCCESSFULLY",
    "data": null,
    "error": null
}
```

---

### Request a Ride (Rider)

```http
POST /rides/request
Authorization: Bearer <accessToken>
Content-Type: application/json
```
```json
{
    "pickupLat": 18.5204,
    "pickupLng": 73.8567,
    "dropLat": 18.6298,
    "dropLng": 73.7997
}
```
**Response:**
```json
{
    "success": true,
    "message": "RIDE REQUESTED SUCCESSFULLY",
    "data": {
        "rideId": 42,
        "status": "REQUESTED",
        "pickupLat": 18.5204,
        "pickupLng": 73.8567,
        "dropLat": 18.6298,
        "dropLng": 73.7997,
        "rider": {
            "id": 1,
            "name": "Aryan Sonawane"
        },
        "driver": null,
        "fare": null,
        "createdAt": "2026-04-12T10:30:00"
    },
    "error": null
}
```

---

### Update Driver Location

```http
PATCH /drivers/location
Authorization: Bearer <driverAccessToken>
Content-Type: application/json
```
```json
{
    "latitude": 18.5220,
    "longitude": 73.8550
}
```
**Response:**
```json
{
    "success": true,
    "message": "LOCATION UPDATED SUCCESSFULLY",
    "data": {
        "driverId": 7,
        "latitude": 18.5220,
        "longitude": 73.8550,
        "updatedAt": "2026-04-12T10:32:14"
    },
    "error": null
}
```

---

### Find Nearby Drivers (Rider)

```http
GET /drivers/nearby?lat=18.5204&lng=73.8567&radiusKm=5
Authorization: Bearer <riderAccessToken>
```
**Response:**
```json
{
    "success": true,
    "message": "NEARBY DRIVERS FETCHED SUCCESSFULLY",
    "data": [
        {
            "driverId": 7,
            "driverName": "Raj Deshmukh",
            "distanceKm": 0.43
        },
        {
            "driverId": 12,
            "driverName": "Suresh Patil",
            "distanceKm": 1.87
        },
        {
            "driverId": 3,
            "driverName": "Mohan Kulkarni",
            "distanceKm": 3.21
        }
    ],
    "error": null
}
```

---

### Error Response Examples

**Wrong password:**
```json
{
    "success": false,
    "message": "INVALID PASSWORD",
    "data": null,
    "error": "401 UNAUTHORIZED"
}
```

**Attempting to use a revoked refresh token:**
```json
{
    "success": false,
    "message": "INVALID OR REVOKED REFRESH TOKEN",
    "data": null,
    "error": "401 UNAUTHORIZED"
}
```

**Driver trying to accept a ride that is already taken:**
```json
{
    "success": false,
    "message": "RIDE ALREADY ACCEPTED BY ANOTHER DRIVER",
    "data": null,
    "error": "400 BAD_REQUEST"
}
```

**Rider trying to cancel an ONGOING ride:**
```json
{
    "success": false,
    "message": "CANNOT CANCEL A RIDE THAT IS ALREADY ONGOING",
    "data": null,
    "error": "400 BAD_REQUEST"
}
```

---

## 🧠 Business Rules and Validations

All rules are enforced in the **service layer**. Controllers are intentionally dumb — they receive, delegate, and return.

### Ride Cannot Be Double-Accepted
```java
// RideService.java
if (!ride.getStatus().equals(RideStatus.REQUESTED)) {
    throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "RIDE ALREADY ACCEPTED BY ANOTHER DRIVER"
    );
}
```

### Ride State Machine Enforcement
```java
// RideService.java
if (!ride.getStatus().equals(RideStatus.ACCEPTED)) {
    throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "RIDE CANNOT BE STARTED — CURRENT STATUS: " + ride.getStatus()
    );
}
```

### Self-Ride Prevention
```java
// RideService.java
if (ride.getRider().getId().equals(ride.getDriver().getId())) {
    throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "DRIVER CANNOT RIDE THEIR OWN REQUEST"
    );
}
```

### Refresh Token Revocation Check
```java
// AuthService.java
if (refreshToken.isRevoked()) {
    throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "INVALID OR REVOKED REFRESH TOKEN"
    );
}
if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
    throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "REFRESH TOKEN EXPIRED"
    );
}
```

### Email Already Registered
```java
// AuthService.java
if (userRepository.existsByEmail(user.getEmail())) {
    throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "EMAIL ALREADY REGISTERED"
    );
}
```

---

## ⚠️ Error Handling

`GlobalExceptionHandler` with `@ControllerAdvice` catches all exceptions application-wide:

```java
@ExceptionHandler(ResponseStatusException.class)
public ResponseEntity<ApiResponse<Object>> handleResponseStatusException(
    ResponseStatusException ex
) {
    return ResponseEntity
        .status(ex.getStatusCode())
        .body(ApiResponse.error(ex.getReason(), ex.getStatusCode().toString()));
}

@ExceptionHandler(Exception.class)
public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error("SOMETHING WENT WRONG", ex.getMessage()));
}
```

### Complete Error Reference

| Scenario | HTTP Status | Message |
|----------|-------------|---------|
| Email not found | 404 NOT FOUND | USER NOT FOUND WITH EMAIL: {email} |
| User ID not found | 404 NOT FOUND | USER NOT FOUND WITH ID: {id} |
| Wrong password | 401 UNAUTHORIZED | INVALID PASSWORD |
| Email already exists | 400 BAD REQUEST | EMAIL ALREADY REGISTERED |
| No JWT token sent | 401 UNAUTHORIZED | UNAUTHORIZED. PLEASE LOGIN AND SEND A VALID TOKEN |
| Expired access token | 401 UNAUTHORIZED | ACCESS TOKEN EXPIRED |
| Invalid refresh token | 401 UNAUTHORIZED | INVALID OR REVOKED REFRESH TOKEN |
| Expired refresh token | 401 UNAUTHORIZED | REFRESH TOKEN EXPIRED |
| Ride not found | 404 NOT FOUND | RIDE NOT FOUND WITH ID: {id} |
| Ride already accepted | 400 BAD REQUEST | RIDE ALREADY ACCEPTED BY ANOTHER DRIVER |
| Invalid ride state transition | 400 BAD REQUEST | RIDE CANNOT BE STARTED — CURRENT STATUS: {status} |
| Cannot cancel ongoing ride | 400 BAD REQUEST | CANNOT CANCEL A RIDE THAT IS ALREADY ONGOING |
| Unauthorized role for endpoint | 403 FORBIDDEN | ACCESS DENIED |
| Unhandled exception | 500 INTERNAL SERVER ERROR | SOMETHING WENT WRONG |

---

## 📐 DTOs — Why We Never Expose Raw Entities

JPA entities contain hashed passwords, lazy-loaded relationships, and internal implementation details. None of that belongs in an HTTP response. Every response in this API uses a DTO — a plain class with exactly the fields the client needs, nothing more.

| DTO | Purpose |
|-----|---------|
| `LoginRequestDTO` | What the client sends to login: email + password |
| `AuthResponseDTO` | Returned on register/login: accessToken + refreshToken + userId + role |
| `LoginResponseDTO` | Returned from `/auth/login`: full login context |
| `RefreshTokenRequestDTO` | What the client sends to `/auth/refresh`: refreshToken string |
| `DriverLocationDTO` | What a driver sends to update location: latitude + longitude |
| `NearbyDriverDTO` | What a rider receives: driverId + driverName + distanceKm |
| `RideRequestDTO` | What a rider sends to request a ride: pickup and drop coordinates |
| `RideResponseDTO` | Complete ride details: status, fare, rider info, driver info, timestamps |

---

## ⚙️ Getting Started — Run it Locally

### Prerequisites

- **Java 21** — [Download](https://www.oracle.com/java/technologies/downloads/)
- **MySQL 8.0** — [Download](https://dev.mysql.com/downloads/)
- **Maven 3.x** — [Download](https://maven.apache.org/download.cgi)
- **Postman** — [Download](https://www.postman.com/downloads/)
- **IntelliJ IDEA** — [Download](https://www.jetbrains.com/idea/) (recommended)

### Step 1 — Clone the repository

```bash
git clone https://github.com/Xaryansonawane/UberApp.git
cd UberApp
```

### Step 2 — Create the MySQL database

```sql
CREATE DATABASE UberApp_DB;
```

Hibernate will auto-create all tables on first run.

### Step 3 — Configure `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/UberApp_DB?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false

server.port=8080

# JWT Configuration
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000
jwt.refresh-expiration=604800000
```

### Step 4 — Run the application

**IntelliJ IDEA:** Open the project, click the green Run button, wait for `Started UberAppApplication` in the console.

**Terminal:**
```bash
mvn spring-boot:run
```

### Step 5 — Test the API

Register a rider:
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test Rider","email":"rider@gmail.com","password":"Test@123","phone":"9999999999","role":"RIDER"}'
```

Register a driver:
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test Driver","email":"driver@gmail.com","password":"Test@123","phone":"8888888888","role":"DRIVER"}'
```

---

## ⚙️ Configuration Reference

| Property | Default | Description |
|----------|---------|-------------|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/UberApp_DB` | MySQL connection string |
| `spring.datasource.username` | `root` | MySQL username |
| `spring.datasource.password` | — | MySQL password |
| `spring.jpa.hibernate.ddl-auto` | `update` | Auto-creates and updates tables |
| `spring.jpa.show-sql` | `true` | Logs all SQL to console |
| `spring.jpa.open-in-view` | `false` | Disables OSIV warning |
| `server.port` | `8080` | HTTP port |
| `jwt.secret` | — | Secret key for HS256 signing |
| `jwt.expiration` | `86400000` | Access token lifetime (24 hours in ms) |
| `jwt.refresh-expiration` | `604800000` | Refresh token lifetime (7 days in ms) |

---

## 🛣️ Roadmap

Features planned for future versions:

- [ ] **Fare Calculation Engine** — Dynamic pricing based on distance and surge factor
- [ ] **Ride Rating System** — Riders and drivers rate each other after each trip
- [ ] **WebSocket Real-Time Tracking** — Live driver position updates pushed to the rider's screen via STOMP
- [ ] **Pagination** — For ride history and driver lists using Spring `Pageable`
- [ ] **Wallet / Payment Ledger** — Track payments per ride, maintain rider and driver balance
- [ ] **Notification System** — Push driver alerts on new ride requests
- [ ] **Admin Dashboard Endpoints** — Manage users, view all rides, suspend accounts
- [ ] **Bean Validation** — `@Valid` on all request bodies for field-level validation
- [ ] **Swagger / OpenAPI Docs** — Auto-generated interactive API documentation
- [ ] **Docker + docker-compose** — One-command local setup
- [ ] **Unit and Integration Tests** — JUnit 5 + MockMvc coverage for services and controllers
- [ ] **Duplicate Follow Prevention** — Mirror the duplicate like guard from the SocialApp project

---

## 👨‍💻 Author

**Aryan Sonawane**

A 3rd year Computer Engineering student from Pune building production-quality backend systems with Java and Spring Boot. RideFlow is the second project in a series of real-world REST API backends — the first being the [Social-Media-REST-API](https://github.com/Xaryansonawane/Social-Media-REST-API).

- GitHub: [@Xaryansonawane](https://github.com/Xaryansonawane)
- LeetCode: [Xaryansonawane](https://leetcode.com/u/Xaryansonawane/)
- HackerRank: [Xaryansonawane](https://www.hackerrank.com/profile/Xaryansonawane)
- Instagram: [@Xaryansonawane](https://instagram.com/Xaryansonawane)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

⭐ If this project helped you understand how production Spring Boot backends are structured, a star means a lot — it tells others this is worth looking at.
