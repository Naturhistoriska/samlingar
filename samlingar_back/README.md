# smalingar-service – Java RESTful Service API

A Java-based RESTful service that provides search and retrieval functionality for a museum's online collection data, backed by an Apache Solr search index. This API acts as an abstraction layer between client applications and the underlying Solr instance, offering a simplified and consistent interface for querying cultural heritage metadata.



## Purpose

This API enables end users, websites, and other services to perform fast, structured searches across the museum’s digital collection—including fossils, plants, animals, minerals, and and historical metadata—without requiring direct Solr knowledge.

## Features


- Query Apache Solr using clean REST endpoints
- Full-text, faceted, and field-specific search
- Pagination, sorting, and filtering
- Structured response model for easy client integration
- Error handling for Solr connection/query errors
- Configurable Solr cores and endpoints
- Built-in logging
- Docker-ready


## Tech Stack

- Java 8
- Wildfly (Thorntail)
- Apache SolrJ (Solr client)
- Maven
- Lombok (optional)
- JUnit / Mockito


## Getting Started

### Prerequisites

Make sure you have installed:

Java 8+
Maven
(Optional) Docker

## Setup

### Clone the Repository

```
git clone https://github.com/Naturhistoriska/samlingar.git
```

cd path_to_repor/samlingar_back/samlingar-service/

### Move into directory

```
cd samlingar_back/samlingar-service/
```


### Build the Project

```
mvn clean package
```


## Run


### Start API

```
java -jar samlingar-api/target/samlingarApi-thorntail.jar -Sinitdata
```

## API Endpoints

### Base URL
http://localhost:8080/api/v1

## Configuration

## YAML Template.
---
swarm:
  solr:
    path: http://localhost:8983/solr
    core: samlingar
    username: {{ solr_user }}
    password: {{ solr_user_password }}



## License

This project is licensed under the MIT License.
You may freely modify and distribute the code.



