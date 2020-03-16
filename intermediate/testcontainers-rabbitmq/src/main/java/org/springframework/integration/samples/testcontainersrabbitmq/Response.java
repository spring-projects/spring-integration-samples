package org.springframework.integration.samples.testcontainersrabbitmq;

import lombok.Value;

import java.util.UUID;

@Value
public class Response {

    private final UUID requestId;
    private final String message;

}
