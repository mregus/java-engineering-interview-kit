# Selected versions

Verified on 2026-11-24. Stable release versions are used; no dynamic, milestone, release-candidate or snapshot dependencies are configured. Spring Boot's dependency BOM manages compatible library versions. The Gradle wrapper distribution is pinned with a SHA-256 checksum.

| Component | Selected version | Selection mechanism |
|---|---|---|
| Java language/toolchain | 21 | Gradle toolchain; local verification OpenJDK 21.0.11 |
| Gradle | 8.14.3 | Committed wrapper scripts, jar, properties and distribution checksum |
| Spring Boot | 3.5.16 | Plugin and BOM |
| Spring Framework / MockMvc | 6.2.19 | Boot BOM |
| Spring Data JPA | 3.5.13 | Boot BOM |
| Hibernate ORM | 6.6.53.Final | Boot BOM |
| H2 | 2.3.232 | Boot BOM |
| PostgreSQL JDBC | 42.7.11 | Boot BOM |
| Flyway core + PostgreSQL module | 11.7.2 | Boot BOM |
| Hibernate Validator | 8.0.3.Final | Boot BOM |
| JUnit Jupiter | 5.12.2 | Boot BOM |
| Mockito | 5.17.0 | Boot BOM |
| Testcontainers | 1.21.4 | Boot BOM |
| springdoc OpenAPI / Swagger UI starter | 2.8.17 | Explicit compatible 2.8.x version |
| PostgreSQL server | 17.7-alpine | Explicit Compose and Testcontainers image tag |
| GitHub Actions | checkout v4; setup-java v4; setup-gradle v4; upload-artifact v4 | Major action tags; hosted status is available on the repository Actions page |

Actuator and the web/JPA/validation/test starters use Boot 3.5.16. The runtime dependency graph can be inspected with `./gradlew dependencies --configuration runtimeClasspath` and the test graph with `./gradlew dependencies --configuration testRuntimeClasspath`. BOM constraints pin the selected managed versions; no custom dependency-management framework is introduced. The selected Docker image tag and GitHub Action major tags are readable version pins, not immutable digests/SHAs; those can be tightened before public adoption.

Compatibility sources:

- [Spring Boot 3.5 system requirements](https://docs.spring.io/spring-boot/3.5/system-requirements.html): Java 21 and Gradle 8.14.3 are within the supported build range.
- [Boot 3.5.16 release announcement](https://spring.io/blog/2026/06/25/spring-boot-3-5-16-available-now/): stable release and end of OSS support for the 3.5 line.
- [Boot 3.5.16 dependency BOM](https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-dependencies/3.5.16/spring-boot-dependencies-3.5.16.pom): source for managed selections.
- [springdoc compatibility matrix](https://springdoc.org/v2/): Spring Boot 3.5 supports the chosen 2.8.x family.
- [Gradle 8.14.3 release notes](https://docs.gradle.org/8.14.3/release-notes.html): wrapper release.

Boot 3.5.16 is retained to meet the explicit 3.5.x requirement. Its OSS support has ended. A move to a currently supported Boot line should be considered before wider interview use; this milestone does not silently change the requested major line or claim a dependency security audit.
