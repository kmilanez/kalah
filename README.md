Kalah
===================
Tool to analyze differences between two base64 encoded payloads (json, xml, plain text, etc) of the same size

# About the architecture
The game was designed based on a microservice architecture. Each service is responsible for a single capabilite and interfaces with each other when needed. 

It also has a Api gateway for future frontend implementaion (You can find screens under frontend-static).

App is composed of the following services:

| Service  | Description |
| ------------- | ------------- |
| kalah-service  |  Handle all capabilities related to the game, like create game, join game, play turn, etc |
| login-service | Handle user management capabilities, like add user, authentication, token management, etc |
| gateway  |  Entry point to expose services to frontend systems and users |
| registry  | Service registry and discovery pattern |

App was designed with scalability in mind. Each service is stateless and can scale horizontaly. All data is store in microservices respective databases, and this can

# About technologies used
The stack used is for the services is Spring with Spring Boot. Infrastructure is based on Spring Cloud Netflix, that integrates Netflix OSS stack to Spring ecosystem.

For more information about them, please refer to documentation:

[Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)

[Spring Cloud Netflix Documentation](https://cloud.spring.io/spring-cloud-netflix/single/spring-cloud-netflix.html)

For database, I choose MongoDB running on docker for simplicity purposes to persist documents as they flow.

[MongoDB Documentation](https://docs.spring.io/spring-data/data-document/docs/current/reference/html)

# Testing and Test coverage
I choose good old Junit + Mockito stack for testing. 

Test coverage is around 80%, considering all the projects together.

# How to run
The project provides a script for starting and stopping the app, in standlone approach or using Docker.

To start and stop the app:

```shell
> ./text-diff.sh start
```
```shell
> ./text-diff.sh stop
```

This will build all the projects, run the tests and build the containers. Then it will run everything.

In case you don't want to use the script, each project can be executed using gradle wrapper:

```shell
> ./diff-service/gradlew bootRun
```
And you will need to start mongodb container manually:

```shell
> docker container run -d -p 27000:27017 --name mongo mongo:latest
```
Or install it. You also need to adjust the service properties to point to right data source and eureka zone.

The provided script have functions that automate these tasks. You can adapt them if necessary.

# How to play a turn

Here's the sequence of events to play a turn:

* POST -> http://localhost:8081/users passing payload: {"username":"<yourusername>","password":"<yourpassword>"}
* POST -> http://localhost:8081/auth passing payload: {"username":"<yourusername>","password":"<yourpassword>"}
* POST -> http://localhost:8081/games passing payload: {"username":"<yourusername>","password":"<yourpassword>"} and token from authentication request in "Authorization" header
At this point game will wait for a second player. For that, add a new user following above steps
* PUT -> http://localhost:8081/games/game-id/pit-id passing payload: {"username":"<yourusername>","password":"<yourpassword>"} and token from authentication request in "Authorization" header. First player available pits are {1,2,3,4,5,6} and second player have {8,9,10,11,12,13}. {7,14} are reserved for kalahs. 


# Future improvements

Unfortunately I did not had much time to focus in this project. Here's my list of improvements that I will revisit as soon as I can:

* Create a frontend app using Angular to integrate with backend
* Better coverage of integration tests
