# Mockify Reference Guide

This document summarizes all functionality provided by Mockify, lists every built‑in generator, and outlines future work planned for the project.

## 1. Application Overview

Mockify turns a YAML schema into REST endpoints backed by in‑memory data generated via [Faker](https://github.com/DiUS/java-faker). After startup the service exposes data for each model defined in `jsonSchema.yml` under `/api/{model}`.

### Key Modules

- **SchemaService** – reads and writes the YAML schema (`jsonSchema.yml`).
- **DatasetBuilder** – generates data rows for each model using `GeneratorRegistry` and wires simple foreign keys.
- **ModelController** – provides read APIs with optional join expansion via `join` query parameter.
- **Schema Builder UI** – available at `/schema-builder` for editing the schema in a browser (work in progress).

## 2. YAML Schema Directives

Schema file location defaults to `src/main/resources/jsonSchema.yml` and is configurable via `application.yml`.

Each top‑level model entry accepts the following keys:

| Key          | Description                                                                 |
|--------------|-----------------------------------------------------------------------------|
| `_count`     | Number of rows to generate for this model.                                  |
| `fake`       | Map of field names to generator specifications.                             |
| `belongsTo`  | Declares a many‑to‑one relationship (adds `<parent>Id` field).              |
| `hasOne`     | Declares a one‑to‑one child link. Validated at startup.                     |
| `hasMany`    | Declares a one‑to‑many child link. Validated at startup.                    |

Example excerpt from the bundled `jsonSchema.yml`:

```yaml
models:
  Author:
    _count: 3
    hasMany: [ Post ]
    fake:
      id: uuid
      name: name
      email: email
      joinedAt: datetime[start,end][2010-01-01T00:00:00Z,2024-12-31T23:59:59Z]

  Post:
    _count: 6
    belongsTo: Author
    fake:
      id: uuid
      title: sentence
      body: loremparagraphs[min,max][2,4]
      views: int[min,max][0,5000]
      status: oneofstring[draft,published,archived]
      price: price[min,max][9.99,49.99]
```

## 3. REST API

Currently only read‑only endpoints are available. For a model named `Author` the following endpoint is exposed:

```
GET /api/Author
```

Use the optional `join` query parameter to control relationship expansion:

- `fks` – only foreign key fields are included (default when configured).
- `shallow` – embeds single related objects defined with `belongsTo` or `hasOne`.
- `deep` – recursively embeds `hasMany` children as well.

## 4. Built‑in Generators

The following table lists every generator recognised by Mockify. The **Name** is used in the YAML spec, **Args** describes parameters (if any), and **Example** shows a typical usage.

<!-- generator table start -->
| Name | Label | Args | Example |
| --- | --- | --- | --- |
| `firstName` | First name | `` | `John` |
| `lastName` | Last name | `` | `Smith` |
| `name` | Full name | `` | `William Fox` |
| `namePrefix` | Name prefix | `` | `Mr.` |
| `nameSuffix` | Name suffix | `` | `MD` |
| `email` | E-mail | `` | `wwade@example.net` |
| `phone` | Phone | `` | `8146447628` |
| `phoneFormatted` | Phone (formatted) | `` | `(786)235-0094` |
| `username` | Username | `` | `thomassylvia` |
| `password[lower,upper,numeric,special,space,length]` | Password | `flags,length` | `^&1%a&&&a!#!` |
| `city` | City | `` | `New Daniel` |
| `country` | Country | `` | `Burundi` |
| `countryCode` | Country code | `` | `SV` |
| `state` | State/Province | `` | `New Hampshire` |
| `stateAbbr` | State abbr | `` | `HI` |
| `street` | Street | `` | `878 Turner Corner` |
| `streetNumber` | Street number | `` | `1452` |
| `streetName` | Street name | `` | `Howard Falls` |
| `streetPrefix` | Street prefix | `` | `W` |
| `streetSuffix` | Street suffix | `` | `Blvd` |
| `zip` | ZIP / Post code | `` | `29613` |
| `latitude[min,max]` | Latitude | `min,max` | `-4.4169` |
| `longitude[min,max]` | Longitude | `min,max` | `-178.1157` |
| `word` | Single word | `` | `through` |
| `sentence` | Sentence | `` | `Floor finally citizen focus mention.` |
| `paragraph` | Paragraph | `` | `Civil house through significant go already take. Free finish high probably authority mind. Parent source night resource election establish brother.` |
| `question` | Question | `` | `Ok game rest report important maybe?` |
| `quote` | Quote | `` | `Have way whom clearly deep deal small.` |
| `phrase` | Catch-phrase | `` | `Profound client-server service-desk` |
| `loremWord` | Lorem word | `` | `ok` |
| `loremWords[min,max]` | Lorem words (range) | `min,max` | `in create relationship avoid` |
| `loremSentences[min,max]` | Lorem sentences (range) | `min,max` | `Try measure how hundred personal bring. Series threat just ball investment.` |
| `loremParagraphs[min,max]` | Lorem paragraphs (range) | `min,max` | `Television work include stock. Pick state theory large mind eat simple. Event day author staff focus.` |
| `URL` | URL | `` | `http://carter.com/` |
| `domainName` | Domain name | `` | `wall.com` |
| `domainSuffix` | Domain suffix | `` | `com` |
| `IPv4` | IPv4 | `` | `97.30.141.74` |
| `IPv6` | IPv6 | `` | `6f58:1aca:c9c2:3ad0:27c8:da2e:3add:87eb` |
| `HTTPStatus` | HTTP status | `` | `OK` |
| `HTTPStatusCode` | HTTP status code | `` | `200` |
| `HTTPMethod` | HTTP method | `` | `PUT` |
| `UUID` | UUID | `` | `d24602e2-e54f-401b-9f8a-c3373ce875a6` |
| `price[min,max]` | Price | `min,max` | `33.43` |
| `int[min,max]` | Integer | `min,max` | `68` |
| `float[min,max]` | Float | `min,max` | `5.42` |
| `digit` | Single digit | `` | `6` |
| `digits n` | Digits (n length) | `n` | `071234` |
| `hex8` | Hex-8 | `` | `d211bd31` |
| `hex16` | Hex-16 | `` | `1d02c2c8c5c1114d` |
| `hex32` | Hex-32 | `` | `a41bbc598726c05097a6cf606db49622` |
| `hex128` | Hex-128 | `` | `b3833e3bceee31e58d97d23a99c3a7653e2a0d21ae1afd566057903cafd5a390c27567afe96bda656a22a7e6551a3d083d8fbb8fc2eced064c846e2cf81e8122` |
| `hex256` | Hex-256 | `` | `494d81bc79ff3342b269597ce7680ff1e007206669f56511c2eaf32bca752423fee86e053fc6e08cc4f0a679692be795ea9edda5dd04bcd7bb08b6e18c53091aa44eb96274f97e166b6f1fef5d95e32d933b009f137e55ae06607cd9f506263f1409a123b7c002962acab2e41a50b7157d42cbb1cb0e8dcdfb27db2647248df6` |
| `bool` | Boolean (50 %) | `` | `True` |
| `bool[pct]` | Boolean with probability | `0-100` | `False` |
| `lexify` | Lexify pattern | `pattern` | `ITEM-taum` |
| `numerify` | Numerify pattern | `pattern` | `1978-2025` |
| `bothify` | Bothify pattern | `pattern` | `GQ-15` |
| `regexp` | RegExp | `regex` | `XI226` |
| `oneOfString` | Pick one (string list) | `list` | `green` |
| `oneOfInt` | Pick one (int list) | `list` | `2` |
| `oneOfFloat` | Pick one (float list) | `list` | `1.1` |
| `oneOfDateTime` | Pick one (ISO date list) | `list` | `2025-01-01T00:00:00Z` |
| `dateTime[start,end]` | DateTime range | `start,end` | `2023-06-01T12:34:56Z` |
| `second` | Second (0-59) | `` | `8` |
| `minute` | Minute (0-59) | `` | `3` |
| `hour` | Hour (0-23) | `` | `12` |
| `day` | Day (1-31) | `` | `24` |
| `month` | Month (1-12) | `` | `1` |
| `year` | Year | `` | `2006` |
| `weekDay` | Week day | `` | `Friday` |
| `monthName` | Month name | `` | `December` |
| `imageURL[w,h]` | Image URL (random) | `width,height` | `https://picsum.photos/300/300?random=820` |
| `color` | Colour name | `` | `Cyan` |
| `hexColor` | Colour HEX | `` | `#ce5d64` |
| `RGBColor` | Colour RGB() | `` | `rgb(126,31,158)` |
| `safeColor` | Safe colour | `` | `maroon` |
| `emoji` | Emoji | `` | `🚀` |
| `emojiDescription` | Emoji description | `` | `rocket` |
| `language` | Language | `` | `Sichuan Yi` |
| `languageAbbr` | Language abbr | `` | `en` |
| `languageBCP` | Language BCP-47 | `` | `en-US` |
| `company` | Company | `` | `Smith PLC` |
| `companySuffix` | Company suffix | `` | `LLC` |
| `jobTitle` | Job title | `` | `General practice doctor` |
| `jobLevel` | Job level | `` | `Lead` |
| `creditCardCVV` | CC CVV | `` | `343` |
| `creditCardExp` | CC expiry | `` | `04/27` |
| `creditCardNumber` | CC number | `` | `3514310145618035` |
| `creditCardType` | CC type | `` | `JCB 16 digit` |
| `currencyCode` | Currency code | `` | `NOK` |
| `currencyName` | Currency name | `` | `Zambian kwacha` |
| `ACHRouting` | ACH routing | `` | `849725935` |
| `ACHAccount` | ACH account | `` | `GB98WSNR33836996941739` |
| `bitcoinAddress` | Bitcoin address | `` | `1552fb34d83b770e65aa49ba7bd16844df` |
| `bitcoinPrivateKey` | Bitcoin private key | `` | `c4130282131d262d0298070e249aee95cc740130cb315fc5db8353b5ae57ab37` |
| `celebrityActor` | Celebrity (actor) | `` | `Eduardo Vargas` |
| `celebrityBusiness` | Celebrity (business) | `` | `Craig-Ramirez` |

| `celebritySport` | Celebrity (sport) | `` | `Personal` |
<!-- generator table end -->

## 5. Work in Progress

Several areas of Mockify are marked as WIP and will evolve in future releases:

- **CRUD Operations** – support for POST, PUT, PATCH and DELETE to allow modifying the in-memory dataset.
- **Improved GUI** – a richer home page and drag‑and‑drop schema designer.
- **Multi‑tenancy** – isolated workspaces per user so different schemas can be served simultaneously.
- **Plugin Architecture** – ability to add or override data generators via pluggable JARs.
- **Configurable Providers** – swap lorem and image providers via configuration.
- **Full Relationship Handling** – one-to-one, one-to-many and many-to-many embedding with robust join policies.
- **Multiple Output Formats** – use content negotiation to return XML, CSV or YAML in addition to JSON.

Contributions are welcome! See `README.md` for how to get started.
