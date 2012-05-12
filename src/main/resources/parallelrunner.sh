#!/bin/bash
RUNNER=com.currencysolutions.test.ParallelRunner
PARSER=com.currencysolutions.test.ParserImpl
CLASSPATH_OPT="-cp ."
java $CLASSPATH_OPT $RUNNER $PARSER $1
