Handler Advice Sample "retry-and-more"
======================================

This sample shows how to use the 2.2.0 Handler Advice feature.


## Stateless Retry Advice Demo

This class (`StatelessRetryDemo`) demonstrates stateless retry.

By default, it runs with a simple default retry (3 tries, no backoff, no recovery)

Run with -Dspring.profiles.active=backoff to run with 3 tries, exponential backoff, no recovery

Run with -Dspring.profiles.active=recovery to run with 3 tries, no backoff, error message "recovery"


In each case enter 'fail n' where n is the number of times you want the service to fail.

e.g. 'fail 2' will succeed in each case on the third try, 'fail 3' will fail permanently after the third try.

__NOTE: Starting with Spring Integration 4.0, stateless retry has convenient namespace support; this sample has been updated to show its use.__

It shows the retry advice declared (for each profile) as a top level bean `<int:handler-retry-advice id="retryAdvice">`. Declaring it as a top level bean allows it to be used in multiple places (or via profiles). You can also declare it within the advice chain using `<int:retry-advice/>` (with no 'id'). In that case, it cannot be reused in other advice chains.


## Stateful Retry Advice Demo

This class (`StatefulRetryDemo`) demonstrates stateful retry.

It is similar to the default version of the stateless retry but uses AMQP; you will see that the exception are thrown back to the container and the retries are re-delivered by AMQP.

NOTE: Starting with Spring Integration 4.0, __stateless retry__ has convenient namespace support; stateful retry requires the retry advice to be configured using `<bean/>` definitions as is shown here.

## Circuit Breaker Advice Demo

This class (`CircuitBreakerDemo`) demonstrates the circuit breaker advice.

In this demo, the target service only succeeds in the last quarter of any minute (seconds 45-59). The breaker's threshold is set to 2, with the breaker going half-open once 15 seconds have elapsed since the last failure.

You can observe the function of the advice by entering a number of messages over time, and watch the resulting messages.


## Expression Evaluating Advice Demo

`FileTransferDeleteAfterSuccessDemo`

`FileTransferRenameAfterFailureDemo`


These classes show how to configure expressions to be evaluated after either succes, or failure; the application context is defined in expression-advice-context-xml.

It is a simulation of an application with a file inbound adapter -> ftp outbound adapter.

The advice on the outbound adapter evaluates an expression after the transfer - delete the file on success, rename it on failure.

The results of the expression evaluation are then logged (INFO or ERROR).

No real FTP is involved; mocks are used to simulate the transfer (success or failure).

In both cases simply add a file ending with .txt in ${java.io.tmpdir}/adviceDemo (e.g. touch /tmp/adviceDemo/x,txt) and the results will appear in the console.


## Running the Demos

In each case, run the main method in each of the demonstration classes.