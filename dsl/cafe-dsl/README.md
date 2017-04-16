# Cafe Demo: Spring Integration Java DSL

This sample demonstrates the classical Cafe Demo, but it is based on [Spring Integration Java DSL](https://github.com/spring-projects/spring-integration-extensions/wiki/Spring-Integration-Java-DSL-Reference)
 and [Spring Boot](http://projects.spring.io/spring-boot).

See the `cafe` project **README.md** for more details about the domain and the Cafe algorithm.

## Run the Sample

* You need Java 8 to run this sample, because it is based on Lambdas.
* running the `org.springframework.integration.samples.dsl.cafe.lambda.Application` class from within STS (Right-click on
Main class --> Run As --> Java Application)
* or from the command line:

    $ gradlew :cafe-dsl:run

There is the second similar sample - `org.springframework.integration.samples.dsl.cafe.nonlambda.Application`, which
demonstrates how Spring Integration Java DSL can be used from pre Java 8 environment.
