package br.com.voca.synthesiser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;

public class Teste {

	public static void main(final String[] args) throws IOException {

		InputStream inputStream = null;
		OutputStream outputStream = null;

		inputStream = new Synthesiser("de").getMP3Data("Ich trinke eine Oragensaft");

		outputStream = new FileOutputStream(new File(FileUtils.getUserDirectory() + "/Documents/casa.mp3"));

		// final byte[] bytes2 = IOUtils.toByteArray(inputStream);
		//
		// System.out.println(Base64.encodeBase64String(bytes2));

		int read = 0;
		final byte[] bytes = new byte[1024];

		while ((read = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}

		System.out.println("Done!");

		outputStream.close();

	}
}
