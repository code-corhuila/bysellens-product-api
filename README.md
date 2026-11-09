# BySellens Product Service

Independent Spring Boot API for the BySellens Product bounded context.

Part of the **LMS Library** distributed system — team `lms-library`, Grupo 2.
Governance and documentation live in [`library-docs`](https://github.com/code-corhuila/library-docs).

## Build and test

Requires Java 21 and Maven. Run `mvn test` from the repository root.

Configure PostgreSQL with `DB_URL`, `DB_USER`, and `DB_PASSWORD`, and set `JWT_SECRET` to a
secret of at least 32 bytes before starting the application. Product images are stored under
`UPLOAD_DIR` (defaults to `./uploads/productos`). Inventory integration uses `INVENTORY_URL`.
The PostgreSQL schema is managed by Flyway.

## Branching

Three permanent branches. **None of them accepts a direct commit** — you enter through a child
branch and leave through a Pull Request.

```
develop  <--PR--  feat/... fix/... chore/...
qa       <--PR--  qa/...
main     <--PR--  release/...  hotfix/...
```

Promotion happens **by re-application** (`git cherry-pick -x`), never by merging one permanent
branch into another: `merge develop -> qa` and `merge qa -> main` do not exist in this model.

`main` requires **1 approval from `ariel5253`**. On `develop` and `qa` the team sets its own review
rule.

Full policy: `00-governance/branching-policy.md` in `library-docs`.
