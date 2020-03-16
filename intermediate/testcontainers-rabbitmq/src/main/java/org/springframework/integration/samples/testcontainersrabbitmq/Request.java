package org.springframework.integration.samples.testcontainersrabbitmq;

import lombok.Value;

import java.util.Optional;
import java.util.UUID;

@Value
public class Request {

    private final UUID id;
    private final Optional<Integer> messageId;

}
