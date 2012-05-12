package com.currencysolutions.test;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;

abstract public class AbstractRunner {

	private final Parser parser;

	public AbstractRunner(String[] args) {
		checkArguments(args);
		String parserClassName = args[0];
		Date targetDate = convertDate(args[1]);
		parser = tryConstructParser(parserClassName, targetDate);
	}

	protected void run() {
		try {
			run(parser);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Parser tryConstructParser(String parserClassName, Date targetDate) {
		try {
			return constructParser(parserClassName, new ParserContext(targetDate));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(String.format("Cannot find class %s", parserClassName), e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Parser constructParser(String parserClassName, ParserContext parserContext) throws ClassNotFoundException,
			NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

		Class<? extends Parser> clazz = Class.forName(parserClassName).asSubclass(Parser.class);
		Constructor<? extends Parser> constructor = clazz.getConstructor(ParserContext.class);
		return constructor.newInstance(parserContext);
	}

	private void checkArguments(String[] args) {
		if (args.length < 2) {
			System.out.printf("Usage: java %s <Parser Class Name> <Date: dd/mm/yyyy>%n", getClass().getName());
			System.exit(1);
		}
	}

	private Date convertDate(String dateString) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(String.format("Invalid date format %s", dateString), e);
		}
	}

	protected abstract void run(Parser parser) throws IOException;
}
