package com.compomics.sigpep.impl;

import com.compomics.sigpep.ExecutorServiceLocator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implementation of ExecutorServiceLocator that returns
 * a cached thread pool executer service.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 01-Aug-2008<br/>
 * Time: 15:05:01<br/>
 */
public class CachedThreadPoolExecutorServiceLocator extends ExecutorServiceLocator {
    
    /**
     * Returns an executor service. The implementation is specified
     * by the sigpep.app.executor.service.class in the sigpep-app.properties file.
     *
     * @return the executor service
     */
    public ExecutorService getExecutorService() {

        if(executorService==null){
           executorService=Executors.newCachedThreadPool();
        }

        return executorService;
    }
}
