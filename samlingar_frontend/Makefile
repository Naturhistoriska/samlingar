all: build

# v0.8.5 matomo implemented

run:
	VITE_SAMLINGAR_API=https://samlingar.nrm.se/api yarn dev

build:
	docker build -t dina/smalingar-ui:v1.0.0 .

# docker login

release:
	docker push dina/smalingar-ui:v1.0.0


