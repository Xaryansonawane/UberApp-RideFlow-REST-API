# 🤝 Contributing to RideFlow-REST-API

First off — thank you for taking the time to read this. Whether you are fixing a typo, reporting a bug, or adding a full feature, every contribution matters and is genuinely appreciated.

This document explains how to contribute to this project in a way that keeps the codebase clean, consistent, and easy for everyone to work with.

---

## 📋 Table of Contents

- [Code of Conduct](#-code-of-conduct)
- [Before You Start](#-before-you-start)
- [How to Contribute](#-how-to-contribute)
- [Setting Up the Development Environment](#-setting-up-the-development-environment)
- [Project Conventions](#-project-conventions)
- [Submitting a Pull Request](#-submitting-a-pull-request)
- [Reporting Bugs](#-reporting-bugs)
- [Suggesting Features](#-suggesting-features)
- [Commit Message Guidelines](#-commit-message-guidelines)
- [What Not to Do](#-what-not-to-do)

---

## 📜 Code of Conduct

This project follows a simple rule: **be respectful**. Everyone contributing here — regardless of experience level — deserves to be treated with patience and kindness. Dismissive comments, gatekeeping, and personal attacks will not be tolerated.

If you are new to Spring Boot or open source, you are still welcome here. Ask questions. No one expects you to know everything.

---

## 🧭 Before You Start

Before writing any code, please do the following:

**Check existing issues first.** Someone may have already reported the bug or suggested the feature you have in mind. Search [GitHub Issues](https://github.com/Xaryansonawane/UberApp/issues) before opening a new one.

**Open an issue before a big PR.** If you want to add a new feature or make a significant change, open an issue and describe what you plan to do. This avoids the situation where you spend hours building something that doesn't align with the project's direction.

**For small fixes** — typos, broken links, minor bugs — you can go straight to a pull request without an issue.

---

## 🛠️ How to Contribute

### 1. Fork the repository

Click the **Fork** button at the top right of this page. This creates your own copy of the repository under your GitHub account.

### 2. Clone your fork

```bash
git clone https://github.com/YOUR_USERNAME/UberApp.git
cd UberApp
```

### 3. Add the original repo as upstream

This lets you pull in future changes from the main repository:

```bash
git remote add upstream https://github.com/Xaryansonawane/UberApp.git
```

### 4. Create a new branch

Never work directly on `main`. Create a branch named after what you are doing:

```bash
git checkout -b fix/refresh-token-expiry-check
# or
git checkout -b feature/ride-rating-system
# or
git checkout -b docs/update-api-reference
```

Branch naming convention:

| Prefix | When to use |
|--------|-------------|
| `feature/` | Adding new functionality |
| `fix/` | Fixing a bug |
| `docs/` | Documentation changes only |
| `refactor/` | Code restructuring without behavior change |
| `test/` | Adding or fixing tests |
| `chore/` | Build scripts, configs, dependencies |

### 5. Make your changes

Write your code. Follow the [Project Conventions](#-project-conventions) below.

### 6. Test your changes

Run the application and verify your change works end to end. Test with Postman. Make sure existing endpoints still behave correctly after your change.

### 7. Commit your changes

Follow the [Commit Message Guidelines](#-commit-message-guidelines).

```bash
git add .
git commit -m "fix: validate refresh token expiry before rotation"
```

### 8. Push to your fork

```bash
git push origin fix/refresh-token-expiry-check
```

### 9. Open a Pull Request

Go to your fork on GitHub and click **Compare & pull request**. Fill in the PR template clearly and submit.

---

## 💻 Setting Up the Development Environment

### What you need

- Java 21
- MySQL 8.0
- Maven 3.x
- IntelliJ IDEA (recommended) or any IDE with Spring Boot support

### Setup steps

**Step 1 — Create the database**
```sql
CREATE DATABASE UberApp_DB;
```

**Step 2 — Configure `application.properties`**

Copy the example config and fill in your local credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/UberApp_DB?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
server.port=8080
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000
jwt.refresh-expiration=604800000
```

**Step 3 — Run the application**
```bash
mvn spring-boot:run
```

The API will be live at `http://localhost:8080`.

---

## 📐 Project Conventions

These conventions are non-negotiable. All contributions must follow them — this is what keeps the codebase readable and consistent.

### Package Structure

Every new file must go into the correct package. No exceptions.

```
com.example.UberApp.dto         → DTOs only
com.example.UberApp.controller  → Controllers only
com.example.UberApp.security    → JWT and security classes only
com.example.UberApp.model       → JPA entities only
com.example.UberApp.service     → Service classes only
com.example.UberApp.repository  → Repository interfaces only
```

Do not create new top-level packages without discussing it in an issue first.

### Error Messages — ALL CAPS

Every error message thrown via `ResponseStatusException` must be in ALL CAPS:

```java
// ✅ Correct
throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND WITH ID: " + id);

// ❌ Wrong
throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
```

This is a project-wide convention inherited from the Social-Media-REST-API and must be maintained consistently.

### DTOs for everything

Raw JPA entities must never appear in controller method signatures as return types, and they must never be serialized directly into HTTP responses.

```java
// ✅ Correct — return a DTO
public ApiResponse<RideResponseDTO> getRide(Long id) { ... }

// ❌ Wrong — returning raw entity
public ApiResponse<Ride> getRide(Long id) { ... }
```

### ApiResponse wrapper — always

Every controller method must return `ApiResponse<T>`. No endpoint should ever return raw data outside the wrapper.

```java
// ✅ Correct
return ResponseEntity.ok(ApiResponse.success("RIDE FETCHED SUCCESSFULLY", rideDTO));

// ❌ Wrong
return ResponseEntity.ok(rideDTO);
```

### Business logic belongs in services

Controllers must not contain any business logic. They receive the request, call the service, and return the response. All validation, rule enforcement, state transitions, and DTO mapping happen in the service layer.

```java
// ✅ Correct controller method
@PostMapping("/request")
public ResponseEntity<ApiResponse<RideResponseDTO>> requestRide(
    @RequestBody RideRequestDTO dto,
    Authentication auth
) {
    RideResponseDTO ride = rideService.requestRide(dto, auth.getName());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success("RIDE REQUESTED SUCCESSFULLY", ride));
}
```

### Naming conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Classes | PascalCase | `RideService`, `AuthController` |
| Methods | camelCase | `requestRide()`, `validateToken()` |
| Variables | camelCase | `driverId`, `refreshToken` |
| Constants | UPPER_SNAKE_CASE | `MAX_RIDE_RADIUS_KM` |
| Database columns | snake_case | `driver_id`, `created_at` |
| REST endpoints | kebab-case | `/rides/nearby-drivers` |
| Branch names | kebab-case with prefix | `fix/token-revocation` |

---

## 🔃 Submitting a Pull Request

When you open a PR, please include the following in the description:

**What does this PR do?**
A clear, one or two sentence summary of the change.

**Why is this change needed?**
What problem does it solve or what improvement does it bring?

**How was it tested?**
Describe how you verified the change works. Mention which endpoints you tested and with what inputs.

**Checklist before submitting:**

- [ ] My code follows the package structure convention
- [ ] Error messages are in ALL CAPS
- [ ] No raw entities in controller return types or HTTP responses
- [ ] All controller methods return `ApiResponse<T>`
- [ ] Business logic is in the service layer, not the controller
- [ ] I have tested the change manually with Postman
- [ ] My commit messages follow the conventional commits format
- [ ] No `System.out.println` or debug statements left in the code
- [ ] I have not committed `application.properties` with real credentials

### PR review process

The maintainer will review your PR as soon as possible. You may receive requests for changes — please respond to them constructively. Once approved, your PR will be squash-merged into `main`.

---

## 🐛 Reporting Bugs

A good bug report lets the maintainer reproduce the problem without guessing. Please include:

**The endpoint and HTTP method**
e.g. `POST /auth/refresh`

**The request body or headers you sent**
```json
{
    "refreshToken": "some-token-value"
}
```

**The response you received**
```json
{
    "success": false,
    "message": "SOMETHING WENT WRONG",
    "data": null,
    "error": "500 INTERNAL_SERVER_ERROR"
}
```

**What you expected to happen**
e.g. "I expected a new access token and a rotated refresh token."

**Your environment**
Java version, MySQL version, OS, Spring Boot version.

Use the [Bug Report issue template](https://github.com/Xaryansonawane/UberApp/issues/new) when available.

---

## 💡 Suggesting Features

Feature suggestions are welcome. When opening a feature request, please explain:

**What problem does this solve?**
Real features solve real problems. Explain what is currently difficult or impossible to do.

**What would the solution look like?**
Describe the new endpoint, entity, or behavior at a high level.

**Is this in scope for this project?**
RideFlow is a backend REST API for a ride-hailing platform. Features should fit naturally within that domain — ride management, driver tracking, user accounts, payments, ratings, notifications.

Things that are likely out of scope: frontend code, mobile app features, third-party payment gateway integrations that require API keys you cannot share.

---

## 📝 Commit Message Guidelines

This project follows [Conventional Commits](https://www.conventionalcommits.org/).

### Format

```
<type>(<scope>): <short description>
```

### Types

| Type | When to use |
|------|-------------|
| `feat` | A new feature |
| `fix` | A bug fix |
| `docs` | Documentation changes only |
| `refactor` | Code change that neither fixes a bug nor adds a feature |
| `test` | Adding or updating tests |
| `chore` | Dependency updates, build config, CI changes |
| `style` | Formatting, whitespace — no logic change |

### Examples

```bash
feat(auth): add refresh token rotation on /auth/refresh

fix(ride): prevent driver from accepting an already accepted ride

docs(readme): add Haversine query explanation section

refactor(service): extract fare calculation into separate method

chore(deps): upgrade JJWT from 0.11.5 to 0.12.3

test(auth): add unit test for revoked refresh token scenario
```

**Rules:**
- Use the imperative mood: "add feature" not "added feature" or "adds feature"
- Keep the subject line under 72 characters
- Reference the relevant issue number in the body if applicable: `Closes #12`
- Do not end the subject line with a period

---

## 🚫 What Not to Do

Please avoid the following — PRs that contain any of these will be asked to revise before merging:

**Do not commit sensitive credentials.** Never commit a real `jwt.secret`, database password, or any API key. If you accidentally do this, contact the maintainer immediately.

**Do not reformat the entire codebase in one PR.** Formatting changes mixed with logic changes make reviews very difficult. Keep them separate.

**Do not add dependencies without discussion.** Adding a new library changes the project's footprint for everyone. Open an issue first.

**Do not bypass the DTO layer.** If you are returning a raw entity from a controller, the PR will not be merged.

**Do not add TODO comments and leave them.** Either implement it or open an issue for it.

**Do not open a PR to `main` from another project's branch.** Make sure your changes are scoped to this project only.

---

## ❓ Questions?

If you are unsure about anything — how to set up the project, whether your idea fits the roadmap, how to structure a new feature — just open an issue with the `question` label or reach out via the contact links on the [author's GitHub profile](https://github.com/Xaryansonawane).

There are no stupid questions here. Ask away.

---

Thank you for contributing. Every PR, every issue, every suggestion makes this project better. 🙌
