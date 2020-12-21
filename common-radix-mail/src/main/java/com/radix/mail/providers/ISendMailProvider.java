package com.radix.mail.providers;

import com.radix.mail.models.Email;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public interface ISendMailProvider {

    @Async
    void sendMail(Email email);

}
