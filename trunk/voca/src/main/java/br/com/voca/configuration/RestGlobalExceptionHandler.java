package br.com.voca.configuration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-error-handling
@ControllerAdvice
public class RestGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	// Catch file size exceeded exception!
	@ExceptionHandler(MultipartException.class)
	@ResponseBody
	ResponseEntity<?> handleControllerException(final HttpServletRequest request, final Throwable ex) {

		final HttpStatus status = getStatus(request);
		return new ResponseEntity("Limite para imagem é de 300kb", status);

		// example
		// return new ResponseEntity("success", responseHeaders, HttpStatus.OK);

	}

	private HttpStatus getStatus(final HttpServletRequest request) {
		final Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.valueOf(statusCode);
	}

}
