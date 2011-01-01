package com.compomics.sigpep.persistence.rdbms.helper.impl;

import org.apache.log4j.Logger;
import org.apache.commons.configuration.ConfigurationUtils;
import com.compomics.dbtools.SqlScript;
import com.compomics.sigpep.persistence.config.Configuration;
import com.compomics.sigpep.persistence.rdbms.helper.DatabaseInitialiser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.net.URL;
import java.io.IOException;

/**
 * @TODO: JavaDoc missing
 *
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

        try {

            String scriptFilePath = Configuration.getInstance().getString("sigpep.db.create.catalog.schema.sql");
            URL urlSqlScript = ConfigurationUtils.locate(scriptFilePath);
            Connection conn = DriverManager.getConnection(config.getString("sigpep.db.url") + "/", adminUsername, adminPassword);

            Statement s = conn.createStatement();

            s.execute("CREATE SCHEMA " + config.getString("sigpep.db.schema.catalog"));
            s.execute("USE " + config.getString("sigpep.db.schema.catalog"));

            SqlScript script = new SqlScript(urlSqlScript);
            script.execute(conn);

        } catch (SQLException e) {
            logger.error("Exception while initialising database", e);
        } catch (IOException e) {
            logger.error("Exception while initialising database", e);
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

        try {

            Class.forName(config.getString("sigpep.db.driverClassName"));
            DriverManager.getConnection(catalogSchemaUrl, adminUsername, adminPassword);

        } catch (SQLException e) {

            if (e.getMessage().startsWith("Unknown database")) {
                retVal = false;
            } else {
                throw new RuntimeException(e);
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return retVal;
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
