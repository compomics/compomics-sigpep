package com.compomics.sigpep.playground;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.compomics.sigpep.PeptideGenerator;
import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.SigPepSessionFactory;
import com.compomics.sigpep.model.Organism;
import com.compomics.sigpep.util.DelimitedTableWriter;

import java.util.Map;
import java.util.Set;

/**
 * @TODO: JavaDoc missing.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 14-Jul-2008<br/>
 * Time: 16:40:23<br/>
 */
public class PeptideDegeneracyReport {

    /**
     * @param taxonId
     * @param proteaseShortNames
     * @TODO: JavaDoc missing.
     */
    public static void reportOriginOfDegeneracy(int taxonId, String... proteaseShortNames) {

        ApplicationContext appContext = new ClassPathXmlApplicationContext("config/applicationContext.xml");
        SigPepSessionFactory sessionFactory = (SigPepSessionFactory) appContext.getBean("sigPepSessionFactory");
        Organism organism = sessionFactory.getOrganism(taxonId);
        SigPepSession session = sessionFactory.createSigPepSession(organism);

        PeptideGenerator peptideGenerator = session.createPeptideGenerator(proteaseShortNames);
        Map<String, Integer> peptide2Degree = peptideGenerator.getPeptideSequenceDegeneracy();
        Map<String, Set<String>> peptide2GeneAccession = peptideGenerator.getPeptideSequenceToGeneAccessionMap();

        int peptideCount = peptide2Degree.size();
        int degeneratePeptideCount = 0;

        int intraGeneOnly = 0;
        int interGeneOnly = 0;
        int intraGeneAndInterGene = 0;
        int interGeneWithIdenticalSequence = 0;

        for (String peptide : peptide2Degree.keySet()) {

            int sequenceCount = peptide2Degree.get(peptide);
            Set<String> geneAccessions = peptide2GeneAccession.get(peptide);
            int geneCount = geneAccessions.size();

            if (sequenceCount > 1) {

                degeneratePeptideCount++;

                //if inter-gene degeneracy
                if (geneCount == sequenceCount) {
                    interGeneOnly++;
                } //if intra-gene degeneracy
                else if (geneCount < sequenceCount) {

                    if (geneCount == 1) {
                        intraGeneOnly++;
                    } else {
                        intraGeneAndInterGene++;
                    }
                } else if (geneCount > sequenceCount) {
                    interGeneWithIdenticalSequence++;
                }
            }
        }

        DelimitedTableWriter dtw = new DelimitedTableWriter(System.out, "\t", false);

        dtw.writeHeader("peptide_count", "degeneratePeptideCount", "intra-gene-only", "inter-gene-only", "inter-and-intra-gene", "inter-gene-with-ident-sequence");
        dtw.writeRow(peptideCount, degeneratePeptideCount, intraGeneOnly, interGeneOnly, intraGeneAndInterGene, interGeneWithIdenticalSequence);
        dtw.writeRow(peptideCount,
                ((double) degeneratePeptideCount / (double) peptideCount) * 100,
                ((double) intraGeneOnly / (double) degeneratePeptideCount) * 100,
                ((double) interGeneOnly / (double) degeneratePeptideCount) * 100,
                ((double) intraGeneAndInterGene / (double) degeneratePeptideCount) * 100,
                ((double) interGeneWithIdenticalSequence / (double) degeneratePeptideCount) * 100);
    }

    /**
     * @param args
     * @TODO: JavaDoc missing.
     */
    public static void main(String[] args) {
        PeptideDegeneracyReport.reportOriginOfDegeneracy(9606, "argc", "lysc", "v8e");
    }
}
