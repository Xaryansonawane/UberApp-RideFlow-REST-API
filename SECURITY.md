# 🔒 Security Policy

---

## 🚗 UberApp REST API – Security

A fully functional, production-structured **RESTful backend** for an Uber-like ride booking platform built with **Spring Boot 3.5.11, Spring Security, JWT Authentication, Spring Data JPA, and MySQL**.

This document explains how security is handled in this project and how vulnerabilities should be reported responsibly.

---

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.11-brightgreen?style=flat-square&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-blue?style=flat-square&logo=springsecurity)
![JWT](https://img.shields.io/badge/JWT-Authentication-purple?style=flat-square&logo=jsonwebtokens)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![Maven](https://img.shields.io/badge/Maven-3.x-red?style=flat-square&logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

---

## 📋 Table of Contents

- [Supported Versions](#-supported-versions)
- [Security Architecture Overview](#-security-architecture-overview)
- [Known Security Boundaries](#-known-security-boundaries)
- [Reporting a Vulnerability](#-reporting-a-vulnerability)
- [What Happens After You Report](#-what-happens-after-you-report)
- [Security Best Practices for Deployment](#-security-best-practices-for-deployment)
- [Dependency Security](#-dependency-security)
- [Out of Scope](#-out-of-scope)

---

## ✅ Supported Versions

Only the latest version on the `main` branch receives security fixes.

| Branch | Supported |
|--------|-----------|
| `main` | ✅ Yes — active development and security fixes |
| Any other branch | ❌ No — development or experimental only |

> Always use the latest version to stay secure.

---

## 🏛️ Security Architecture Overview

### Authentication — JWT Token System

UberApp uses a **stateless JWT access token** for all protected routes. Tokens are signed using HMAC-SHA256 and validated on every request by `JwtAuthenticationFilter`.

```
ACCESS TOKEN
  ├── Algorithm    : HMAC-SHA256 (HS256)
  ├── Signed with  : jwt.secret (from application.yml)
  ├── Lifetime     : 24 hours (86400000 ms)
  ├── Storage      : Client-side only — never persisted on server
  └── Revocation   : Cannot be revoked mid-life — expires naturally
```

### Authorization — Role-Based Access Control

Every user is assigned a role at registration. Spring Security's filter chain restricts routes by role:

```
UserRole
  ├── USER    → Can request rides, pay, rate drivers, use wallet
  ├── DRIVER  → Can accept rides, go online/offline, view earnings
  └── ADMIN   → Can approve drivers, block users, view all stats
```

```
PUBLIC (no token required)
  └── POST /api/auth/register
  └── POST /api/auth/login
  └── GET  /api/files/**

AUTHENTICATED (valid JWT required)
  └── All /api/rides/**
  └── All /api/payments/**
  └── All /api/driver/**
  └── All /api/wallet/**
  └── All /api/rating/**
  └── All /api/scheduled-rides/**
  └── All /api/invoice/**
  └── All /api/cancellation/**

ADMIN only
  └── GET /api/admin/**
  └── PUT /api/admin/block/**
  └── PUT /api/admin/unblock/**
  └── PUT /api/cancellation/reset/**
```

Any request to a protected route without a valid Bearer token is rejected at the filter level with `401 UNAUTHORIZED` before reaching any controller.

### Password Storage

Passwords are hashed using **BCrypt** before being stored. The raw password is never logged, stored, or returned in any API response.

```java
// Encoding on register
user.setPassword(passwordEncoder.encode(request.getPassword()));
```

```java
// Hidden from all responses
@JsonProperty(access = WRITE_ONLY)
private String password;
```

### What is explicitly NOT stored

- Raw passwords — ever
- The JWT secret in any response
- Access tokens on the server side — they are stateless
- Personally identifiable information beyond what the platform requires

---

## 🛡️ Security Features

### 🔄 Business Logic Protection

UberApp enforces strict rules at the service layer to prevent abuse:

- ❌ **Cancellation block** — users with 3+ cancellations cannot request rides
- ❌ **Penalty system** — ₹50 auto-deducted from wallet on ride cancellation
- ❌ **Driver approval gate** — unapproved drivers cannot go online or accept rides
- ❌ **Duplicate ride prevention** — rider cannot have two active rides simultaneously
- ❌ **Duplicate rating prevention** — only one rating allowed per ride
- ❌ **Wallet overdraft prevention** — deduction blocked if balance is insufficient
- ❌ **Scheduled ride validation** — must be booked at least 30 minutes in future
- ❌ **Duplicate registration prevention** — email and phone must be unique

### 🧠 Input Validation

All request bodies are validated before processing:

- `@NotBlank` — rejects empty or whitespace-only strings
- `@NotNull` — rejects null values
- `@Email` — enforces valid email format
- `@Min(1) @Max(5)` — enforces star rating bounds

HTTP status codes returned:

| Status | Meaning |
|--------|---------|
| `400` | Invalid input / bad request |
| `401` | Missing or invalid JWT token |
| `404` | Resource not found |
| `500` | Unexpected server error |

### 📁 File Upload Security

- Only image files accepted — validated by MIME type:

```java
contentType.startsWith("image/")
```

- Max file size: **10MB** per request, **5MB** per image
- Unique filenames generated via `UUID` — prevents overwrite attacks
- Files stored in server-local `uploads/` directory — not publicly accessible without auth

### ⚠️ Global Exception Handling

Centralized error handling via `@RestControllerAdvice` in `GlobalExceptionHandler`:

- Prevents stack traces or internal data from leaking to clients
- All exceptions return consistent structured JSON:

```json
{
  "success": false,
  "message": "ERROR MESSAGE",
  "data": null
}
```

### 📦 DTO Security

- No raw `@Entity` objects exposed in responses
- Sensitive fields hidden using `@JsonProperty(access = WRITE_ONLY)`
- All responses wrapped in `ApiResponse<T>` for consistency

---

## ⚠️ Known Security Boundaries

These are intentional design decisions — not vulnerabilities.

**Access tokens cannot be revoked before expiry.**
Because tokens are stateless, there is no server-side mechanism to invalidate them mid-life. If a token is compromised, it remains valid until its 24-hour window closes. Reducing `jwt.expiration` in config mitigates this.

**JWT secret is symmetric (HS256).**
This project uses a shared-secret signing algorithm. For a single-service backend this is standard and acceptable. For multi-service production environments, RS256 with asymmetric keys is preferred.

**No rate limiting implemented.**
The `/api/auth/login` and `/api/auth/register` endpoints are not rate-limited. Brute-force protection must be added at the infrastructure level (Nginx, API Gateway, or Bucket4j) before production deployment.

**No HTTPS enforcement in the application layer.**
TLS termination is expected to be handled at the infrastructure layer (Nginx, cloud load balancer). Running over plain HTTP in production exposes tokens in transit.

**Haversine distance query uses raw coordinate input.**
The nearest driver search accepts latitude and longitude as query parameters. These are cast to `double` and passed to parameterized queries — SQL injection is not possible, but no geographic bounds validation is applied.

**No refresh tokens.**
There is a single 24-hour access token. Once issued it cannot be renewed — users must log in again after expiry.

---

## 🚨 Reporting a Vulnerability

**Please do not open a public GitHub issue for security vulnerabilities.**

Public issues are visible immediately. A disclosed vulnerability with no fix puts every person running this codebase at risk. Report privately first.

### How to report

Send an email to:

```
aryanvipinsonawane@gmail.com
```

Use the subject line:
```
[SECURITY] UberApp-REST-API — <brief description>
```

### What to include

**Affected component**
Which endpoint, class, or feature is affected? e.g. `POST /api/auth/login`, `JwtService.java`, `RideService.java`

**Description**
What is the security issue and what is the root cause?

**Steps to reproduce**
Exact request — method, URL, headers, body — and the observed response.

**Impact assessment**
What can an attacker do? Access other users' data? Bypass authentication? Manipulate rides or payments?

**Suggested fix (optional)**
Not required but very helpful.

---

## 🔄 What Happens After You Report

| Timeline | What happens |
|----------|-------------|
| Within 48 hours | Acknowledgement — report received and under review |
| Within 7 days | Initial severity assessment and fix timeline communicated |
| After fix | Merged to `main`, you are credited, brief public disclosure made |
| If invalid | Clear explanation of why the behavior is intentional or out of scope |

There is no bug bounty program. What you get is genuine credit and appreciation.

---

## 🛡️ Security Best Practices for Deployment

### Protect the JWT secret

The `jwt.secret` in `application.yml` signs all access tokens. Anyone who obtains it can forge valid tokens for any user.

- Never commit `application.yml` with a real secret to version control
- Use environment variables or a secrets manager in production
- Use a cryptographically strong secret — minimum 256 bits of entropy

```bash
# Generate a strong secret
openssl rand -hex 64
```

### Use HTTPS in production

Never expose this API over plain HTTP beyond `localhost`. Use Nginx or a cloud load balancer to terminate TLS. All tokens travel in HTTP headers — without TLS they are visible to anyone on the network path.

### Reduce access token lifetime for sensitive deployments

The default 24-hour expiry is convenient but means a leaked token stays valid for a full day. For higher-security environments reduce this:

```yaml
jwt:
  expiration: 900000   # 15 minutes
```

### Restrict database access

The MySQL user should only have the minimum permissions required:

```sql
CREATE USER 'uberapp_user'@'localhost' IDENTIFIED BY 'strong_password';
GRANT SELECT, INSERT, UPDATE, DELETE ON UberApp_DB.* TO 'uberapp_user'@'localhost';
FLUSH PRIVILEGES;
```

Do not use `root` in production.

### Add rate limiting before going public

Add brute-force protection to at minimum `/api/auth/login` and `/api/auth/register`. Options:

- **Bucket4j** — in-process rate limiting for Spring Boot
- **Nginx `limit_req`** — rate limit at the reverse proxy level
- **API Gateway** — AWS API Gateway or Kong

### Disable SQL logging in production

```yaml
spring:
  jpa:
    show-sql: false
```

Logging SQL queries in production is a security and performance concern.

---

## 📦 Dependency Security

| Dependency | Version | Notes |
|------------|---------|-------|
| `spring-boot-starter-security` | 3.5.11 | Spring Security 6 — actively maintained |
| `jjwt-api` | 0.11.5 | JJWT — HS256 token signing and validation |
| `spring-boot-starter-data-jpa` | 3.5.11 | Hibernate ORM — parameterized queries throughout |
| `mysql-connector-j` | 9.6.0 | MySQL JDBC driver |
| `bcrypt` (via Spring Security) | — | Password hashing — built into Spring Security |
| `spring-boot-starter-validation` | 3.5.11 | Bean validation — `@NotBlank`, `@Email`, `@Min`, `@Max` |

### Keeping dependencies up to date

Run this periodically to check for known CVEs in your dependency tree:

```bash
mvn dependency:tree
mvn org.owasp:dependency-check-maven:check
```

To add the OWASP plugin:

```xml
<plugin>
    <groupId>org.owasp</groupId>
    <artifactId>dependency-check-maven</artifactId>
    <version>9.0.9</version>
</plugin>
```

---

## 🚫 Out of Scope

The following are not considered vulnerabilities in the context of this project:

- Issues that require physical access to the server or database
- Issues that only affect a local development environment with default credentials
- Theoretical attacks with no demonstrated exploit path
- Missing security headers (CSP, HSTS, X-Frame-Options) — this is a REST API, not a web application
- Rate limiting — acknowledged as a known limitation, documented above
- The fact that access tokens cannot be revoked mid-life — intentional stateless design
- Security issues in third-party services or infrastructure not maintained by this project
- `spring.jpa.show-sql=true` in development config — expected in dev mode only

---

## 🔐 Best Practices Followed

- Principle of Least Privilege on all roles
- Stateless authentication — no session state on server
- Layered architecture: Controller → Service → Repository
- Input validation on all request bodies and parameters
- No sensitive data exposure in any API response
- Consistent response format across all endpoints via `ApiResponse<T>`
- Parameterized queries via Spring Data JPA — SQL injection not possible
- UUID-based filenames on upload — no path traversal or overwrite possible
- Business logic protection at service layer — not just at controller level

---

## 👨‍💻 Maintainer

**Aryan Sonawane**
- GitHub: [@Xaryansonawane](https://github.com/Xaryansonawane)
- Email: aryanvipinsonawane@gmail.com

---

*This security policy was last reviewed: April 2026*
