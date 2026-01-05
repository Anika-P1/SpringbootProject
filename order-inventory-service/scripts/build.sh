#!/bin/bash

# Build the order-inventory-service application

# Exit immediately if a command exits with a non-zero status
set -e

# Define variables
PROJECT_NAME="order-inventory-service"
TARGET_DIR="target"
JAR_FILE="$TARGET_DIR/$PROJECT_NAME-0.0.1-SNAPSHOT.jar"

# Clean previous builds
echo "Cleaning previous builds..."
mvn clean

# Build the project
echo "Building the project..."
mvn package -DskipTests

# Check if the JAR file was created
if [ -f "$JAR_FILE" ]; then
    echo "Build successful: $JAR_FILE"
else
    echo "Build failed: JAR file not found."
    exit 1
fi

# Optionally, you can add commands to build Docker image or other tasks here
# echo "Building Docker image..."
# docker build -t $PROJECT_NAME .

echo "Build process completed."