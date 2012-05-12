package com.currencysolutions.test;

import java.io.File;
import java.io.PrintStream;
import java.io.IOException;

public interface Parser {
    public void parse(File statement) throws IOException;
    public int count(String currency);
    public void print(PrintStream out);
}
