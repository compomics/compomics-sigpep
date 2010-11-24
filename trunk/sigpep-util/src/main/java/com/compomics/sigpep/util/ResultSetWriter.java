package com.compomics.sigpep.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 22-Jan-2008<br/>
 * Time: 12:32:36<br/>
 */
public class ResultSetWriter {

    /**
     * the result set to write
     */
    public ResultSet resultSet;

    /**
     * Constructs a ResultSetWriter for a ResultSet
     *
     * @param resultSet the JDBC result set
     */
    public ResultSetWriter(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    /**
     * Writes the result set to a print stream.
     *
     * @param printStream     the output print stream
     * @param columnDelimiter the column delimiter
     * @param printLineNumber print line numbers in front of every row
     * @throws IOException thrown if an exception occurs while writing the result set
     */
    public void write(PrintStream printStream, String columnDelimiter, boolean printLineNumber) throws IOException {

        this.write(new PrintWriter(printStream), columnDelimiter, printLineNumber);

    }

    /**
     * Writes the result set to a file.
     *
     * @param filename        the name of the output file
     * @param columnDelimiter the column delimiter
     * @param printLineNumber print line numbers
     * @throws IOException thrown if an exception occurs while writing the result set
     */
    public void write(String filename, String columnDelimiter, boolean printLineNumber) throws IOException {
        try {
            this.write(new PrintWriter(filename), columnDelimiter, printLineNumber);
        } catch (FileNotFoundException e) {
            throw new IOException("Exception while writing result set.", e);
        }
    }

    /**
     * Writes result set to a print writer.
     *
     * @param printWriter     the output print writer
     * @param columnDelimiter the column delimiter
     * @param printLineNumber print line numbers in front of every row
     * @throws IOException thrown if an exception occurs while writing the result set
     */
    public void write(PrintWriter printWriter, String columnDelimiter, boolean printLineNumber) throws IOException {

        try {

            resultSet.beforeFirst();
            int columnCount = resultSet.getMetaData().getColumnCount();
            String[] columnValues = new String[columnCount];

            DelimitedTableWriter tableWriter = new DelimitedTableWriter(printWriter,
                    columnCount,
                    columnDelimiter,
                    printLineNumber);

            //print column header
            for (int column = 1; column <= columnCount; column++) {

                String columnValue = resultSet.getMetaData().getColumnLabel(column);
                columnValues[column - 1] = columnValue;

            }

            tableWriter.writeHeader(columnValues);

            //print rows
            while (resultSet.next()) {


                for (int column = 1; column <= columnCount; column++) {

                    String columnValue = resultSet.getString(column);
                    columnValues[column - 1] = columnValue;

                }

                tableWriter.writeRow(columnValues);

            }


        } catch (SQLException e) {
            throw new IOException("Error while writing result set.", e);
        }

    }

}
