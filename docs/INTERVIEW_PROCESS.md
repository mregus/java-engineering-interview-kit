# Proposed interview process

This is a proposed personal engineering initiative for management review. It is not an established employer hiring or AI policy. The current kit supports a management demonstration and mock interviews; operational hiring use requires approval and calibration.

## Before the session

Provide the candidate the same starter version, JDK instructions, assigned-mode AI policy, and public exercise specifications. Ask them to verify the environment before the interview; setup is not the assessed skill. Interviewers run the baseline suite in advance, choose an adaptive route, and confirm accommodations. Use only fictional records and personal/demo environments. Do not ask candidates to use employer systems or accounts.

If setup fails, pause the assessment clock and use a prepared local environment or reschedule the coding portion. Record setup failure as process evidence, not a candidate competency deficit. Agree on screen sharing and accessibility needs; do not record sessions without the applicable consent process.

## Sixty-minute structure

| Minutes | Activity | Evidence |
|---|---|---|
| 0–5 | Introductions, mode confirmation, environment check | Baseline green; shared understanding |
| 5–20 | J1 or J2 | Read/debug existing code; verify a focused fix |
| 20–40 | M2, or continue foundational work adaptively | Multi-layer reasoning, boundaries, tests |
| 40–55 | Senior design discussion or scoped implementation where supported | Failures, consistency, authorization, trade-offs |
| 55–60 | Candidate questions and wrap-up | Candidate experience; remaining uncertainty |

Candidates are not expected to complete all exercises. J1/J2 are estimated at ten minutes each, M2 at twenty. Planned senior exercises require twenty to thirty minutes for fuller implementation; the fifteen-minute slot can establish design evidence but does not prove a concurrency guarantee. Offer a dedicated follow-up when the relevant evidence is insufficient.

V0.1 supplies only J1, J2 and M2. Use S1/S2/S3 specifications for an explicitly labeled discussion; do not claim their unimplemented variants or hidden acceptance suites are ready. Choose the next task based on observed evidence, allow time to finish a useful increment, and record the adaptation.

## Consistency and independence

Use the same starting condition and public examples for a given exercise/version. Let the candidate first explain the symptom, choose an investigation, make a change, and verify it. Observe whether they can form hypotheses, use failures to revise them, and explain code they wrote. Avoid continuous prompting or requiring a running monologue; allow quiet thinking and ask for a short explanation afterward.

Record any assistance with timestamp, reason, and its effect. A common assistance scale is: clarification of the written requirement; conceptual prompt; directed implementation help. Exercise-specific prompts and expected answers belong in the future private interviewer kit, not this candidate repository. Internal mock interviews should calibrate this scale before pilot use. Until then, call it provisional and disclose its use.

A passing public example is one observation. Inspect boundary reasoning, rejected-input side effects, test quality, and the candidate's explanation. Ask for a small change or an additional edge-case test to assess understanding. Record independent work and assisted work separately; do not infer competence from speed, typing style, confidence, or accent.

## Proposed AI modes

| Mode | Permitted | Assessment emphasis |
|---|---|---|
| Independent coding — recommended first calibration track | Ordinary IDE completion; official documentation; generative AI disabled | Debugging, implementation, tests, independent reasoning |
| AI-assisted coding — separately calibrated track | Generative AI permitted, with the candidate responsible for all output | Review, explanation, verification, detection and correction of generated defects |

Communicate the mode before the session and apply it consistently. “Ordinary completion” means conventional symbol/parameter completion, not generative code suggestions. For independent mode ask candidates to disable generative IDE features. In AI-assisted mode ask candidates to identify assisted portions and explain their tests; do not require a paid model account. Provide equal access for a pilot or defer that track. Keep confidential information out of prompts.

Do not compare uncalibrated raw scores across modes. If the agreed tool is unavailable, agree on a documented mode change or reschedule rather than silently changing conditions. This policy is for management decision; it is not represented as employer policy.

## After the session

Complete an evidence-based evaluation before discussing it with another interviewer. Mark unobserved competencies “not assessed.” Record task version, mode, completed behavior, test results, assistance, demonstrated limits, and a recommendation with uncertainty. Apply the [leveling framework](LEVELING_FRAMEWORK.md), including senior competency gates. Keep candidate-specific notes and hiring decisions outside this public starter. Use approved retention and access practices for the eventual pilot.
