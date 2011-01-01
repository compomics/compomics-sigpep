package com.compomics.sigpep.playground;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.SigPepSessionFactory;
import com.compomics.sigpep.SigPepQueryService;
import com.compomics.sigpep.model.PeptideFactory;
import com.compomics.sigpep.model.Protease;
import com.compomics.sigpep.model.Organism;
import com.compomics.sigpep.util.DelimitedTableReader;
import com.compomics.sigpep.util.SigPepUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @TODO: JavaDoc missing.
 * 
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 07-Mar-2008<br/>
 * Time: 10:55:39<br/>
 */
public class BarcodeResultCheck {

    /**
     * @TODO: JavaDoc missing.
     * 
     * @param args
     */
    public static void main(String[] args) {

        try {

            String filename = "/home/mmueller/data/sigpep/barcodes_9606_zmin2_zmax2_acc1_tryp_ptm_metdiox,trpdiox,cystriox.tab";

            DelimitedTableReader dtr = new DelimitedTableReader(new FileInputStream(filename), "\t");
            Set<String> barcodeResultPeptides = new HashSet<String>();
            for (Iterator<String[]> rows = dtr.read(); rows.hasNext();) {
                String[] row = rows.next();
                barcodeResultPeptides.add(row[0]);

            }

            ApplicationContext appContext = new ClassPathXmlApplicationContext("config/applicationContext.xml");
            SigPepSessionFactory sessionFactory = (SigPepSessionFactory) appContext.getBean("sigPepSessionFactory");
            Organism organism = sessionFactory.getOrganism(9606);
            SigPepSession session = sessionFactory.createSigPepSession(organism);
            SigPepQueryService service = session.createSigPepQueryService();
            Protease protease = service.getProteaseByShortName("tryp");
            Set<String> signaturePeptides = service.getSignaturePeptideSequencesForProtease(protease);

//            System.out.println(signaturePeptides.containsAll(barcodeResultPeptides));

            signaturePeptides.removeAll(barcodeResultPeptides);
            Map<Double, String> uniqueMasses = new TreeMap<Double, String>();
            for (String peptide : signaturePeptides) {
                double mass = SigPepUtil.round(PeptideFactory.createPeptide(peptide).getPrecursorIon().getNeutralMassPeptide(), 4);
//                System.out.println(mass + " " + peptide);
                uniqueMasses.put(mass, peptide);
            }
//            System.out.println(signaturePeptides.size());

            for (Double mass : uniqueMasses.keySet()) {
                System.out.println(mass + " " + uniqueMasses.get(mass));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
