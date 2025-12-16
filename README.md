# Samlingar - Museum Collections Search Platform

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


The Samlingar - Museum Collections Search Platform is a complete system for ingesting, indexing, searching, and presenting cultural heritage objects.
It is built on a modular architecture with separate services for indexing, data processing, API access, UI display, and search functionality via Apache Solr.

This repository/project consists of five major components:

- samlingar-csv-solr (CSV File → Solr Indexer (Java))
- samlingar-specify-dwc (Specify Database → Solr Indexer (Java))
- samlingar_backend (Java RESTful API (Search API))
- samlingar-frontend (Vue 3 Web Application)
- solr (Apache Solr Search Engine)

Together, these components create a scalable, searchable platform for digital museum collections.




```
CSV Files                       Specify Database
     └──(1) samlingar-csv-solr         └──(2) samlingar-specify-dwc
                     │                    │
                     └────────────┬───────┘
                                  ▼
                           (5) Apache Solr
                                  │
                                  ▼
                        (4) samlingar-service
                                  │
                                  ▼
                         (3) samlingar-ui


```
Each component is independent and can be deployed or maintained separately.


## Components

1. samlingar-csv-solr (Java)

A Java-based ingestion tool that reads CSV files containing collection object metadata and indexes them directly into Solr.

### Features

- Reads CSV files

- Validates coordinates

- Maps columns to Solr schema fields

- Supports bulk indexing in json format

- Configurable batch size

- Error reporting and logging


### Running the Indexer

cd  samlingar-csv-solr

java -jar -Dswarm.collection="paleo" -Dswarm.delect.collection=true samlingar-data-process/target/csvToSolr-thorntail.jar -Sinitdata



2. samlingar-specify-dwc (Java)

A Java ingestion tool that extracts records from a Specify database and indexes them into Solr.

### Features

Connects to specify database using JDBC

Runs SELECT queries to fetch object metadata

Transforms database rows to json

Bulk indexing support

cd samlingar-specify-dwc

java -jar  -Xms4096M -Xmx6144M  -Dswarm.update=true  specify-data-process/target/specifyToSolr-thorntail.jar -Sinitdata_ent


3. samlingar-frontend (Vue 3 Application - ui)

## Features

Vue 3 + Vite setup

Search bar with live results

Faceted filters

Item detail page

Responsive design

Axios for API calls


### Running the UI
yarn dev


Open:

👉 http://localhost:5173/



4.  samlingar_service (Java RESTful API (Solr Search API))


A Java-based API layer that provides clean, stable REST endpoints for the Vue UI and other consumers.
Acts as a secure abstraction over Apache Solr.

### Features

Full-text search

Facets, filters, sorting, pagination

Get item by ID

JSON responses

Swagger UI documentation

Error handling


### Running the API


cd samlingar_service

java -jar samlingar-api/target/samlingarApi-thorntail.jar -Sinitdata



API base URL:

http://localhost:8080/



5. Apache Solr

Apache Solr powers full-text indexing and search for all museum objects.

### Features

Custom schema for museum collections

Full-text search

Faceted navigation

Highlighting

REST-based administration



## Development Setup


## Prerequisites

Java 8+

Node.js 16+

Apache Solr 8.x

Specify (optional)

Maven


## Recommended startup order

Start Solr

Run CSV Indexer or MySQL Indexer (optional)

Start REST API

Start Vue 3 UI


## Production Deployment

Each component can be containerized and orchestrated with Docker or Kubernetes.

Typical deployment:

Solr in its own container

REST API in a backend container

Vue UI built as static files served by Nginx

Indexers run periodically using cron or CI/CD jobs
