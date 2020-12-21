package com.radix.mail.implementations;

import com.radix.mail.models.Email;
import com.radix.mail.providers.ISendMailProvider;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Log
public class SMTPSendMail implements ISendMailProvider {

    @Resource
    @Autowired
    private ISMTPConfig smtpConfig;

    @Override
    @Async
    public void sendMail(Email email) {
        //TODO: Send mail with SMTP provider
        log.info("Email supposed sent...");
    }
}
