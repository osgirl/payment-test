package com.currencysolutions.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class ParallelRunnerTest {

	/**
	 * Simulate load by running this enough time to hopefully make it fail if
	 * there is a threading problem.
	 * 
	 * @throws Exception
	 */
	@Test
	public void stressTestThreadingCode() throws Exception {
		for (int i = 0; i < 1000; i++) {
			Date date = new SimpleDateFormat("dd-MM-yyyy").parse("27-03-2012");
			Parser parser = new ParserImpl(new ParserContext(date));

			// run two thread:
			ParserThread thread1 = new ParserThread(parser, new File("statement1.txt"));
			ParserThread thread2 = new ParserThread(parser, new File("statement2.txt"));
			thread1.start();
			thread2.start();
			thread1.join();
			thread2.join();

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			parser.print(new PrintStream(bos));

			// verify:
			String actual = new String(bos.toByteArray());
			String expected = "EUR 28,200.00\nUSD 46,700.00\nGBP 39,294.03\n";
			assertEquals("Failed on iteration# " + i, expected, actual);
		}
	}

	private class ParserThread extends Thread {
		private Parser parser;
		private File file;

		public ParserThread(Parser parser, File file) {
			this.parser = parser;
			this.file = file;
		}

		@Override
		public void run() {
			try {
				parser.parse(file);
			} catch (IOException ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}

	}

}
