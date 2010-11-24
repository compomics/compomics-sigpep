package com.compomics.sigpep.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 30-Jan-2008<br/>
 * Time: 11:03:37<br/>
 */
public class DelimitedTableReader {

    private InputStream inputStream;
    private String columnDelimiter;
    private int currentRowNumber = 0;


    public DelimitedTableReader(InputStream inputStream,
                                String columnDelimiter) {

        this.inputStream = inputStream;
        this.columnDelimiter = columnDelimiter;

    }

    public Iterator<String[]> read() {
        return new RowIterator(inputStream);
    }

    public int getCurrentRowNumber() {
        return currentRowNumber;
    }

    class RowIterator implements Iterator<String[]> {

        String nextLine;
        BufferedReader bufferedReader;

        RowIterator(InputStream inputStream) {

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                nextLine = bufferedReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        /**
         * Returns <tt>true</tt> if the iteration has more elements. (In other
         * words, returns <tt>true</tt> if <tt>next</tt> would return an element
         * rather than throwing an exception.)
         *
         * @return <tt>true</tt> if the iterator has more elements.
         */
        public boolean hasNext() {
            return nextLine != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration.
         * @throws java.util.NoSuchElementException
         *          iteration has no more elements.
         */
        public String[] next() {

            if (nextLine == null) {
                throw new NoSuchElementException();
            }

            String[] retVal = null;
            retVal = nextLine.split(columnDelimiter);

            currentRowNumber++;

            try {
                nextLine = bufferedReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return retVal;

        }

        /**
         * Removes from the underlying collection the last element returned by the
         * iterator (optional operation).  This method can be called only once per
         * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
         * the underlying collection is modified while the iteration is in
         * progress in any way other than by calling this method.
         *
         * @throws UnsupportedOperationException if the <tt>remove</tt>
         *                                       operation is not supported by this Iterator.
         * @throws IllegalStateException         if the <tt>next</tt> method has not
         *                                       yet been called, or the <tt>remove</tt> method has already
         *                                       been called after the last call to the <tt>next</tt>
         *                                       method.
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
