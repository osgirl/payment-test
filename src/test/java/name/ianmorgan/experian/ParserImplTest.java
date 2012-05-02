package name.ianmorgan.experian;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;

public class ParserImplTest {

	private Parser parser;

	@Before
	public void setUp() throws Exception {
		parser = new ParserImpl(new SimpleDateFormat("dd-MM-yyyy").parse("27-03-2012"));
	}

	@Test
	public void shouldCountCurrency() throws Exception {
		parser.parse(new File("statement1.txt"));

		assertEquals(2, parser.count("GBP"));
	}

	@Test
	public void shouldGenerateSampleOutputWithProvidedTestData() throws Exception {
		// setup:
		parser.parse(new File("statement1.txt"));
		parser.parse(new File("statement2.txt"));

		// execute:
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		parser.print(new PrintStream(bos));

		// verify:
		String actual = new String(bos.toByteArray());
		String expected = "EUR 28,200.00\nUSD 46,700.00\nGBP 39,294.03\n";
		assertEquals(expected, actual);
	}

}
