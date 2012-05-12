package com.currencysolutions.test;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelRunner extends AbstractRunner {

    private final static int THREAD_COUNT = 2;
    private final static long TIMEOUT = 60;

    public static void main(String[] args) {
        new ParallelRunner(args).run();
    }
    
    public ParallelRunner(String[] args) {
        super(args);
    }
 
    protected void run(final Parser parser) {
    
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);
    
        pool.submit(new Runnable() {
            public void run() {
                try {
                    parser.parse(new File("statement1.txt"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    
        pool.submit(new Runnable() {
            public void run() {
                try {
                    parser.parse(new File("statement2.txt"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }        
        });

        if (terminatePool(pool)) {
            parser.print(System.out);
        }
    }
    
    protected boolean terminatePool(ExecutorService pool) {
        pool.shutdown();
        try {
            boolean terminated = pool.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
            if (! terminated) {
                pool.shutdownNow();
                System.out.println("Thread Pool did not shutdown within a reasonable amount of time");
                return false;
            } else {
                return true;
            }
        } catch (InterruptedException e) {
            return false;
        }
    }
}
