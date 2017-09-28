package br.com.voca.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private MailContentBuilder mailContentBuilder;

	@Async
	public void sendSimpleMessage(final String to, final String subject, final String text) {
		final MimeMessagePreparator messagePreparator = mimeMessage -> {
			final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			final String content = mailContentBuilder.build(subject, text);
			messageHelper.setText(content, true);
		};
		emailSender.send(messagePreparator);
	}
}
