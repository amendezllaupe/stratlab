# StratLab — Historical Data Import

## Purpose

StratLab imports quantitative historical data from explicitly exported snapshots. It does not scrape Tracker.gg, replay browser requests, or read the `valorant-analisis` repository at runtime.

## Ownership boundary

| Repository | Owns |
|---|---|
| `valorant-analisis` | Original Tracker snapshots, manual historical summaries, tactical Markdown, playbooks and editorial conclusions |
| `stratlab` | Imported player identities, import audit trail, normalized statistics, dashboards and future official Riot synchronization |

The first supported contract is a Tracker `statsv2` **season** payload. The payload is submitted explicitly to StratLab, preserving the source repository as historical evidence.

## Import endpoint

```text
POST /api/historical-imports/tracker/seasons
```

Example request body:

```json
{
  "playerDisplayName": "Hypocrown",
  "riotTag": "Hypocrown#CAP",
  "sourceReference": "valorant-analisis:data/source/tracker/snapshots/hypocrown/.../season.json",
  "payload": {
    "data": [
      {
        "type": "season",
        "attributes": {
          "seasonId": "...",
          "playlist": "competitive"
        },
        "metadata": { "shortName": "V26: A5 Competitive" },
        "stats": { "matchesPlayed": { "value": 32 } }
      }
    ]
  }
}
```

The client must provide player identity because a Tracker season payload can omit it. Riot tags are trimmed and normalized for identity lookup. The service validates the contract, creates the player when necessary, stores an immutable import audit record, normalizes season statistics, and is idempotent by a canonical SHA-256 fingerprint of the normalized player identity and payload.

The endpoint is disabled by default. For a local import, set both variables in the process that starts the backend:

```bash
export STRATLAB_HISTORICAL_IMPORT_ENABLED=true
export STRATLAB_HISTORICAL_IMPORT_KEY='generate-a-local-random-secret'
```

Every request must include the matching `X-StratLab-Import-Key` header. This is an operational guard for local migration, not a substitute for future player authentication and Riot RSO.

## Security rules

- Import only JSON response bodies, never HAR files.
- Never submit request headers, cookies, URLs with session data, API keys or Riot OAuth tokens.
- `sourceReference` is a human-readable provenance reference, not a credential-bearing URL.
- The stored payload is data evidence, not an authorization artifact.
- Payloads larger than 10 MiB are rejected.
- The endpoint rejects payloads containing credential-like fields (`token`, `cookie`, `authorization`, `secret`, `password`, `credential`, `session`, `accessKey`, `jwt`, or headers).
- An import request filter validates the import key and enforces the payload limit before Spring deserializes the request body.

## Riot API readiness

This import path is intentionally provider-specific at the edge and provider-neutral in the database. A future `RiotMatchSyncService` will create the same player and normalized statistics records after Riot approves Production API access and RSO opt-in.
