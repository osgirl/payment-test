package name.ianmorgan.experian;


/**
 * As provided for code test
 *
 */
public interface Parser {
	void parse(java.io.File statement) throws java.io.IOException;

	int count(String currency);

	void print(java.io.PrintStream out);
}


