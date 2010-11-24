package com.compomics.sigpep;

import org.rosuda.REngine.Rserve.RConnection;

/**
 * Provides a connection to R through Rserve.
 * <br/>
 * Install Rserve via CRAN by typing install.packages("Rserve") on the R consol.
 * Rserve can be started either by typing</br>
 * <code>library(Rserve)</code></br>
 * <code>Rserve()</code></br> in R
 * or typing<br/>
 * <code>R CMD path/to/R/libs/Rserve/libs/Rserve-bin.so</code></br>
 * on the command line.
 *
 * If connecting to Rserve remotely remote access has to be enabled in Rserve
 * by including <code>remote enable</code> in the configuration file. The default location
 * of the configuration file is /etc/Rserv.conf. Alternative or additional configuration
 * files can be speficied with the command line option --RS-conf.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 20-Aug-2008<br/>
 * Time: 15:18:08<br/>
 */
public abstract class RConnectionProvider {

    /** the singleton instance  */
    private static RConnectionProvider ourInstance;

    /**
     * Returns the R connection provider instance.
     *
     * @return the R connection provider
     */
    public static RConnectionProvider getInstance() {

        // creates an instance of the RConnectionProvider
        // the implementation is specified in the sigpep-app.properties file
        Configuration config = Configuration.getInstance();
        String rConnectionProviderClass = config.getString("sigpep.app.r.connection.provider.class");

        if (ourInstance == null) {

            try {
                ourInstance = (RConnectionProvider)Class.forName(rConnectionProviderClass).newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

        return ourInstance;

    }

    /**
     * Returns an R connection.
     *
     * @return thr R connection
     */
    public abstract RConnection getRConnection();

}
