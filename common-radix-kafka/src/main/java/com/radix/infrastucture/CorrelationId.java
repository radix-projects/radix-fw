package com.radix.infrastucture;

import lombok.Getter;
import lombok.extern.java.Log;

import java.util.UUID;

@Getter
@Log
public class CorrelationId {

    private final String id;

    public CorrelationId(String title) {
        this.id = title + "(" + UUID.randomUUID().toString() + ")";
    }

    public CorrelationId continueWith(String title) {
        return new CorrelationId(this.id + "-" + title);
    }

}
