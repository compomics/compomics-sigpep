package com.compomics.sigpep.persistence.dao;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.compomics.sigpep.model.Persistable;

/**
 * An advice that opens a Hibernate session before every call to a method
 * of the domain model that returns a collection. This is required to
 * allow lazy loading of collections. The advice is applied to domain
 * classes at class loading time using the Spring AspjectJ class loading
 * instrumentation.
 * <p/>
 * To enable class loader instrumentation the JVM has to be setupDatabase with
 * parameter  -javaagent:/path/to/spring-agent-2.5.5.jar.
 * <p/>
 * To use Spring load-time AspectJ weaving when the application is setupDatabase
 * on Tomcat server place spring-tomcat-weaver.jar file in Tomcat's
 * lib folder and configure Tomcat to use the new classloader in the
 * Web Application's META-INF/context.xml file:
 *
 * <Context>
 *  <Loader loaderClass="org.springframework.instrument.classloading.tomcat.TomcatInstrumentableClassLoader"/>
 * </Context>
 *
 * (http://static.springframework.org/spring/docs/2.5.x/reference/aop.html).
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 25-Jun-2008<br/>
 * Time: 14:36:42<br/>
 */

@Aspect
public class HibernateSessionTransactionInterceptor {

    @Around("methodsToBeProfiled()")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {

        //get session factory of persistent object
        Persistable target = (Persistable) pjp.getTarget();
        SessionFactory sessionFactory = (SessionFactory) target.getSessionFactory();

        Object result;

        if (sessionFactory != null) {

            //open session
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.load(target.getClass(), target.getId());
            result = pjp.proceed();
            session.getTransaction().commit();

        } else {
            result = pjp.proceed();
        }

        //return Collection
        return result;
    }

    @Pointcut("execution(public java.util.Collection+ com.compomics.sigpep.model.Persistable.get*(..))")
    public void methodsToBeProfiled() {
    }
}
