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




## Pre-req

Docker, Solr, Java 8, Maven 3, Node v22.3.0, vue 3





































