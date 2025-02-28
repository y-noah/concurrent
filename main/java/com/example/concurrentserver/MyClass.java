package com.example.concurrentserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyClass {
    private static final Logger logger = LoggerFactory.getLogger(MyClass.class);

    public void myMethod() {
        logger.debug("Debug message");
        logger.info("Info message");
        logger.warn("Warning message");
        logger.error("Error message");
    }
}
