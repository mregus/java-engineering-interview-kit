# V0.1 delivery summary

V0.1 is implemented and verified as a runnable candidate starter. The baseline is working software; J1, J2 and M2 deliberately retain their specified candidate work. The management process is a proposal, not an approved or completed pilot.

## Delivered structure

```text
java-engineering-interview-kit/
├── README.md
├── build.gradle / settings.gradle / gradlew / gradlew.bat
├── gradle/wrapper/                 # Wrapper jar, properties, distribution checksum
├── docker-compose.yml             # Optional local PostgreSQL, demo credentials
├── .github/workflows/ci.yml        # H2 package job and PostgreSQL job
├── docs/                          # Architecture, database, testing, process, rubric,
│                                  # management proposal, roadmap, versions, verification
├── exercises/                     # J1/J2/M2 ready; M1/M3/S1/S2/S3 specifications only
└── src/
    ├── main/java/com/interview/ledger/
    │   ├── LedgerApplication.java
    │   ├── controller/
    │   ├── service/
    │   ├── repository/
    │   ├── model/
    │   ├── dto/
    │   ├── exception/
    │   └── configuration/
    ├── main/resources/
    │   ├── application.yml / application-postgres.yml
    │   └── db/migration/ + db/demo/
    ├── test/java/                 # Passing unit/H2 integration baseline
    ├── exerciseTest/java/         # Public independently selectable examples
    └── postgresTest/java/         # Opt-in real PostgreSQL baseline
```

Implemented: Client, Fund, Investor, FundParticipation, LedgerTransaction and TransactionAudit; corresponding repositories; typed request/response records; conventional layered services/controllers; client-scoped baseline APIs; Bean Validation and centralized ProblemDetail handling; transaction/audit atomicity; Actuator health; OpenAPI/Swagger; deterministic migrations and fixtures.

[README.md](../README.md) enumerates all REST endpoints and run commands. [DATABASE.md](DATABASE.md) records all schema constraints and fixtures: two clients, four funds, five investors, five participations, eight transactions and eight audits, including the exact six-transaction reporting fund and a participation with no financial activity.

## Selected versions

Java 21; Spring Boot 3.5.16; Gradle 8.14.3; Spring Data JPA 3.5.13; Hibernate 6.6.53.Final; H2 2.3.232; PostgreSQL JDBC 42.7.11; PostgreSQL server 17.7-alpine; Flyway 11.7.2; JUnit 5.12.2; Mockito 5.17.0; Testcontainers 1.21.4; springdoc 2.8.17. Actuator, MockMvc and other Spring starters follow the Boot BOM. [VERSIONS.md](VERSIONS.md) provides sources and maintenance constraints.

## Verification outcome

- `./gradlew clean test`: **40/40 pass**, none skipped.
- `./gradlew bootJar`: passes; executable jar also starts successfully.
- `./gradlew postgresTest`: **7/7 pass**, none skipped.
- Application `bootRun`, Flyway startup, fixture initialization, health and Swagger: verified.
- Live REST smoke: **31 checks pass**.
- Public exercise compilation: passes. J1: 3 expected failures of 5; J2: 4 of 7; M2: 3 of 5. Generic J1 tag selection also verified.
- Compose definition validates. Long-running Compose execution remains unverified; see the repository [Actions page](https://github.com/mregus/java-engineering-interview-kit/actions) for hosted CI status.

See [VERIFICATION.md](VERIFICATION.md) for exact failure behaviors and report locations. There are no unresolved local baseline blockers. Test success does not imply implemented authentication, concurrency guarantees, or completed exercises.

## Routine implementation decisions

The supplied complete specification replaced the initial provisional domain before final verification. No provisional endpoint/type/schema contract remains.

- The candidate source is maintained in the `java-engineering-interview-kit` project directory on `main`. The initial source ZIP is a snapshot of the original local delivery; the personal GitHub repository is the location for subsequent updates.
- Gradle source sets enforce baseline/exercise/PostgreSQL separation, rather than putting all test classes under the same default source root.
- Scalar FK fields keep entities small; composite database keys enforce tenant relationships.
- Email normalization trims and lowercases with `Locale.ROOT`; duplicate scope is per client. Fund names are trimmed, case-sensitive and client-unique.
- Initial USD support is a documented domain constant. Financial dates are LocalDate; creation timestamps are Instant. Amounts are BigDecimal/NUMERIC(19,2).
- Fixed seed IDs are below 1000; generated IDs start at 1000. Demo data lives in its own versioned Flyway location, enabled by default for this interview application.
- INACTIVE clients remain readable and cannot create records (409). Missing participation conflicts return 409; foreign-client resource references return 404.
- Audit transaction IDs are logical references without a deleting FK; CREATE evidence persists after financial deletion. Update/delete operations are reserved vocabulary, not implemented APIs.
- Default binding is localhost; no external identity provider is configured. Path scoping does not establish authenticated authorization.
- The baseline transaction/audit operation is atomic. M3's deliberately defective variant will be introduced separately in V0.2.

## Remaining work

M1 query/pagination and an isolated M3 defect; S1 concurrent idempotency, S2 trusted tenant authorization and S3 period locking; PostgreSQL concurrency tests; the separately approved private interviewer kit; internal mock-interview calibration; management review before candidate pilot; license selection; and dependency-support review before wider use.

No employer source/data/credentials were used. No reference implementations, hidden tests, private hints or deployments are included. All shipped tests are candidate-visible. The project owner has separately authorized committing and pushing this starter to the personal GitHub repository.
