# Roadmap and publication boundaries

| Version | Status | Deliverables / exit criteria |
|---|---|---|
| V0.1 — Management demonstration MVP | Implemented locally; see verification | Runnable app, six entities, schema/fixtures, scoped REST baseline, H2/PostgreSQL checks, J1/J2/M2, public exercise checks, CI config, candidate/process/management docs |
| V0.2 — Junior/mid bank | Planned | Implement M1 pagination and isolated defective M3 variant; broaden meaningful tests and instructions |
| V0.3 — Senior bank | Planned | S1 idempotency, isolated S2 authorization defect with lightweight trusted test context, S3 period locking; PostgreSQL race/failure tests |
| V0.4 — Interviewer kit | Planned | Separately maintained reference implementations, hidden tests, hints, variants, scored examples and evaluation templates |
| V0.5 — Internal pilot | Proposed, not approved or conducted | Mock interviews, difficulty/scoring calibration, setup reliability and experience review |
| V1.0 — Approved interview pilot | Requires management approval | Evidence-reviewed release and approved pilot process |

The first milestone does not implement IdempotencyRecord, ReportingPeriod, security providers, update/delete financial APIs, distributed infrastructure, or senior reference solutions. The baseline's atomic audit creation is useful working behavior, not a supplied solution to the later defective M3 variant.

## Candidate versus private materials

The personal candidate repository is intended to be named `java-engineering-interview-kit`. It includes original starter source, synthetic fixtures, public requirements, a transparent rubric and public test examples. Candidates can inspect every shipped test.

The proposed private repository/workspace is `java-engineering-interview-solutions`. It will contain reference code, hidden acceptance tests, exact hints, expected answers, common incorrect implementations, detailed scoring guidance, calibration examples and alternate variants. None are created in V0.1.

Keep the private workspace outside the candidate repository entirely. Never use a candidate branch, ignored directory, temporary commit, release jar, test fixture, artifact upload or Git history to hold private answers. When later approved, the private harness can consume an explicit starter/candidate revision and run outside it; it must not copy private source or test reports into public artifacts. Public release packaging should include only the candidate tree. Review source, history and generated artifacts before any eventual publication.

The project owner has authorized committing and pushing the candidate starter to the personal GitHub repository `mregus/java-engineering-interview-kit`. Repository publication does not approve a management pilot, deployment, paid resources, or publication of private interviewer materials. License selection and management pilot approval remain separate decisions. Hosted workflow status is available on the repository's [Actions page](https://github.com/mregus/java-engineering-interview-kit/actions).
