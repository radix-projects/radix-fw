package com.radix.mail.models;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Email implements Serializable {

    private final String to;
    private final String from;
    private final String replayTo;
    private final String subject;
    private final String body;

}
