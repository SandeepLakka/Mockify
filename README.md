# Mockify - Mocked Backend API Generator tool

> *Instant fake REST APIs powered by SpringBoot, YAML and Datafaker.*
>
---

## Overview 💡

Mockify is a personal, open‑source side‑project that lets you design a data model in **YAML** and immediately receive a
fully‑functional JSON REST API backed by in‑memory Datafaker data. Think of it as self‑hosted *jsonplaceholder*+*mockify*
but written in Java17 & Spring Boot3.

### Key Features 🔦

* **YAML‑first modelling** – describe *count*, *fields* & *generators* in one file.
* **40+ built‑in generators** – name, country, uuid, int\[min,max], price\[min,max] …
* **Relationship keywords** – `belongsTo`, `hasOne`, `hasMany` *(view‑only for now)*.
* **Visual schema builder** – Tailwind+Alpine UI to edit YAML without leaving the browser.
* **Instant REST endpoints** – `GET /api/<Model>` queries.
* **Plug‑in architecture** – drop your own generator implementation & restart (Generic/Format plugin based architecture
  is WIP).

---

## Quick Start 🤘🏼

```bash
# clone & run
$ git clone https://github.com/SandeepLakka/mockify.git
$ cd mockify
$ mvn spring-boot:run
```

| URL                                | Description                 |
| ---------------------------------- | --------------------------- |
| `/schema-builder`                  | Visual designer (YAML ⇆ UI) [WIP, deal with yml directly for now] |
| `/api/Author`                      | Example list endpoint       |

---

## YAML Schema Syntax 🧾

```yaml
models:
  User:
    _count: 10 # -- count of how many records we need to generate for User
    fake:
      id: UUID # -- random UUID
      name: name # -- random name
      age: int[min,max][18,100] # -- random int between 18 and 100 (both inclusive)
      country: country # -- random country
```

| Key             | Meaning                                         |
| --------------- | ----------------------------------------------- |
| `_count`        | How many fake rows to generate                  |
| `fake`          | Map of **field → generatorSpec**                |
| `generatorSpec` | `base[label][args]` e.g. `int[min,max][18,100]` |

## Roadmap (WIP) 💥

* Bidirectional **relationship‑aware expansion** & referential ID linking.
* UI drag‑n‑drop for relationships.
* CLI & Docker image for one‑liner startup.
* Relationships (One-to-One, One-to-many, Many-to-many, Many-to-one)
* Visual UI for API Schema CRUD Operations.
* Additional Generators
* Plugin architecture for new Generator(s) addition
* and many more ))

---

## Contributing✋

This is a personal side‑project – **not backed by any company** – and I welcome feedback, issues and PRs.

1. `fork → clone → mvn test`
2. Follow [ConventionalCommits](https://www.conventionalcommits.org/)
3. Open a PR – I merge fast & with gratitude.

Have ideas? File an issue or ping me on GitHub.

---

## License 🔖

MIT © Sandeep Lakka

#### It's free!

![Free as in freedom not as free beer](assets/faif.png)
---

#### 🤖 About these docs

Large chunks of this documentation were drafted with **OpenAI ChatGPT** and then reviewed, organised and tested by a
human maintainer (me 😎).
