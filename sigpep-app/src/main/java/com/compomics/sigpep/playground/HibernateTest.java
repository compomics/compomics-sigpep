package com.compomics.sigpep.playground;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.compomics.sigpep.model.*;
import com.compomics.sigpep.model.constants.Organisms;
import com.compomics.sigpep.persistence.util.HibernateUtil;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 17-Jan-2008<br/>
 * Time: 10:28:00<br/>
 */
public class HibernateTest {

    /**
     * The log4j logger
     */
    private static Logger logger = Logger.getLogger(HibernateTest.class);


    public static void main(String[] args) {

        int ncbiTaxonId = 4932;
        String geneAccession = "YAL002W";
        String proteinAccession = "YAL002W";
        String protease = "tryp";

        System.out.println("Organism test");
        System.out.println(Organisms.getInstance().getSpeciesName(ncbiTaxonId));

        logger.info("Hibernate test");
        SessionFactory sf = HibernateUtil.getSessionFactory(ncbiTaxonId);
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();

        //fetch protease
        Query q = s.createQuery("from Protease where name = :name");
        q.setParameter("name", protease);
        Protease pt = (Protease)q.uniqueResult();

        //fetch protein
        q = s.createQuery("from Gene where primaryDbXref.accession = :accession");
        q.setParameter("accession", proteinAccession);
        Gene g = (Gene)q.uniqueResult();

        t.commit();
        s.close();

        Session s2 = sf.openSession();
        Transaction t2 = s2.beginTransaction();
        s2.update("Gene", g);

        //get protein sequence
        for(Protein p : g.getProteins()){
            System.out.println(p);
        }

        t2.commit();
        s2.close();
        
    }
}
