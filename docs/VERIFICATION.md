# V0.1 verification results

**Executed locally on 2026-11-24, America/New_York.** Java: Homebrew OpenJDK 21.0.11, arm64 macOS. Wrapper: Gradle 8.14.3. Optional PostgreSQL checks used Docker and Testcontainers with `postgres:17.7-alpine`. Results below come from executed commands, JUnit XML, application logs, and live HTTP requests.

## Build and automated tests

| Command | Observed result |
|---|---|
| `./gradlew clean test` | **PASS — 40 tests, 0 failures, 0 errors, 0 skipped** |
| `./gradlew bootJar` | **PASS** — executable `build/libs/java-engineering-interview-kit-0.1.0.jar` created |
| `./gradlew postgresTest` | **PASS — 7 tests, 0 failures, 0 errors, 0 skipped** |
| `./gradlew exerciseTestClasses` | **PASS** — all exercise checks compile |
| `./gradlew exerciseJ1` | **Expected RED — 5 tests: 2 pass, 3 fail** |
| `./gradlew exerciseJ2` | **Expected RED — 7 tests: 3 pass, 4 fail** |
| `./gradlew exerciseM2` | **Expected RED — 5 tests: 2 pass, 3 fail** |
| `./gradlew exerciseTest -Pexercise=J1` | **Expected RED** — selects only the same 5 J1 cases; same 3 failures |
| `./gradlew dependencies --configuration runtimeClasspath` | **PASS** — resolved versions checked against VERSIONS.md |
| `docker compose config --quiet` | **PASS** — optional Compose definition validates |

Intentional exercise tasks exit 1 because their assertions fail. This is not a claim that these tasks pass. The default baseline suite runs no exercise acceptance tests and requires no Docker.

Failure details match the documented defects:

- **J1:** contribution plus distribution gives 1200.00 instead of 800.00; expense-only gives 50.00 instead of -50.00; mixed types give 1225.40 instead of 775.20. Empty and contribution-only cases pass.
- **J2:** a blank name, invalid email, and blank email each persist an unwanted investor; successful creation returns 200 instead of 201. Missing name/email and duplicate-email scenarios already pass. Location is required by the public test but the status assertion fails first in the starter.
- **M2:** March, one-day inclusive range, and zero-transaction report return the explicit 501 exercise response instead of 200. Invalid/missing date and unavailable/cross-client fund scenarios pass.

## Running application verification

Executed `./gradlew bootRun --args='--server.port=18080'`. Startup completed, Hibernate validated the schema, and Flyway reported **two successfully applied migrations**, schema version 2. A fresh H2 database loaded the deterministic synthetic data.

**31 live HTTP checks passed**, covering:

- Health UP, Swagger HTML, Swagger redirect, and generated OpenAPI JSON.
- Both client reads and every baseline route family: fund, investor, participation and transaction list/detail/create paths.
- Followable fund/transaction creation Location headers.
- Cross-client fund/investor/transaction reads returning 404, duplicate fund returning 409, and invalid monetary input returning 400.
- Current J2 success status (200) and current M2 response (501), explicitly observed as known exercise defects.
- Exact dedicated Growth Fund fixture: six transactions with the specified dates, types and amounts; Beta's separate transaction list remained scoped.

The smoke writes used a new synthetic fund/investor/participation so they did not alter the dedicated reporting fixture. The verification application was then stopped.

Also executed the packaged jar with `java -jar build/libs/java-engineering-interview-kit-0.1.0.jar --server.port=18081`. It started successfully with a fresh H2 database, health returned UP, the client list contained two clients, and Alpha's transaction list contained exactly six seed rows. This verification process was stopped afterward.

The baseline and real PostgreSQL tests verify repeat `Flyway.migrate()` executes zero additional migrations and does not duplicate seed records. They also verify required participation, positive amounts, composite same-client relationships, scoped uniqueness, and retained audit evidence. The baseline failure-injection test proves rollback on audit failure; PostgreSQL verifies full `NUMERIC(19,2)` precision and successful paired audit creation.

## Evidence and reproducibility

Local generated reports:

- [Baseline HTML](../build/reports/tests/test/index.html)
- [PostgreSQL HTML](../build/reports/tests/postgresTest/index.html)
- [J1 HTML](../build/reports/tests/exerciseJ1/index.html)
- [J2 HTML](../build/reports/tests/exerciseJ2/index.html)
- [M2 HTML](../build/reports/tests/exerciseM2/index.html)

JUnit XML is under `build/test-results/<task>/`. Build output is ignored by Git and omitted from the source ZIP; rerun the commands to regenerate these reports after extraction. The final tests ran sequentially, and the final PostgreSQL report completed successfully. Earlier implementation defects and an overlapping clean/report write were resolved before these final results.

## Unverified or intentionally limited

- No unresolved local build or baseline-test blocker remains.
- Hosted GitHub Actions was not executed during the initial local verification. GitHub publication was subsequently authorized; see the repository [Actions page](https://github.com/mregus/java-engineering-interview-kit/actions) for current hosted results.
- Compose YAML was validated; the optional long-running Compose database was not launched. Real PostgreSQL behavior was exercised through disposable Testcontainers instead.
- Windows and Linux execution were not run locally; the supplied wrapper includes Windows support and CI targets Linux.
- J1/J2/M2 remain intentionally unfinished. M1/M3/S1/S2/S3 implementations, private acceptance tests, reference solutions, and calibration sessions are absent.
- No authentication, multi-instance idempotency, closed-period locking, financial update/delete API, load benchmark, or dependency security audit is claimed.
- Boot 3.5 is at end of OSS support; see [selected versions and sources](VERSIONS.md). It was retained to meet the requested stack.

The initial local milestone was completed without a commit, remote creation, push, deployment, employer dependency, or paid service. Subsequent committing and pushing to the personal GitHub account were separately authorized by the project owner. No deployment or paid service is introduced by that authorization.
