package com.radix.infrastructure.messaging.kafka;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class CorrelationId {

    private final String id;

    public CorrelationId(String title) {
        this.id = title + "(" + UUID.randomUUID().toString() + ")";
    }

    public CorrelationId continueWith(String title) {
        return new CorrelationId(this.id + "-" + title);
    }

}
