[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![CircleCI](https://circleci.com/gh/juliuskrah/rest-example/tree/spring-jwt.svg?style=svg)](https://circleci.com/gh/juliuskrah/rest-example/tree/spring-jwt)
# REST Example for Java using Spring Security
Simple REST repository to accompany my [REST series](http://juliuskrah.com/tutorial/2017/11/07/securing-a-spring-rest-service-with-jwt/).

I have implemented REST security using JWT with Spring

For demonstration purposes this example stores resources in memory. If you are looking for the similar example that stores resources in a database, take a look at the [spring-data](https://github.com/juliuskrah/rest-example/tree/spring-data) branch


# Quickstart
```bash
> git clone https://github.com/juliuskrah/rest-example.git -b spring-jwt
> cd rest-example
> ./mvnw clean spring-boot:run
```

If you are looking for how to implement JWT security in a Jersey Spring based
project checkout the [jersey-spring-jwt](https://github.com/juliuskrah/rest-example/tree/jersey-spring-jwt) branch