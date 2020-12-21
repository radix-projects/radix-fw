package com.radix.mail.implementations;

public interface ISMTPConfig {


    String getHost();
    Integer getPort();
    Boolean getUseSSL();
    Boolean getUseTLS();
    String getUser();
    String getPassword();

}
