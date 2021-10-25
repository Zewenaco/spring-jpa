
run:
	mvn clean install spring-boot:run

pitest:
	mvn org.pitest:pitest-maven:mutationCoverage

test:
	mvn clean install org.pitest:pitest-maven:mutationCoverage

format:
	mvn fmt:format

include minikube/Makefile