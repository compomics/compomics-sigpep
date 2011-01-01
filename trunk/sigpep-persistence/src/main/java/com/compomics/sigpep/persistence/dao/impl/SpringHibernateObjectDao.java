package com.compomics.sigpep.persistence.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.compomics.sigpep.model.*;
import com.compomics.sigpep.persistence.dao.ObjectDao;

import java.util.HashSet;
import java.util.Set;

/**
 * An implementation of ObjectDao that uses Hibernate ORM and Spring/AspectJ AOP
 * based session management. A Hibernate session transaction interceptor advice
 * which opens a session when collections are initialised is applied at class loading
 * time to all get methods of the domain model that return a Collection.
 * <p/>
 * To enable class loader instrumentation the JVM has to be setupDatabase with
 * parameter  -javaagent:/path/to/spring-agent-2.5.5.jar.
 *
 * To use Spring load-time AspectJ weaving when the application is setupDatabase
 * on Tomcat server place spring-tomcat-weaver.jar file in Tomcat's
 * lib folder and configure Tomcat to use the new classloader in the
 * Web Application's META-INF/context.xml file:
 *
 * <Context>
 *  <Loader loaderClass="org.springframework.instrument.classloading.tomcat.TomcatInstrumentableClassLoader"/>
 * </Context>
 *
 * http://www.springindepth.com/book/ch06s02.html
 *
 * <p/>
 * (http://static.springframework.org/spring/docs/2.5.x/reference/aop.html).
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 03-Jun-2008<br/>
 * Time: 10:05:23<br/>
 */
public class SpringHibernateObjectDao extends HibernateDaoSupport implements ObjectDao {

    /**
     * Constructs an Object DAO (for use with the Spring Bean Factory).
     * The Hibernate session factory must be set via the respective setter.
     */
    public SpringHibernateObjectDao() {
    }

    /**
     * Constructs an ObjectDao that uses the session factory passed as a parameter
     * to open Hibernate sessions.
     *
     * @param sessionFactory the Hibernate session factory
     */
    public SpringHibernateObjectDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
    }

    //////////////////
    //Organism queries
    //////////////////

    /**
     * Get the organism.
     *
     * @return the organism
     */
    public Organism getOrganism() {

        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("organism");
        Object result = query.uniqueResult();
        session.getTransaction().commit();

        if (result != null) {
            return (Organism) setResultSessionFactory(result);
        } else {
            throw new DataRetrievalFailureException("Organism not retrievable.");
        }
    }

    //////////////////
    //Protease queries
    //////////////////

    /**
     * Get all proteases.
     *
     * @return the protease.
     */
    public Set<Protease> getAllProteases() {

        Set<Protease> retVal = new HashSet<Protease>();

        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("allProteases");
        for (Object o : query.list()) {
            retVal.add((Protease) setResultSessionFactory(o));
        }
        session.getTransaction().commit();

        return retVal;
    }

    /**
     * Get the protease by short name.
     *
     * @param shortName the protease short name
     * @return the protease
     */
    public Protease getProteaseByShortName(String shortName) {

        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("proteaseByShortName")
                .setParameter("shortName", shortName);
        Object result = query.uniqueResult();
        session.getTransaction().commit();

        if (result != null) {
            return (Protease) setResultSessionFactory(result);
        } else {
            throw new DataRetrievalFailureException("No protease with short name " + shortName + ".");
        }
    }

    /**
     * Get the protease set by short name.
     *
     * @param shortName the protease short name
     * @return the proteases
     */
    public Set<Protease> getProteaseSetByShortName(Set<String> shortName) {

        Set<Protease> retVal = new HashSet<Protease>();
        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("proteaseByShortName")
                .setParameterList("shortName", shortName);
        for (Object o : query.list()) {
            retVal.add((Protease) setResultSessionFactory(o));
        }
        session.getTransaction().commit();

        return retVal;
    }

    //////////////
    //Gene queries
    //////////////

    /**
     * Get all genes.
     *
     * @return the genes.
     */
    public Set<Gene> getAllGenes() {
        Set<Gene> retVal = new HashSet<Gene>();

        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("allGenes");

        for (Object o : query.list()) {
            retVal.add((Gene) setResultSessionFactory(o));
        }

        session.getTransaction().commit();

        return retVal;
    }

    /**
     * Get genes by acession number.
     *
     * @param accession the gene accession number
     * @return the gene
     */
    public Gene getGeneByAccession(String accession) {

        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("geneByAccession").setParameter("accession", accession);
        Gene retVal = (Gene) query.uniqueResult();
        session.getTransaction().commit();

        if (retVal != null) {
            retVal = (Gene) setResultSessionFactory(retVal);
            return retVal;
        } else {
            throw new DataRetrievalFailureException("No gene with accession " + accession + ".");
        }
    }

    /**
     * Get the gene set by accession number.
     *
     * @param geneAccession the list of gene accession numbers
     * @return the set of genes
     */
    public Set<Gene> getGeneSetByAccession(Set<String> geneAccession) {

        Set<Gene> retVal = new HashSet<Gene>();

        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();

        //if less than 500 accessions use parameterised query
        if (geneAccession.size() < 500) {

            Query query = session.getNamedQuery("geneByAccession").setParameterList("accession", geneAccession);
            for (Object o : query.list()) {
                    retVal.add((Gene) setResultSessionFactory(o));
            }

        } else {

            //else filter afterwards
            Query query = session.getNamedQuery("allGenes");
            for (Object o : query.list()) {
                Gene g = (Gene) o;
                if (geneAccession.contains(g.getPrimaryDbXref().getAccession())) {
                    retVal.add((Gene) setResultSessionFactory(o));
                }
            }
        }
        session.getTransaction().commit();

        return retVal;
    }

    /////////////////
    //Protein queries
    /////////////////

    /**
     * Get all proteins.
     *
     * @return the set of proteins
     */
    public Set<Protein> getAllProteins() {

        Set<Protein> retVal = new HashSet<Protein>();

        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("allProteins");

        for (Object o : query.list()) {
            retVal.add((Protein) setResultSessionFactory(o));
        }

        session.getTransaction().commit();

        return retVal;
    }

    /**
     * Get protein by accession number.
     *
     * @param accession the protein accession number
     * @return the protein
     */
    public Protein getProteinByAccession(String accession) {

        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("proteinByAccession").setParameter("accession", accession);
        Object result = query.uniqueResult();
        session.getTransaction().commit();

        if (result != null) {
            return (Protein) setResultSessionFactory(result);
        } else {
            throw new DataRetrievalFailureException("No protein with accession " + accession + ".");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param proteinAccession a set of accessions that should not contain more than 500 elements
     */
    public Set<Protein> getProteinSetByAccession(Set<String> proteinAccession) {

        Set<Protein> retVal = new HashSet<Protein>();

        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();
        //if less than 500 accessions use parameterised query
        if (proteinAccession.size() < 500) {

            Query query = session.getNamedQuery("proteinByAccession").setParameterList("accession", proteinAccession);
            for (Object o : query.list()) {
                    retVal.add((Protein) setResultSessionFactory(o));
            }

        } else {

            //else filter afterwards
            Query query = session.getNamedQuery("allProteins");
            for (Object o : query.list()) {
                Protein p = (Protein) o;
                if (proteinAccession.contains(p.getPrimaryDbXref().getAccession())) {
                    retVal.add((Protein) setResultSessionFactory(o));
                }
            }
        }

        session.getTransaction().commit();

        return retVal;
    }

    //////////////////
    //Sequence queries
    //////////////////

    /**
     * Get all protein sequences.
     *
     * @return the set of protein sequences
     */
    public Set<ProteinSequence> getAllProteinSequences() {

        Set<ProteinSequence> retVal = new HashSet<ProteinSequence>();

        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("allProteinSequences");

        for (Object o : query.list()) {
            retVal.add((ProteinSequence) setResultSessionFactory(o));
        }

        session.getTransaction().commit();

        return retVal;
    }

    /////////////////
    //Peptide queries
    /////////////////

    /**
     * Get peptide by id.
     *
     * @param peptideId the peptide id
     * @return the peptide
     */
    public Peptide getPeptideById(String peptideId) {

        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("peptideById").setParameter("peptideId", peptideId);
        Object result = query.uniqueResult();
        session.getTransaction().commit();

        if (result != null) {
            return (Peptide) setResultSessionFactory(result);
        } else {
            throw new DataRetrievalFailureException("No peptide with id " + peptideId + ".");
        }
    }

    /**
     * Get peptide feature by sequence.
     *
     * Note: not implemented, always returns null!
     *
     * @param peptideSequence the peptide sequence
     * @return the peptide feqture
     */
    public PeptideFeature getPeptideFeatureBySequence(String peptideSequence) {
        return null; // @TODO: implement!!
    }

    /**
     * Get peptide feature by id.
     *
     * @param ids the feature ids
     * @return the set of peptide featues.
     */
    public Set<PeptideFeature> getPeptideFeatureById(Set<Integer> ids) {

        Set<PeptideFeature> retVal = new HashSet<PeptideFeature>();

        Session session = this.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("peptideFeatureById").setParameter("peptideFeatureId", ids);

        for (Object o : query.list()) {
            retVal.add((PeptideFeature) setResultSessionFactory(o));
        }

        session.getTransaction().commit();

        return retVal;
    }

    /**
     * @TODO: JavaDoc missing
     *
     * @param object
     * @return
     */
    public Object setResultSessionFactory(Object object) {

        Persistable p = (Persistable) object;
        p.setSessionFactory(this.getSessionFactory());
        return p;
    }
}
