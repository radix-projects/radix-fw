package com.radix.mail.models;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.util.MimeType;

import java.io.Serializable;

@Data
@Builder
@ToString
public class Email implements Serializable {

	private final String to;

	private final String from;

	@ToString.Exclude
	private final String replayTo;

	private final String subject;

	@ToString.Exclude
	private final String body;

	private final MimeType bodyMimeType;
}
