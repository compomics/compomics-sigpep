package com.compomics.sigpep.persistence.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.compomics.sigpep.persistence.dao.impl.SpringHibernateObjectDao;
import com.compomics.sigpep.persistence.dao.ObjectDaoFactory;
import com.compomics.sigpep.persistence.dao.ObjectDao;
import com.compomics.sigpep.persistence.util.HibernateUtil;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 24-Jun-2008<br/>
 * Time: 19:04:15<br/>
 */
public class SpringHibernateObjectDaoFactory extends ObjectDaoFactory {

    public static String pointCutExpression = "execution(public * com.compomics.sigpep.model.impl.*.get*(..))";

    static {

        //required to intitialise Spring/AspectJ aspect weaving on class loader level
        new ClassPathXmlApplicationContext("/META-INF/persistenceSpringContext.xml");
    }

    public SpringHibernateObjectDaoFactory() {
    }

    public ObjectDao createObjectDao(int taxonId) {

        SpringHibernateObjectDao retVal = new SpringHibernateObjectDao();

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory(taxonId);
        
        retVal.setSessionFactory(sessionFactory);

        return retVal;

    }

}
