# WebFlux: Spring Integration Java DSL

This sample demonstrates the usage of the WebFlux protocol adapter to split incoming messages to different routes and provide the results as SSE events.

NOTE: at the time of this writing, [the WebFlux integration drops POST messages with empty request body](https://jira.spring.io/browse/INT-4462)

## Run the Sample

* You need Java 8 to run this sample, because it is based on Lambdas.
* running the `de.escalon.sample.webflux.WebFluxApplication` class from within STS (Right-click on
Main class --> Run As --> Java Application)
* or from the command line in the _webflux_ folder:

    $ mvn spring-boot:run

## Interact with the Sample

The sample expects messages containing a JSON array where possible items are `"latte macchiato""` or `"caffe"`.

    $ curl -v -d "[\"latte macchiato\", \"caffe\"]" -H "Content-Type: application/json" http://localhost:8080/messages

To listen for SSE events:

    $ curl localhost:8080/events
    
Whenever a message is processed, a corresponding event will be sent to the _/events_ resource.

    data:"latte macchiato"

    data:"caffe"

