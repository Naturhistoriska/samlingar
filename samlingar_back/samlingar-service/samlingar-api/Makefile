#!make

all: build

build:
	docker build -t dina/samlingar-api:v0.3.3 .


# docker login
release:
	docker push dina/samlingar-api:v0.3.3
