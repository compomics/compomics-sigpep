package com.compomics.sigpep.util;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
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

    public DelimitedTableWriter(PrintWriter printWriter,
                                int columnCount,
                                String columnDelimiter,
                                boolean printLineNumber) {

        this.printWriter = printWriter;
        this.columnCountLimit = columnCount;
        this.columnDelimiter = columnDelimiter;
        this.printLineNumber = printLineNumber;

    }

    public DelimitedTableWriter(OutputStream outputStream,
                                int columnCount,
                                String columnDelimiter,
                                boolean printLineNumber) {

        this(new PrintWriter(outputStream), columnCount, columnDelimiter, printLineNumber);
       
    }

    public DelimitedTableWriter(PrintWriter printWriter,
                                String columnDelimiter,
                                boolean printLineNumber) {

        this.printWriter = printWriter;
        this.columnDelimiter = columnDelimiter;
        this.printLineNumber = printLineNumber;

    }

    public DelimitedTableWriter(OutputStream outputStream,
                                String columnDelimiter,
                                boolean printLineNumber) {

        this(new PrintWriter(outputStream), columnDelimiter, printLineNumber);

    }

    public void writeHeader(Object... columnHeaders) {

        this.writeRow(0, columnHeaders);

    }

    public void writeRow(Object... columnValues) {



        this.writeRow(++currentRowNumber, columnValues);
    }

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

            try{
                columnValue = columnValues[column].toString();
            } catch (NullPointerException e){
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

    public boolean isPrintLineNumber() {
        return printLineNumber;
    }

    public void setPrintLineNumber(boolean printLineNumber) {
        this.printLineNumber = printLineNumber;
    }

    public int getCurrentRowNumber() {
        return currentRowNumber;
    }

    public void resetRowCounter() {
        this.currentRowNumber = 0;
    }
    
    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    public void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public String getColumnDelimiter() {
        return columnDelimiter;
    }

    public void setColumnDelimiter(String columnDelimiter) {
        this.columnDelimiter = columnDelimiter;
    }

    public int getColumnCount() {
        return columnCountLimit;
    }

    public void setColumnCount(int columnCount) {
        this.columnCountLimit = columnCount;
    }


}
