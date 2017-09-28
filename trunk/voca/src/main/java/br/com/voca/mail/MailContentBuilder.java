package br.com.voca.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@org.springframework.stereotype.Service
public class MailContentBuilder {

	private final TemplateEngine templateEngine;

	@Autowired
	public MailContentBuilder(final TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public String build(final String subject, final String message) {
		final Context context = new Context();
		context.setVariable("mensagem", message);
		context.setVariable("titulo", subject);
		return templateEngine.process("basic", context);
	}
}
