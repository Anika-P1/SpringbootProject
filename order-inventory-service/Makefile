# Makefile

# Makefile for Order Inventory Service

.PHONY: build run test clean

build:
	mvn clean package -DskipTests

run:
	java -jar target/order-inventory-service-0.0.1-SNAPSHOT.jar

test:
	mvn test

clean:
	mvn clean