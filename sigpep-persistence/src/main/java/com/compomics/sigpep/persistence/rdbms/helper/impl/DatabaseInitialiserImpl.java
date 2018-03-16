package com.compomics.sigpep.persistence.rdbms.helper.impl;

import org.apache.log4j.Logger;
import org.apache.commons.configuration.ConfigurationUtils;
import com.compomics.dbtools.SqlScript;
import com.compomics.sigpep.persistence.config.Configuration;
import com.compomics.sigpep.persistence.rdbms.helper.DatabaseInitialiser;

import java.sql.*;
import java.net.URL;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @TODO: JavaDoc missing
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 29-Apr-2009<br/>
 * Time: 15:03:05<br/>
 */
public class DatabaseInitialiserImpl implements DatabaseInitialiser {

    /**
     * the log4j logger
     */
    private static Logger logger = Logger.getLogger(DatabaseInitialiserImpl.class);

    /**
     * provides access to the persistence layer configuration in sigpep-persistence.properties
     */
    private Configuration config = Configuration.getInstance();

    /**
     * the URL of the catalog schema
     */
    private String catalogSchemaUrl = config.getString("sigpep.db.url") + "/" + config.getString("sigpep.db.schema.catalog");

    /**
     * username of user with admin priviliges
     */
    private String adminUsername;

    /**
     * password of user with admin priviliges
     */
    private String adminPassword;

    /**
     * map of organisms
     */
    private Map<Integer, String> organismMap;

    /**
     * Constructs a SigPep database initialiser.
     */
    public DatabaseInitialiserImpl() {
    }

    /**
     * Constructs a SigPep database initialiser.
     *
     * @param adminUsername the administrator username
     * @param adminPassword the administrator password
     */
    public DatabaseInitialiserImpl(String adminUsername, String adminPassword) {
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    /**
     * Initialises the SigPep database by setting up the catalog schema.
     *
     * @return true if the database has been initialised successfully, false if not.
     */
    public boolean initialise() {
        Connection conn = null;
        Statement s = null;
        try {

            String scriptFilePath = Configuration.getInstance().getString("sigpep.db.create.catalog.schema.sql");
            URL urlSqlScript = ConfigurationUtils.locate(scriptFilePath);
            conn = DriverManager.getConnection(config.getString("sigpep.db.url") + "/", adminUsername, adminPassword);

            s = conn.createStatement();

            s.execute("CREATE SCHEMA " + config.getString("sigpep.db.schema.catalog"));
            s.execute("USE " + config.getString("sigpep.db.schema.catalog"));

            SqlScript script = new SqlScript(urlSqlScript);
            script.execute(conn);

        } catch (SQLException e) {
            logger.error("Exception while initialising database", e);
        } catch (IOException e) {
            logger.error("Exception while initialising database", e);
        } finally {
            try {
                s.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return isInitialised();
    }

    /**
     * Tests if the SigPep database has been initialised already.
     *
     * @return true if the database has been initialised, false if not.
     */
    public boolean isInitialised() {

        boolean retVal = true;
        Connection con = null;
        try {

            Class.forName(config.getString("sigpep.db.driverClassName"));
            con = DriverManager.getConnection(catalogSchemaUrl, adminUsername, adminPassword);

        } catch (SQLException e) {
            if (e.getMessage().startsWith("Unknown database")) {
                retVal = false;
            } else {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return retVal;
    }

    public Map<Integer, String> getOrganismMap() {
        Connection conn = null;
        Statement s = null;
        if (organismMap == null) {
            try {
                conn = DriverManager.getConnection(catalogSchemaUrl, adminUsername, adminPassword);
                s = conn.createStatement();

                organismMap = new HashMap<Integer, String>();

                ResultSet rs = s.executeQuery(
                        "SELECT ncbi_taxon_id, organism_name FROM organism");

                while (rs.next()) {
                    organismMap.put(rs.getInt(1), rs.getString(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } finally {
                try {
                    s.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        return organismMap;
    }

    /**
     * Sets the username for database access.
     *
     * @param adminUsername username of user with administration privileges
     */
    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    /**
     * Sets the password for database access.
     *
     * @param adminPassword password of user with administration privileges
     */
    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
