package com.epam.brest.test;

/**
 * An interface for an implementation, with will be loaded by MyClassLoader.
 */
public interface Incrementer {

    public static StringBuffer count1 = new StringBuffer("");

    /**
     * Increment a count1 value.
     *
     * @return incremented value of count1
     */
    public StringBuffer incCount1();

    /**
     * Increment a count2 value.
     *
     * @return incremented value of count2
     */
    public int incCount2();
}
