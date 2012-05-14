package com.currencysolutions.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * <p>
 * Payment file processing. Based on following assumptions:
 * </p>
 * <ul>
 * <li>Volumes low, so no need to keep internal lookups for efficiency (and file
 * I/O will almost certainly be the first performance bottleneck)</li>
 * <li>All record have valid format - no error handling or attempt to skip bad
 * records</li>
 * <li>Currencies stored with two decimal places(so what about Yen etc?)</li>
 * </ul>
 * 
 * 
 * @author Ian Morgan
 * 
 */
public class ParserImpl implements Parser {

	private static final int DATE_FIELD = 0;
	private static final int DEBIT_OR_CREDIT_FIELD = 1;
	private static final int AMOUNT_FIELD = 2;
	private static final int CURRENCY_FIELD = 3;
	private static final int OPTIONAL_FIELDS_START = 4;

	private Date date;
	private List<PaymentRecord> payments;
	private Pattern paymentRecordPattern = Pattern.compile("^PAY\\d{6}[A-Z]{2}$");

	public ParserImpl(Date date) {
		this.date = date;
		this.payments = new ArrayList<PaymentRecord>();
	}

	@Override
	public void parse(File statement) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(statement));
		String line = null;

		while ((line = reader.readLine()) != null) {
			String[] tokens = line.split("\\|");
			PaymentRecord payment = parseAsPaymentRecordForDay(date, tokens);
			if (payment != null) {
				synchronized (this) {
					payments.add(payment);
				}
			}
		}
	}

	private PaymentRecord parseAsPaymentRecordForDay(Date date, String[] tokens) {
		String day = new SimpleDateFormat("dd/MM/yyyy").format(date);

		if (day.equals(tokens[DATE_FIELD]) && "DEBIT".equals(tokens[DEBIT_OR_CREDIT_FIELD]) && isPayment(tokens)) {
			PaymentRecord record = new PaymentRecord();
			record.currency = tokens[CURRENCY_FIELD];
			record.amount = Double.parseDouble(tokens[AMOUNT_FIELD]);
			return record;
		}
		return null;
	}

	private boolean isPayment(String[] tokens) {
		for (int i = OPTIONAL_FIELDS_START; i < tokens.length; i++) {
			Matcher m = paymentRecordPattern.matcher(tokens[i]);
			if (m.matches()) {
				return true;
			}

		}
		return false;
	}

	@Override
	public int count(String currency) {
		synchronized (this) { 
			int count = 0;
			for (PaymentRecord record : payments) {
				if (currency.equals(record.currency)) {
					count++;
				}
			}
			return count;
		}
	}

	@Override
	public void print(PrintStream out) {
		synchronized (this) {
			NumberFormat nf = new DecimalFormat("###,###,###.00");
			for (String currency : allCurrencies()) {
				double total = sum(currency);
				out.print(currency);
				out.print(' ');
				out.print(nf.format(total));
				out.print('\n');
			}
		}
	}

	private Set<String> allCurrencies() {
		Set<String> result = new HashSet<String>();
		for (PaymentRecord record : payments) {
			result.add(record.currency);
		}
		return result;
	}

	private double sum(String currency) {
		double total = 0;
		for (PaymentRecord record : payments) {
			if (currency.equals(record.currency)) {
				total += record.amount;
			}
		}
		return total;
	}

	/**
	 * A simple class to hold data. Only used internally
	 */
	public static class PaymentRecord {
		public double amount;
		public String currency;
	}

}
