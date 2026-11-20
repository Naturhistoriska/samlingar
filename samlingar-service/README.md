# Samlingar-service

A Java-based RESTful service that provides search and retrieval functionality for a museum's online collection data, backed by an Apache Solr search index. 

This project requires Java 25 and uses the [Quarkus](https://quarkus.io/) Java Framework.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
make run
```
or
```shell script
./mvnw quarkus:dev
```

API url: http://localhost:8080/api  
API docs:
http://localhost:8080/api/docs  
Dev UI (dev mode only): http://localhost:8080/q/dev/

## Packaging and running the application

The application can be packaged using:

```shell script
make build
```
or
```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Packaging and running the application in Docker

Build container:
```shell script
make build-docker
```
Run:
```shell script
make run-docker
```
