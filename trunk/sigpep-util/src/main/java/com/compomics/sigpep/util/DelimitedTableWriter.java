package com.compomics.sigpep.util;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @TODO: JavaDoc missing
 * 
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 22-Jan-2008<br/>
 * Time: 15:52:41<br/>
 */
public class DelimitedTableWriter {

    private PrintWriter printWriter;
    private String columnDelimiter;
    private int columnCountLimit = -1;
    private boolean printLineNumber;
    private int currentRowNumber = 0;

    /**
     * @TODO: JavaDoc missing
     *
     * @param printWriter
     * @param columnCount
     * @param columnDelimiter
     * @param printLineNumber
     */
    public DelimitedTableWriter(
            PrintWriter printWriter,
            int columnCount,
            String columnDelimiter,
            boolean printLineNumber) {

        this.printWriter = printWriter;
        this.columnCountLimit = columnCount;
        this.columnDelimiter = columnDelimiter;
        this.printLineNumber = printLineNumber;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param outputStream
     * @param columnCount
     * @param columnDelimiter
     * @param printLineNumber
     */
    public DelimitedTableWriter(
            OutputStream outputStream,
            int columnCount,
            String columnDelimiter,
            boolean printLineNumber) {

        this(new PrintWriter(outputStream), columnCount, columnDelimiter, printLineNumber);
    }

    /**
     * @TODO: JavaDoc missing
     * 
     * @param printWriter
     * @param columnDelimiter
     * @param printLineNumber
     */
    public DelimitedTableWriter(
            PrintWriter printWriter,
            String columnDelimiter,
            boolean printLineNumber) {

        this.printWriter = printWriter;
        this.columnDelimiter = columnDelimiter;
        this.printLineNumber = printLineNumber;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param outputStream
     * @param columnDelimiter
     * @param printLineNumber
     */
    public DelimitedTableWriter(
            OutputStream outputStream,
            String columnDelimiter,
            boolean printLineNumber) {

        this(new PrintWriter(outputStream), columnDelimiter, printLineNumber);
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param columnHeaders
     */
    public void writeHeader(Object... columnHeaders) {
        this.writeRow(0, columnHeaders);
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param columnValues
     */
    public void writeRow(Object... columnValues) {
        this.writeRow(++currentRowNumber, columnValues);
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param rowNumber
     * @param columnValues
     */
    private void writeRow(int rowNumber, Object... columnValues) {

        if (columnCountLimit != -1 && columnValues.length != columnCountLimit) {
            throw new IllegalArgumentException("Array length has to be equal to column count of table. Array length = " + columnValues.length + ", column count limit " + columnCountLimit);
        }

        if (printLineNumber) {
            printWriter.print(rowNumber + columnDelimiter);
        }

        int columnCount = columnValues.length;

        for (int column = 0; column < columnCount; column++) {

            String columnValue;

            try {
                columnValue = columnValues[column].toString();
            } catch (NullPointerException e) {
                columnValue = "null";
            }
            //if not last colmn
            if (column < columnCount - 1) {

                //print column value and seperator
                printWriter.print(columnValue + columnDelimiter);

                //else
            } else {

                //print only column value
                printWriter.println(columnValue);
            }
        }

        printWriter.flush();
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public boolean isPrintLineNumber() {
        return printLineNumber;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param printLineNumber
     */
    public void setPrintLineNumber(boolean printLineNumber) {
        this.printLineNumber = printLineNumber;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public int getCurrentRowNumber() {
        return currentRowNumber;
    }

    /**
     * @TODO: JavaDoc missing
     */
    public void resetRowCounter() {
        this.currentRowNumber = 0;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param printWriter
     */
    public void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public String getColumnDelimiter() {
        return columnDelimiter;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param columnDelimiter
     */
    public void setColumnDelimiter(String columnDelimiter) {
        this.columnDelimiter = columnDelimiter;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @return
     */
    public int getColumnCount() {
        return columnCountLimit;
    }

    /**
     * @TODO: JavaDoc missing
     * 
     * @param columnCount
     */
    public void setColumnCount(int columnCount) {
        this.columnCountLimit = columnCount;
    }
}
