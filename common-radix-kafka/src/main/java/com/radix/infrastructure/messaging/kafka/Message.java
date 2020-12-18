package com.radix.infrastructure.messaging.kafka;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class Message<T> {

    private final CorrelationId id;
    private final T payLoad;

    public Message(CorrelationId id, T payLoad) {
        this.id = id;
        this.payLoad = payLoad;
    }
}
