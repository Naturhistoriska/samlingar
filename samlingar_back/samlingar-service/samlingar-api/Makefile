#!make

all: build

run:
	java -jar target/samlingarApi-thorntail.jar -s ../../project-initdata_sample_for_api.yml

build:
	docker build -t dina/samlingar-api:v1.0.1 .


# docker login
release:
	docker push dina/samlingar-api:v1.0.1




# gbif data image 0.4.3
