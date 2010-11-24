package com.compomics.sigpep;

/**
 * Provides access to an instance of SigPepApplication.
 *
 * The implementation of the locator depends on the the
 * application context (standalone, webapp, ...) and
 * is configured in the sigpep-app.properties file.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 24-Jul-2008<br/>
 * Time: 14:07:23<br/>
 */
public abstract class ApplicationLocator {

    /** provides access to the configuration in the sigpep-app.properties file */
    private static Configuration config = Configuration.getInstance();

    /** the ApplicationLocator implemenation */
    private static String locatorClass = config.getString("sigpep.app.locator.class");

    /** the ApplicationLocator singleton */
    private static ApplicationLocator ourInstance;

    /**
     * Returns an ApplicationLocator singleton instance.
     *
     * @return the application locator
     */
    public static ApplicationLocator getInstance() {

        //create an instance if called the first time
        if (ourInstance == null) {

            //instantiate implementation
            try {
                ourInstance = (ApplicationLocator)Class.forName(locatorClass).newInstance();
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
     * Returns an instance of the SigPepApplication bean.
     *
     * @return a SigPepApplication bean
     */
    public abstract SigPepApplication getApplication();

}
