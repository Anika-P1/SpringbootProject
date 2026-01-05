#!/bin/bash

# This script runs the order-inventory-service application locally.

# Set the environment variable for the application profile
export SPRING_PROFILES_ACTIVE=dev

# Run the application using Maven
./mvnw spring-boot:run