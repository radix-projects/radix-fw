package com.radix.mail.implementations;

import com.radix.mail.models.Email;
import com.radix.mail.providers.ISendMailProvider;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.logging.Level;

@Component
@Log
public class SMTPSendMail implements ISendMailProvider {

	@Resource
	@Autowired
	private ISMTPConfig smtpConfigParams;

	private final MimeType DEFAULT_MIME_TYPE = MimeTypeUtils.TEXT_PLAIN;

	@Override
	@Async
	public void sendMail(Email email) {

		Message message = new MimeMessage(this.createSMTPSession());

		try {
			message.setFrom(new InternetAddress(email.getFrom()));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));

			message.setSubject(email.getSubject());

			MimeMultipart mimeMultipart = new MimeMultipart();

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(email.getBody(), email.getBodyMimeType() != null ?
					email.getBodyMimeType().toString() :
					DEFAULT_MIME_TYPE.toString());

			mimeMultipart.addBodyPart(mimeBodyPart);

			message.setContent(mimeMultipart);

			Transport.send(message);
		} catch (MessagingException e) {
			log.log(Level.SEVERE, "Ocorreu um erro ao enviar o e-mail", e);
		}

	}

	/**
	 * Cria a sessão com o servidor SMTP, por onde serão enviados os e-mails
	 */
	public Session createSMTPSession() {

		Session session = Session.getInstance(this.createSMTPProperties(), new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(smtpConfigParams.getUser(), smtpConfigParams.getPassword());
			}
		});

		return session;
	}

	private Properties createSMTPProperties() {

		Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", this.smtpConfigParams.getUseTLS());
		prop.put("mail.smtp.host", this.smtpConfigParams.getHost());
		prop.put("mail.smtp.port", this.smtpConfigParams.getPort());
		//aceitar todos os hosts
		prop.put("mail.smtp.ssl.trust", "*");

		return prop;
	}
}

