package com.currencysolutions.test;


import java.io.File;
import java.io.IOException;

class SerialRunner extends AbstractRunner {
    
    public static void main(String[] args) {
        new SerialRunner(args).run();
    }
    
    public SerialRunner(String[] args) {
        super(args);
    }
 
    protected void run(Parser parser) throws IOException {
        parser.parse(new File("statement1.txt"));
        parser.parse(new File("statement2.txt"));
        
        parser.print(System.out);
    }   
}
