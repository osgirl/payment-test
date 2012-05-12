package com.currencysolutions.test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PaymentTest {

	public static void main(String[] args) {
		Parser parser = new ParserImpl(new ParserContext(parseDate(args)));
		try {
			parser.parse(new File("statement1.txt"));
			parser.parse(new File("statement2.txt"));
			parser.print(System.out);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private static Date parseDate(String[] args) {
		try {
			return new SimpleDateFormat("dd-MM-yyyy").parse(args[0]);
		} catch (Exception ex) {
			System.out.println("Please provide date in dd-mm-yyyy format");
			System.exit(-11);
		}
		return null;// keep compiler happy
	}
}
