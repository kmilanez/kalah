Kalah
===================
Tool to analyze differences between two base64 encoded payloads (json, xml, plain text, etc) of the same size

# About the architecture
The game was designed based on a microservice architecture. Each service is responsible for a single capabilite and interfaces with each other when needed. 

It also has a Api gateway for future frontend implementaion (You can find screens under frontend-static, but the integration with frontend is pending).

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


# Future improvements

* Integrate frontend screens into 
* Better coverage of integration tests
