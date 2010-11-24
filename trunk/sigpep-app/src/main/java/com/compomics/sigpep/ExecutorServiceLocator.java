package com.compomics.sigpep;

import java.util.concurrent.ExecutorService;

/**
 * Provides access to an ExecutorService that provides methods to manage termination
 * and methods that can produce a Future for tracking progress of one or
 * more asynchronous tasks.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 01-Aug-2008<br/>
 * Time: 14:54:38<br/>
 */
public abstract class ExecutorServiceLocator {

    /** the singleton instance  */
    private static ExecutorServiceLocator ourInstance;

    /** the executor service instance created when getExecutorService() is called the first time  */
    protected static ExecutorService executorService;

    /**
     * Returns the singleton instance of the executor service locator.
     *
     * @return the executor service locator
     */
    public static ExecutorServiceLocator getInstance() {

        //get the implementation of the executor from the sigpep configuration
        Configuration config = Configuration.getInstance();
        String locatorClass = config.getString("sigpep.app.executor.service.class");

        //create instance if it doesn't exist yet
        if (ourInstance == null) {

            try {
                ourInstance = (ExecutorServiceLocator)Class.forName(locatorClass).newInstance();
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
     * Returns an executor service. The implementation is specified
     * by the sigpep.app.executor.service.class in the sigpep-app.properties file.
     *
     * @return the executor service
     */
    public abstract ExecutorService getExecutorService();
}
