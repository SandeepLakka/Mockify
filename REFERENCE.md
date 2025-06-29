# Mockify Reference Guide

Mockify is a Spring Boot application that turns a YAML description of models into instant REST endpoints backed by fake data. This document aims to cover all the existing functionality, how to use the built‑in generators, and the current work in progress.

## Table of Contents
1. [Getting Started](#getting-started)
2. [YAML Schema](#yaml-schema)
3. [Generators](#generators)
4. [REST API](#rest-api)
5. [Schema Builder UI](#schema-builder-ui)
6. [Configuration](#configuration)
7. [Known Limitations](#known-limitations)
8. [Roadmap and Work in Progress](#roadmap-and-work-in-progress)

## Getting Started
Clone the project and run it using Maven:
```bash
$ git clone https://github.com/SandeepLakka/mockify.git
$ cd mockify
$ mvn spring-boot:run
```
The server reads `src/main/resources/jsonSchema.yml` on startup and exposes endpoints under `/api/`.

## YAML Schema
The YAML file defines your data models.
```yaml
models:
  ModelName:
    _count: <number_of_records>
    fake:
      fieldName: <generator_spec>
    belongsTo: <ParentModelName>    # optional
    hasMany: [ <ChildModelName> ]   # optional
```
* **_count** – how many objects to generate
* **fake** – map of field → generator specification
* **belongsTo / hasMany / hasOne** – relationship keywords (see [Known Limitations](#known-limitations))

A sample schema is included in the repository (`jsonSchema.yml`).

## Generators
Generators produce values for the fields defined in the schema. Below is the complete catalogue defined in `GeneratorMetaProvider`:

| Name | Description/Example |
| --- | --- |
| `firstName` | First name (example: `firstName`) |
| `lastName` | Last name (example: `lastName`) |
| `name` | Full name (example: `name`) |
| `namePrefix` | Name prefix (example: `namePrefix`) |
| `nameSuffix` | Name suffix (example: `nameSuffix`) |
| `email` | E-mail (example: `email`) |
| `phone` | Phone (example: `phone`) |
| `phoneFormatted` | Phone (formatted) (example: `phoneFormatted`) |
| `username` | Username (example: `username`) |
| `city` | City (example: `city`) |
| `country` | Country (example: `country`) |
| `countryCode` | Country code (example: `countryCode`) |
| `state` | State/Province (example: `state`) |
| `stateAbbr` | State abbr (example: `stateAbbr`) |
| `street` | Street (example: `street`) |
| `streetNumber` | Street number (example: `streetNumber`) |
| `streetName` | Street name (example: `streetName`) |
| `streetPrefix` | Street prefix (example: `streetPrefix`) |
| `streetSuffix` | Street suffix (example: `streetSuffix`) |
| `zip` | ZIP / Post code (example: `zip`) |
| `latitude[min,max]` | Latitude (example: `latitude[-90,90]`) |
| `longitude[min,max]` | Longitude (example: `longitude[-180,180]`) |
| `word` | Single word (example: `word`) |
| `sentence` | Sentence (example: `sentence`) |
| `paragraph` | Paragraph (example: `paragraph`) |
| `question` | Question (example: `question`) |
| `quote` | Quote (example: `quote`) |
| `phrase` | Catch-phrase (example: `phrase`) |
| `loremWord` | Lorem word (example: `loremWord`) |
| `loremWords[min,max]` | Lorem words (range) (example: `loremWords[3,7]`) |
| `URL` | URL (example: `URL`) |
| `domainName` | Domain name (example: `domainName`) |
| `domainSuffix` | Domain suffix (example: `domainSuffix`) |
| `IPv4` | IPv4 (example: `IPv4`) |
| `IPv6` | IPv6 (example: `IPv6`) |
| `HTTPStatus` | HTTP status (example: `HTTPStatus`) |
| `HTTPStatusCode` | HTTP status code (example: `HTTPStatusCode`) |
| `HTTPMethod` | HTTP method (example: `HTTPMethod`) |
| `UUID` | UUID (example: `UUID`) |
| `price[min,max]` | Price (example: `price[10,199]`) |
| `int[min,max]` | Integer (example: `int[1,100]`) |
| `float[min,max]` | Float (example: `float[0.1,9.9]`) |
| `digit` | Single digit (example: `digit`) |
| `digits n` | Digits (n length) (example: `digits 6`) |
| `hex8` | Hex-8 (example: `hex8`) |
| `hex16` | Hex-16 (example: `hex16`) |
| `hex32` | Hex-32 (example: `hex32`) |
| `hex128` | Hex-128 (example: `hex128`) |
| `hex256` | Hex-256 (example: `hex256`) |
| `bool` | Boolean (50 %) (example: `bool`) |
| `bool[pct]` | Boolean with probability (example: `bool[80]`) |
| `oneOfString` | Pick one (string list) (example: `oneOfString`) |
| `oneOfInt` | Pick one (int list) (example: `oneOfInt`) |
| `oneOfFloat` | Pick one (float list) (example: `oneOfFloat`) |
| `oneOfDateTime` | Pick one (ISO date list) (example: `oneOfDateTime`) |
| `dateTime[start,end]` | DateTime range (example: `dateTime[2000-01-01T00:00:00Z,2025-01-01T00:00:00Z]`) |
| `second` | Second (0-59) (example: `second`) |
| `minute` | Minute (0-59) (example: `minute`) |
| `hour` | Hour (0-23) (example: `hour`) |
| `day` | Day (1-31) (example: `day`) |
| `month` | Month (1-12) (example: `month`) |
| `year` | Year (example: `year`) |
| `weekDay` | Week day (example: `weekDay`) |
| `monthName` | Month name (example: `monthName`) |
| `imageURL[w,h]` | Image URL (random) (example: `imageURL[300,300]`) |
| `color` | Colour name (example: `color`) |
| `hexColor` | Colour HEX (example: `hexColor`) |
| `safeColor` | Safe colour (example: `safeColor`) |
| `emoji` | Emoji (example: `emoji`) |
| `emojiDescription` | Emoji description (example: `emojiDescription`) |
| `language` | Language (example: `language`) |
| `languageAbbr` | Language abbr (example: `languageAbbr`) |
| `languageBCP` | Language BCP-47 (example: `languageBCP`) |
| `company` | Company (example: `company`) |
| `companySuffix` | Company suffix (example: `companySuffix`) |
| `jobTitle` | Job title (example: `jobTitle`) |
| `jobLevel` | Job level (example: `jobLevel`) |
| `creditCardCVV` | CC CVV (example: `creditCardCVV`) |
| `creditCardExp` | CC expiry (example: `creditCardExp`) |
| `creditCardNumber` | CC number (example: `creditCardNumber`) |
| `creditCardType` | CC type (example: `creditCardType`) |
| `currencyCode` | Currency code (example: `currencyCode`) |
| `currencyName` | Currency name (example: `currencyName`) |
| `ACHRouting` | ACH routing (example: `ACHRouting`) |
| `ACHAccount` | ACH account (example: `ACHAccount`) |
| `bitcoinAddress` | Bitcoin address (example: `bitcoinAddress`) |
| `bitcoinPrivateKey` | Bitcoin private key (example: `bitcoinPrivateKey`) |
| `celebrityActor` | Celebrity (actor) (example: `celebrityActor`) |
| `celebrityBusiness` | Celebrity (business) (example: `celebrityBusiness`) |
| `celebritySport` | Celebrity (sport) (example: `celebritySport`) |
## REST API
Mock data is available via REST endpoints under `/api`. Currently only read operations are implemented.

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/api/{model}` | List all generated objects for a model |

Joining related objects is controlled by the `join` query parameter or the default `join-policy` in `application.yml`. Valid modes are `FKS`, `SHALLOW`, and `DEEP`.

## Schema Builder UI
Navigate to `/schema-builder` for a simple web form that lets you add or modify models and fields. Saving the form rewrites `jsonSchema.yml` and reloads data in memory.

## Configuration
Important settings live in `application.yml`.
* `mockify.schema-path` – location of the YAML file.
* `mockify.join-policy` – default join behaviour (`FKS`, `SHALLOW`, or `DEEP`).

## Known Limitations
* Only `GET` endpoints are available; data is regenerated on each startup.
* Relationship handling is basic and may produce circular structures in DEEP mode.
* Generators are hard-coded; new ones require changing the source.
* Single-tenant only – no workspace or multi-user separation.
* Output format is fixed to JSON.

## Roadmap and Work in Progress
Planned enhancements include:
* **Full CRUD APIs** – POST, PUT, PATCH and DELETE for each model.
* **Improved GUI** – home page, polished schema builder, drag-and-drop relationships.
* **Multi-Tenancy** – isolated workspaces per user with automatic cleanup.
* **Plugin Architecture** – drop-in jars to provide additional generators or override defaults.
* **Configurable Providers** – swap lorem ipsum or image sources via configuration.
* **Relationship Management** – one-to-one, one-to-many and many-to-many linking with proper foreign-key handling.
* **Multiple Output Formats** – support XML, CSV or YAML responses via `Accept` header.
* And more as the project evolves.
