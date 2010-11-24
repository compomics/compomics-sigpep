package com.compomics.sigpep.impl;

import com.compomics.sigpep.ApplicationLocator;
import com.compomics.sigpep.SigPepApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ApplicationLocator that uses the Spring ClassPathXmlApplicationContext
 * to instantiate the SigPepApplication bean.
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 24-Jul-2008<br/>
 * Time: 14:14:34<br/>
 */
public class StandaloneApplicationLocator extends ApplicationLocator {

    /** the Spring application context */
    private static ApplicationContext appContext = new ClassPathXmlApplicationContext("/config/applicationContext.xml");

    /** the SigPepApplication bean */
    private static SigPepApplication sigPepApplication = (SigPepApplication) appContext.getBean("sigPepApplication");

    /**
     * Returns an instance of the SigPepApplication bean.
     *
     * @return a SigPepApplication bean
     */
    public SigPepApplication getApplication() {
        return sigPepApplication;
    }

}
