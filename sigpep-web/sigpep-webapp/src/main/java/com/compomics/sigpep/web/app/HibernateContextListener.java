package com.compomics.sigpep.web.app;

import com.compomics.sigpep.persistence.util.HibernateUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;


/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 22-Jul-2008<br/>
 * Time: 18:33:37<br/>
 */
public class HibernateContextListener implements ServletContextListener {

    protected static Logger logger = Logger.getLogger(HibernateContextListener.class);

    public void contextInitialized(ServletContextEvent event) {

        try {
            HibernateUtil.class.newInstance();
        } catch (InstantiationException e) {
            logger.error(e);
        } catch (IllegalAccessException e) {
            logger.error(e);
        }
        
    }

    public void contextDestroyed(ServletContextEvent event) {
        //do nothing
    }
}
