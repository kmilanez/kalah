#!/bin/bash

################################################
# Command line interface to start/stop diff app
################################################

# Usage
function _usage {
  echo "usage: $0 [start|stop] <docker|standalone>"
  exit -1
}

# Build all services
function _build_services {
    for dir in gateway registry login-service kalah-service; do
      ${dir}/gradlew -b ${dir}/build.gradle clean build
    done
}

# Build all Docker containers
function _build_containers {
    for dir in gateway registry login-service kalah-service; do
      CONTAINER_NAME=$(echo $dir | tr -d "[./]")
      docker build -t "${CONTAINER_NAME}:latest" ${dir}/
    done
}

# Start all services with docker
function _start {
   # Check if docker is installed
   if [ ! -x "$(command -v docker)" ]; then
      echo "Docker is not installed. Please install it before running the app."
      exit -1
   fi
   # Build service
   _build_services
   # Build docker containers
   _build_containers
   # Start mongoDb container
   docker container run -d -p 27000:27017 --name mongo mongo:latest
   # Run service containers
   for dir in gateway registry login-service kalah-service; do
      SERVICE_NAME=$(echo $dir | tr -d "[./]")
      echo "Starting container for ${SERVICE_NAME}"
      if [ "$SERVICE_NAME" == "registry" ]; then
            docker container run -d -p 8761:8761 --name=registry "${SERVICE_NAME}:latest"
      elif [ "$SERVICE_NAME" == "gateway" ]; then
            docker container run -d -p 8081:8081 --name=gateway --link=registry "${SERVICE_NAME}:latest"
      elif [ "$SERVICE_NAME" == "login-service" ]; then
            docker container run -d --name=login-service --link=mongo --link=registry "${SERVICE_NAME}:latest"
      elif [ "$SERVICE_NAME" == "kalah-service" ]; then
            docker container run -d --name=kalah-service --link=mongo --link=registry "${SERVICE_NAME}:latest"
      fi
    done
}

# Stop all services with docker
function _stop {
   docker container stop login-service   
   docker container rm login-service
   docker container stop kalah-service   
   docker container rm kalah-service   
   docker container stop gateway   
   docker container rm gateway
   docker container stop registry
   docker container rm registry
   docker container stop mongo
   docker container rm mongo
   echo "All containers have been stopped"
}


############################
# Script starts here
############################
if [ $# -eq 0 ]; then
   _usage
fi

if [ "$1" == "start" ]; then
   _start
elif [ "$1" == "stop" ]; then
   _stop
else 
   _usage   
fi
