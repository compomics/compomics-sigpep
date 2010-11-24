package com.compomics.sigpep.impl;

import org.apache.log4j.Logger;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import com.compomics.sigpep.Configuration;
import com.compomics.sigpep.RConnectionProvider;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 20-Aug-2008<br/>
 * Time: 15:23:01<br/>
 */
public class RConnectionProviderImpl extends RConnectionProvider {

    protected static Logger logger = Logger.getLogger(RConnectionProviderImpl.class);


    private Configuration config = Configuration.getInstance();
    private String rServeHost = config.getString("sigpep.app.r.serve.host");
    private int rServePort = config.getInt("sigpep.app.r.serve.port");

    public RConnection getRConnection() {

        logger.info("connecting to Rserve on host " + rServeHost + ", port " + rServePort + ".");
        try {            
            return new RConnection(rServeHost, rServePort);
        } catch (RserveException e) {
            throw new RuntimeException("Exception while connecting to R at " + rServeHost + " on port " + rServePort + ". Make sure Rserve is running and accepts connections. In case you try to connect remotely remote connections have to be enabled by including \"remote enable\" in the Rserve configuration file.", e);
        }
    }

}
