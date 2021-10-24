#!/bin/bash

MINIKUBE_PROFILE=${MINIKUBE_PROFILE:-mini-service}

echo
echo
echo "START PUBLISH $(date)"

kubectl delete -f ./minikube/k8s/k8s-2-deployment.yaml
kubectl delete -f ./minikube/k8s/k8s-3-service.yaml
kubectl delete -f ./minikube/k8s/k8s-4-ingress.yaml

kubectl apply -f ./minikube/k8s/k8s-1-namespace.yaml

eval $(minikube docker-env -p "${MINIKUBE_PROFILE}")
minikube ip -p "${MINIKUBE_PROFILE}"

kubectl apply -f minikube/k8s/

echo "===================================="
echo
echo
minikube service list -p "${MINIKUBE_PROFILE}" -n spring-jpa
echo
# kubens spring-jpa
sleep 5
kubectl get pods -n spring-jpa
echo
echo "kubectl logs pod-name"
echo
echo "END PUBLISH $(date)"
echo --------------------

