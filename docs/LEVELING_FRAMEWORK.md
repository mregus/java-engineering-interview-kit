# Provisional leveling framework

This framework supports consistent discussion; it does not replace interviewer judgment. Thresholds below are proposals and must be calibrated through internal mock interviews. V0.1 alone supplies no implemented senior exercise, so a high arithmetic score does not certify senior engineering competency.

| Category | Weight | Observable evidence |
|---|---:|---|
| Java fundamentals | 15% | Collections, enum usage, BigDecimal, readable logic |
| Spring Boot and API development | 15% | DTOs, validation, HTTP semantics, exception handling |
| Database and persistence | 15% | Client predicates, constraints, joins, transactions, query cost |
| Problem-solving and debugging | 15% | Hypotheses, inspection, targeted corrections, response to evidence |
| Testing and code quality | 15% | Meaningful boundaries, failure tests, isolation, maintainability |
| Technical communication | 10% | Clear assumptions, explanations, questions, stated uncertainty |
| Architecture and production reasoning | 15% | Concurrency, failure recovery, authorization, scalability, trade-offs |

Rate observed categories from 1 to 5:

| Rating | Anchor |
|---|---|
| 1 | Insufficient demonstrated competency |
| 2 | Developing; substantial guidance required |
| 3 | Meets expected competency for the assessed task |
| 4 | Strong independent competency |
| 5 | Advanced competency, clear reasoning, robust implementation |

With all categories observed:

```text
weighted score = SUM(category rating / 5 × category weight)
```

**Not assessed is not zero.** For partial evidence, report coverage and a provisional normalized score:

```text
coverage = SUM(weights of observed categories)
observed score = SUM(observed category rating / 5 × category weight)
normalized observed score = observed score / coverage × 100
```

Example: observed weights totaling 60% and observed score 42 points give 70% normalized at 60% coverage. Do not compare that result with a fully assessed result as if evidence coverage were equal. Missing role-critical evidence requires a follow-up or an explicit insufficient-evidence recommendation.

| Provisional range, with adequate evidence | Suggested interpretation |
|---|---|
| Below 45% | Below baseline |
| 45–64% | Junior range |
| 65–84% | Mid-level range |
| 85–100% | Senior range only when senior competencies are demonstrated |

## Competency expectations

**Junior:** navigates an existing application, implements clearly defined requirements, understands basic Java/Spring concepts, writes basic tests, and explains the change. Occasional guidance is compatible with this level. J1/J2 provide relevant evidence.

**Mid-level:** independently spans API/service/repository layers, handles validation and edge cases, understands queries and transactions, writes meaningful integration tests, identifies defects, and explains design decisions. M2 supports this assessment. A correct in-memory report is valid functional evidence; discuss scalability before awarding strong production-query competency.

**Senior:** demonstrates relevant mid-level skills plus failure and concurrency reasoning, database constraints/isolation/locking, reliable financial operations, trusted authorization and tenant isolation, architectural trade-offs, and explicit limits of guarantees. Require concrete evidence in these areas, rather than merely an aggregate ≥85. In V0.1, discussion evidence must be distinguished from tested implementation. Unproven concurrency and authorization competencies remain unassessed.

Record a final recommendation as: assessed level; exercise/version and mode; observed coverage; independent versus assisted evidence; functioning behavior and test evidence; risks or gaps; confidence and any follow-up. A provisional score is an input to this recommendation, not an automatic hiring decision.

Private evaluation templates, scored example answers, hints and calibration records belong outside this repository. The public rubric makes expectations visible without providing reference implementations.
