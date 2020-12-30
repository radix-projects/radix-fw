package com.radix.infrastructure.messaging.kafka;

import java.util.UUID;

import lombok.Getter;

@Getter
public class CorrelationId {

    private final String id;

    public CorrelationId(String title) {
        this.id = title + "(" + UUID.randomUUID().toString() + ")";
    }

    public CorrelationId continueWith(String title) {
        return new CorrelationId(this.id + "-" + title);
    }

}
