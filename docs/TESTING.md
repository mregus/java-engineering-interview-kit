# Testing

## Actual commands

| Command | Purpose | Initial expectation |
|---|---|---|
| `./gradlew clean test` | Mockito unit tests, H2 REST/schema/integration baseline | Green |
| `./gradlew bootJar` | Executable application packaging | Green |
| `./gradlew bootRun` | Local H2 application | Starts on localhost:8080 |
| `./gradlew exerciseTestClasses` | Compile all public exercise tests without running them | Green |
| `./gradlew exerciseJ1` | Only J1 public examples | Intentional failures |
| `./gradlew exerciseJ2` | Only J2 public examples | Intentional failures |
| `./gradlew exerciseM2` | Only M2 public examples | Intentional failures |
| `./gradlew exerciseTest -Pexercise=J1` | Equivalent tag-selected public checks | Intentional failures |
| `./gradlew postgresTest` | Real PostgreSQL Flyway/JPA/integrity checks, requires Docker | Green when Docker is available |

`exerciseTest` requires exactly J1, J2, or M2; a missing or unknown selection fails with an actionable message. The aliases are independent Test tasks and need no Gradle property. For fresh verification after an unchanged run use `--rerun-tasks` when necessary. Report directories are separate for each task.

## Source separation

- `src/test/java`: passing baseline. It does not assert J1's wrong arithmetic, J2's exact defective 200 status, or M2's unfinished 501 as desirable behavior.
- `src/exerciseTest/java`: candidate-visible JUnit tags J1, J2, M2 and public specification examples. Not part of the default `test` or `check` lifecycle.
- `src/postgresTest/java`: opt-in baseline Testcontainers coverage. Not part of the Docker-free default suite.
- Interviewer-only tests and reference implementations: **absent**, to be maintained in a separately approved private workspace/repository. No hidden tests are generated, embedded in jars, or placed in Git history.

The additional source sets are deliberate departures from the illustrative directory tree in the specification: Gradle enforces the separation rather than relying on naming conventions inside `src/test`.

## What baseline tests establish

MockMvc checks all ordinary route families, client scoping, positive creation paths, followable resource Locations, duplicate conflicts, participation without transactions, request errors, exact decimal input constraints, malformed JSON, Swagger, and health. Integration checks prove real Flyway initialization, composite foreign keys, monetary positivity, deterministic fixtures, repeat migration, and retained audit evidence.

A failure-injection test calls the actual proxied service outside any test transaction, makes audit persistence fail, and confirms that the financial insert was rolled back. It demonstrates the current baseline guarantee; the defective M3 exercise variant remains planned. Mockito unit tests check early failure paths, explicit scoped queries, inactive clients, and normalization.

PostgreSQL Testcontainers tests use a real `postgres:17.7-alpine` instance. They check migrations, repeat initialization, client-scoped uniqueness, composite relationships, positivity, full 19-digit monetary precision through JPA, audit creation, and retention after deletion. A PostgreSQL test fails when Docker cannot start; it does not silently skip and report success. These checks do not establish concurrency or idempotency guarantees.

Most H2 API tests run within rollback-only test transactions. Failed validation assertions compare row counts before and after. PostgreSQL failure cases use independent statements so a rejected statement cannot poison another test transaction. Explicit transactional checks roll back their synthetic changes. Baseline tests leave the dedicated reporting fixture intact.

## Expected public failures

J1 has five examples: empty and contribution-only cases already pass; distribution, expense-only, and mixed-direction cases fail. J2 examples cover valid creation, missing/blank fields, email format, no persistence on rejection, and duplicates. Missing fields and duplicates have partial baseline handling, while blank/invalid values and creation semantics need work. M2's three valid reports fail with 501; date and ownership guards already pass.

These failures indicate the assigned work, not a broken environment. Do not disable exercise tests or weaken assertions to make a check pass. Fix one exercise and rerun its task plus `test`; there is no requirement to fix the others.

## CI

`.github/workflows/ci.yml` has a Java 21 H2 build/package job and a separate PostgreSQL Testcontainers job on Ubuntu. It compiles public exercise tests but does not run intentionally failing acceptance checks. HTML reports are uploaded even on failure. Hosted execution status is available on the repository's [Actions page](https://github.com/mregus/java-engineering-interview-kit/actions). The local verification snapshot is recorded separately below.

See [VERIFICATION.md](VERIFICATION.md) for measured outcomes rather than inferred success. Local XML reports are under `build/test-results/<task>/`, HTML reports under `build/reports/tests/<task>/`. A local smoke harness also exercised a live application over HTTP; its result is summarized there.
