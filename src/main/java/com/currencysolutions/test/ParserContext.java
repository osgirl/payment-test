package com.currencysolutions.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.currencysolutions.test.ParserImpl.PaymentRecord;

/**
 * A little parser context objects to passed into new instances of the parser
 * 
 * Its a simple way of ensuring that in a multithread scenario when multiple 
 * parsers are running they can also share access to a single 
 * synchronized list
 * 
 * It also means multiple concurrent process can be independently without 
 * fear of them overwriting each other's data. Even if there is no business 
 * need for this, it much safe operationally as there is no chance of a 
 * rouge process (say one that never completed properly) overwriting another.   
 * 
 * @author Ian Morgan
 *
 */
public class ParserContext {
	private Date date;

	private List<PaymentRecord> payments;

	public ParserContext(Date date) {
		this.date = date;
		this.payments = Collections.synchronizedList(new ArrayList<PaymentRecord>());
	}
	
	public Date getDate() {
		return date;
	}

	public List<PaymentRecord> getPayments() {
		return payments;
	}

	
	

}
