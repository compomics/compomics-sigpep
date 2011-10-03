package com.compomics.sigpep.web.app;

import com.compomics.sigpep.ApplicationLocator;
import com.compomics.sigpep.SigPepApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.faces.context.FacesContext;

/**
 * ApplicationLocator that uses the Spring WebApplicationContext
 * to instantiate the SigPepApplication bean.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 24-Jul-2008<br/>
 * Time: 14:40:33<br/>
 */
public class WebApplicationLocator extends ApplicationLocator {

    /**
     * the servelet context
     */
    private static ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

    /**
     * the Spring application context
     */
    private static ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

    /**
     * the SigPepApplication bean
     */
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
