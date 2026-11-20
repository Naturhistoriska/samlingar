build:
	./mvnw clean package

build-docker: build
	docker build -f src/main/docker/Dockerfile.jvm -t naturhistoriska/samlingar-service-dev .

run:
	./mvnw quarkus:dev

run-docker:
	docker run -i --rm -p 8080:8080 \
		--env samlingar.solr.base-url=http://172.17.0.1:18983/solr \
		--env JAVA_MAX_MEM_RATIO=25 \
		--name samlingar-service-dev \
		naturhistoriska/samlingar-service-dev 
