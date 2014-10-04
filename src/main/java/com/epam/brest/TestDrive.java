package com.epam.brest;

import com.epam.brest.loader.MyClassLoader;
import com.epam.brest.test.Incrementer;
import org.apache.log4j.Logger;

/**
 * TestDrive
 */
public class TestDrive {
    public static String input = "1";
    public static final Logger LOG = Logger.getLogger(TestDrive.class);

    public static void main(String[] args) throws Exception {
        LOG.info("Would you like to test the app?");
        LOG.info("0 - No, thanks.");
        LOG.info("1 - Let's do it!");

        input = System.console().readLine();

        switch (input) {
            case "0": {
                LOG.info("Bye! Have a nice day :)");
                break;
            }
            case "1": {
                for(int i = 0; i < 5; i++) {
                    ClassLoader loader = new MyClassLoader();
                    Class myClass = Class.forName("com.epam.brest.test.IncrementerImpl", true, loader);
                    Incrementer incrementer = (Incrementer) myClass.newInstance();

                    LOG.info(">> Created class instance:");
                    LOG.info(incrementer.toString());
                    LOG.info("Count1: " + incrementer.incCount1());
                    LOG.info("Count2: " + incrementer.incCount2());
                }

            }
        }
    }
}
