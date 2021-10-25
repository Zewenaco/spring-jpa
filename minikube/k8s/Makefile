.EXPORT_ALL_VARIABLES:
MINIKUBE_PROFILE=mini-service

minikube-create:
	bash ./minikube/k8s/profile-mini-service-create.sh

minikube-build-image:
	mvn clean install org.pitest:pitest-maven:mutationCoverage && \
	eval $$(minikube docker-env -p "${MINIKUBE_PROFILE}") && \
	docker build --rm -f minikube/ci/Dockerfile -t pineapple-tools/spring-jpa .

minikube-deploy:
	bash ./minikube/k8s/script-deploy-minikube.sh

build-and-deploy: minikube-build-image minikube-deploy

minikube:
	eval $$(minikube docker-env -p "${MINIKUBE_PROFILE}")
	minikube ip -p "${MINIKUBE_PROFILE}"

minikube-ssh:
	minikube ssh -p "${MINIKUBE_PROFILE}"

minikube-dashboard:
	minikube dashboard -p "${MINIKUBE_PROFILE}"

minikube-stop:
	minikube stop -p "${MINIKUBE_PROFILE}"

minikube-start:
	minikube start -p "${MINIKUBE_PROFILE}"

minikube-watch-pod:
	watch kubectl get pods -n spring-jpa

services:
	minikube service list -p "${MINIKUBE_PROFILE}" -n spring-jpa

.PHONY: minikube
.PHONY: build
