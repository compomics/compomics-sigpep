package com.compomics.sigpep.webapp.component;

import com.compomics.sigpep.webapp.MyVaadinApplication;
import org.apache.log4j.Logger;
import org.vaadin.jonatan.contexthelp.ContextHelp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 27/09/11
 * Time: 9:54
 * To change this template use File | Settings | File Templates.
 */
public class FormHelp extends ContextHelp {
    private static Logger logger = Logger.getLogger(FormHelp.class);

    Properties iFormHelpProperties;

    public FormHelp(){
        super();
        //load form properties
        InputStream lInputStream = MyVaadinApplication.class.getClassLoader().getResourceAsStream("sigPepWebApp.properties");
        iFormHelpProperties = new Properties();
        try {
            iFormHelpProperties.load(lInputStream);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public Properties getFormHelpProperties() {
        return iFormHelpProperties;
    }

    public void setFormHelpProperties(Properties aFormHelpProperties) {
        iFormHelpProperties = aFormHelpProperties;
    }

}
