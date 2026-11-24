# Java Engineering Interview Kit

A small, runnable Java 21 application for a **60-minute adaptive live coding interview**. Candidates improve an existing synthetic investment ledger rather than assemble a project. V0.1 is a management demonstration starter; the interview process remains a proposal for review.

This personal project contains original code and fictional records. It uses no employer code, data, credentials, infrastructure, or internal requirements.

## Before cloning the repository <- these are instructions for the candidate

Prepare your development environment before the interview so the session can focus on coding rather than installation. Preparation means getting the starter running; leave the exercise changes for the interview unless your interviewer gives different instructions.

### Required tools and access

| Requirement | What to prepare |
|---|---|
| Git | Install Git and confirm `git --version` works in your terminal. |
| Java Development Kit 21 | Install a full **JDK 21**, including the Java compiler. Both `java -version` and `javac -version` must report version 21. |
| Editor or IDE | Use an editor you are comfortable with that supports Java and Gradle projects. Set both the project SDK and the Gradle JVM to JDK 21. |
| Terminal and browser | Be able to run commands, open the local Swagger UI, and share your editor/terminal during the session if requested. |
| Internet access | Allow the first build to download Gradle and dependencies from public repositories. Complete that download before the interview. |
| Repository access | Obtain the GitHub repository URL and any assigned branch or tag from your interviewer. If the repository is private, accept the invitation and verify access with your own GitHub account before cloning. |

Run these checks **before downloading the project**:

```text
git --version
java -version
javac -version
```

If Java reports another version, select JDK 21 in your shell. If `JAVA_HOME` is set, it must point to the JDK 21 installation directory, and your `PATH` must resolve its `bin` directory. Reopen the terminal and check again. The IDE's Java settings and the terminal's Java settings may need to be configured separately.

You do **not** need to install Gradle, Maven, PostgreSQL, or Docker for the standard interview. The repository includes the Gradle Wrapper, and the default database is an automatically initialized, in-memory H2 instance. No cloud account, external authentication provider, paid service, or custom database credentials are required. Docker and PostgreSQL are optional unless your interviewer explicitly assigns database-specific work.

### Confirm the interview conditions

Before the session, confirm the assigned repository revision, the expected interview duration, and whether the session uses independent or AI-assisted coding. Follow the communicated AI policy, including any generative IDE features. The proposed process allows official documentation; see [Interview process](docs/INTERVIEW_PROCESS.md) for the two proposed modes. Your interviewer must communicate which mode applies.

Let your interviewer know in advance about installation or repository-access problems, network restrictions, or any accommodations you need. Candidates are not expected to complete every exercise or build the application from scratch.

## Clone and verify the starter

Clone the starter using the URL below, unless your interviewer supplies a different fork or exercise variant. For a private repository, use your configured GitHub credentials or SSH key.

```bash
git clone https://github.com/mregus/java-engineering-interview-kit.git
cd java-engineering-interview-kit
```

If a specific branch or tag was assigned, use `git clone --branch ASSIGNED_REF https://github.com/mregus/java-engineering-interview-kit.git` instead, replacing `ASSIGNED_REF` with the assigned branch or tag. Follow your interviewer's instructions about forks, working branches, and submitting changes.

Open this directory as a Gradle project in your IDE and allow dependency import to finish. Run all following commands from the project directory.

On macOS or Linux:

```bash
./gradlew --version
./gradlew clean test
./gradlew bootRun
```

On Windows PowerShell:

```powershell
.\gradlew.bat --version
.\gradlew.bat clean test
.\gradlew.bat bootRun
```

The wrapper version output should show Java 21 for the launcher and daemon JVMs. Baseline tests should finish with `BUILD SUCCESSFUL`. Application startup should report `Started LedgerApplication`. The first build can take longer while dependencies download.

If macOS or Linux reports `Permission denied` for the wrapper, run `chmod +x gradlew` and retry. If dependency downloads fail, check your network or proxy configuration and contact your interviewer if the problem persists. Avoid changing application code to fix an environment problem.

In another terminal:

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/api/clients
curl http://localhost:8080/api/clients/1/funds
curl http://localhost:8080/api/clients/1/transactions
```

Open [Swagger UI](http://localhost:8080/swagger-ui/index.html) or the [OpenAPI JSON](http://localhost:8080/v3/api-docs). Stop with Ctrl-C. To use another port: `./gradlew bootRun --args='--server.port=8081'`. The app binds to localhost by default. The H2 database is in memory and resets when the application JVM exits.

Before the interview, confirm that the health endpoint returns `"status":"UP"`, the client list contains the synthetic clients, and Swagger UI opens. **Exercise tests intentionally fail in the starter**, and the unfinished reporting endpoint returns 501; these are assigned tasks, not setup failures. Keep a clean starting copy and avoid changing fixtures or solving exercises during environment preparation unless instructed.

Build an executable jar:

```bash
./gradlew bootJar
java -jar build/libs/java-engineering-interview-kit-0.1.0.jar
```

## Candidate workflow (I think keeping this to 3 to 4 exercises max is more than ok)

1. Run the baseline tests and read the assigned exercise before editing.
2. Explain your assumptions, make a focused change, and add meaningful tests.
3. Run the selected public exercise checks and rerun the baseline suite.
4. Explain what you verified and what remains uncertain. Completing every exercise is not expected.

| Exercise | Task | Command | Starter behavior |
|---|---|---|---|
| [J1](exercises/J1.md) | Fix balance direction | `./gradlew exerciseJ1` | All amounts are incorrectly added |
| [J2](exercises/J2.md) | Investor validation and HTTP semantics | `./gradlew exerciseJ2` | Invalid blanks/emails accepted; success uses 200 without Location |
| [M2](exercises/M2.md) | Fund report | `./gradlew exerciseM2` | Valid report returns 501 |

These checks **intentionally fail** before implementation and are excluded from `test`, `check`, and the CI baseline. Equivalent selection: `./gradlew exerciseTest -Pexercise=J1` (also `J2` or `M2`). All three exercises are independent. J1 is a service-level exercise with no balance HTTP endpoint; M2 must not call J1's defective service. Public tests are examples, not a complete specification.

M1, M3, S1, S2, and S3 are [planned exercises](docs/ROADMAP.md); their implementations and task aliases are not supplied in V0.1.

## Working baseline

Client-scoped fund, investor, participation, and transaction APIs; database integrity constraints; centralized ProblemDetail errors; synthetic fixtures; atomic transaction/audit creation; health and API documentation; H2 baseline tests and opt-in real PostgreSQL checks.

Every scoped path starts `/api/clients/{clientId}`:

| Method | Path | Result |
|---|---|---|
| GET | `/api/clients` | Clients |
| GET | `/api/clients/{clientId}` | Client |
| POST, GET | `…/funds` | Create/list funds |
| GET | `…/funds/{fundId}` | Fund |
| POST, GET | `…/investors` | Create/list investors (POST includes J2 defects) |
| GET | `…/investors/{investorId}` | Investor |
| POST | `…/funds/{fundId}/investors/{investorId}` | Create participation |
| GET | `…/funds/{fundId}/investors` | Participating investors, including those with no transactions |
| GET | `…/investors/{investorId}/funds` | Participating funds |
| POST, GET | `…/transactions` | Create/list transactions |
| GET | `…/transactions/{transactionId}` | Transaction |
| GET | `…/funds/{fundId}/report?from=2026-03-01&to=2026-03-31` | M2 exercise |

Read success is 200; creation success is 201 plus Location except for the explicit J2 defect. Missing and cross-client resources return 404; duplicates and missing participation return 409; invalid ordinary requests return 400. No update/delete API is implemented. Transaction search is currently a deterministic, unpaginated client list; filters belong to planned M1.

Example financial write (all fields and amounts are synthetic):

```bash
curl -i -X POST http://localhost:8080/api/clients/1/transactions \
  -H 'Content-Type: application/json' \
  -d '{"fundId":2,"investorId":3,"transactionType":"CONTRIBUTION","amount":10.25,"transactionDate":"2026-03-31","description":"Synthetic demo"}'
```

The example uses Income Fund so the dedicated Growth Fund reporting fixture stays unchanged. Each successful transaction has a CREATE audit record. Investor emails are trimmed and lowercased with `Locale.ROOT`; uniqueness is scoped to the client. USD is the only currency. Stored positive amounts use `BigDecimal` and `NUMERIC(19,2)`.

## PostgreSQL, optional

Docker Compose is provided only for a local PostgreSQL alternative:

```bash
docker compose up -d --wait
./gradlew bootRun --args='--spring.profiles.active=postgres'
# Run isolated database checks; Testcontainers creates its own disposable database:
./gradlew postgresTest
docker compose down
```

Compose binds PostgreSQL to localhost and uses only explicit demo credentials (`ledger_demo` / `ledger_demo`, database `interview_ledger`). Its named volume persists across restarts. `docker compose down -v` deliberately deletes that demo database to restore fixtures. Testcontainers does not require Compose. Environment overrides are `LEDGER_DB_URL`, `LEDGER_DB_USER`, and `LEDGER_DB_PASSWORD`.

## Documentation

- [Architecture and security boundary](docs/ARCHITECTURE.md)
- [Schema, fixtures, and financial rules](docs/DATABASE.md)
- [Tests and independent exercise selection](docs/TESTING.md)
- [Interview process and proposed AI policy](docs/INTERVIEW_PROCESS.md)
- [Leveling rubric](docs/LEVELING_FRAMEWORK.md)
- [Management proposal](docs/MANAGEMENT_PROPOSAL.md)
- [Dependency versions](docs/VERSIONS.md)
- [Verification results](docs/VERIFICATION.md)
- [Roadmap and separation of private materials](docs/ROADMAP.md)
- [Delivery summary and decisions](docs/DELIVERY.md)

**Local teaching application:** client path scoping and database integrity are implemented; authenticated authorization is not. A caller can choose a different client path. S2 will introduce a separate security exercise. No reference implementations or interviewer-only acceptance tests are included. This starter is not a production financial or accounting system.
