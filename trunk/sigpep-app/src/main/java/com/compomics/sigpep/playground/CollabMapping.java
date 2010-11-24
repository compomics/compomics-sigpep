package com.compomics.sigpep.playground;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.compomics.dbtools.DatabaseException;
import com.compomics.sigpep.PeptideGenerator;
import com.compomics.sigpep.SigPepQueryService;
import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.SigPepSessionFactory;
import com.compomics.sigpep.analysis.SignatureTransitionFinder;
import com.compomics.sigpep.analysis.SignatureTransitionFinderType;
import com.compomics.sigpep.model.*;
import com.compomics.sigpep.persistence.util.HibernateUtil;
import com.compomics.sigpep.util.DelimitedTableReader;
import com.compomics.sigpep.util.DelimitedTableWriter;
import com.compomics.sigpep.util.SigPepUtil;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 11-Mar-2008<br/>
 * Time: 14:33:17<br/>
 */
public class CollabMapping {

    /**
     * The log4j logger
     */
    private static Logger logger = Logger.getLogger(CollabMapping.class);

    public static void getPeptideCardinality(String inputFile, String outputFile) throws IOException, DatabaseException {

        ApplicationContext appContext = new ClassPathXmlApplicationContext("config/applicationContext.xml");
        SigPepSessionFactory sessionFactory = (SigPepSessionFactory) appContext.getBean("sigPepSessionFactory");
        Organism organism = sessionFactory.getOrganism(9606);
        SigPepSession session = sessionFactory.createSigPepSession(organism);

        InputStream is = new FileInputStream(inputFile);
        OutputStream os = new FileOutputStream(outputFile);

        DelimitedTableReader dtr = new DelimitedTableReader(is, "\t");
        DelimitedTableWriter dtw = new DelimitedTableWriter(os, "\t", false);

        logger.info("reading peptides from file " + inputFile);
        //read peptides into set
        Set<String> peptides = new HashSet<String>();

        for (Iterator<String[]> rows = dtr.read(); rows.hasNext();) {

            String[] row = rows.next();
            String peptide = row[0];
            peptides.add(peptide);

        }

        logger.info("fetching peptide cardinality from database");

        SigPepQueryService queryService = session.createSigPepQueryService();

        PeptideGenerator pg = session.createPeptideGenerator("tryp");

        Map<String, Integer> peptide2Cardinality = pg.getPeptideSequenceDegeneracy();

        logger.info("writing results to file " + outputFile);
        for (String peptide : peptides) {


            int cardinality = -1;
            if (peptide2Cardinality.containsKey(peptide)) {
                cardinality = peptide2Cardinality.get(peptide);
            }

            dtw.writeRow(peptide, cardinality);

        }


    }

    public static void joinQtrapQstarSrmIdentifications(String qstarQtrapIdentInputFile,
                                                        String srmIdentFile,
                                                        String outputFile) throws IOException {

        DelimitedTableReader dtrQ = new DelimitedTableReader(new FileInputStream(qstarQtrapIdentInputFile), "\t");
        DelimitedTableReader dtrSrm = new DelimitedTableReader(new FileInputStream(srmIdentFile), "\t");
        DelimitedTableWriter dtw = new DelimitedTableWriter(new FileOutputStream(outputFile), "\t", false);

        Map<String, Set<String>> srmIdentPeptide = new HashMap<String, Set<String>>();
        Map<String, Set<String>> qIdentPeptide = new HashMap<String, Set<String>>();
        Map<String, Integer> peptideCard = new HashMap<String, Integer>();

        //read SRM identifications and peptides
        //and put them into map with protein accession
        //as key
        for (Iterator<String[]> rows = dtrSrm.read(); rows.hasNext();) {

            String[] row = rows.next();
            String ensemblId = row[0];
            String peptide = row[1];

            if (!srmIdentPeptide.containsKey(ensemblId)) {
                srmIdentPeptide.put(ensemblId, new HashSet<String>());
            }
            srmIdentPeptide.get(ensemblId).add(peptide);


        }

        //read qtrap/qstar identifications and peptides
        //and put them into a protein id -> peptide sequence map
        //and a peptide -> peptide cardinality map
        for (Iterator<String[]> rows = dtrQ.read(); rows.hasNext();) {

            String[] row = rows.next();
            String ensemblId = row[1];
            String peptide = row[2];
            int cardinality = new Integer(row[3]);

            if (!qIdentPeptide.containsKey(ensemblId)) {
                qIdentPeptide.put(ensemblId, new HashSet<String>());
            }
            qIdentPeptide.get(ensemblId).add(peptide);

            peptideCard.put(peptide, cardinality);

        }

        //write join table to file
        //format: protein id | peptide sequence qtrap/qstar | peptide sequence SRM | peptide cardinality
        for (String ensemblId : qIdentPeptide.keySet()) {

            Set<String> qPeptides = qIdentPeptide.get(ensemblId);

            if (srmIdentPeptide.containsKey(ensemblId)) {

                Set<String> srmPeptides = srmIdentPeptide.get(ensemblId);

                for (String qPeptide : qPeptides) {

                    if (srmPeptides.contains(qPeptide)) {

                        dtw.writeRow(ensemblId, qPeptide, qPeptide, peptideCard.get(qPeptide));

                        srmPeptides.remove(qPeptide);

                    } else {

                        dtw.writeRow(ensemblId, qPeptide, "null", peptideCard.get(qPeptide));

                    }


                }

                for (String srmPeptide : srmPeptides) {

                    dtw.writeRow(ensemblId, "null", srmPeptide, 1);

                }

            }
        }


    }

    /**
     * Does a left joing of SRM identifications (proteins and peptides) and
     * the unique SRM predictions.
     *
     * @param srmIdentFile file containing the proteins and peptides identified by SRM
     * @param srmPredFile  file containing the predicted unique peptides with unique SRM transition
     * @param outputFile   the outputfile
     * @throws IOException if an exception occurs during file access
     */
    public static void leftJoinSrmIdentificationsAndPredictions(String srmIdentFile,
                                                                String srmPredFile,
                                                                String outputFile)
            throws IOException, DatabaseException {

        ApplicationContext appContext = new ClassPathXmlApplicationContext("config/applicationContext.xml");
        SigPepSessionFactory sessionFactory = (SigPepSessionFactory) appContext.getBean("sigPepSessionFactory");
        Organism organism = sessionFactory.getOrganism(9606);
        SigPepSession session = sessionFactory.createSigPepSession(organism);


        DelimitedTableReader dtrSrmIdent = new DelimitedTableReader(new FileInputStream(srmIdentFile), "\t");
        DelimitedTableReader dtrSrmPred = new DelimitedTableReader(new FileInputStream(srmPredFile), "\t");
        DelimitedTableWriter dtw = new DelimitedTableWriter(new FileOutputStream(outputFile), "\t", false);

        Map<String, Set<String>> srmIdentPeptide = new HashMap<String, Set<String>>();
        Map<String, Set<String>> srmPredPeptide = new HashMap<String, Set<String>>();

        //read SRM identifications and peptides
        //and put them into map with protein accession
        //as key
        logger.info("reading SRM identifications from " + srmIdentFile);
        for (Iterator<String[]> rows = dtrSrmIdent.read(); rows.hasNext();) {

            String[] row = rows.next();
            String ensemblId = row[0];
            String peptide = row[1];

            if (!srmIdentPeptide.containsKey(ensemblId)) {
                srmIdentPeptide.put(ensemblId, new HashSet<String>());
            }
            srmIdentPeptide.get(ensemblId).add(peptide);


        }

        //read SRM predictions and peptides
        //and put them into map with protein accession
        //as key
        logger.info("reading SRM predictions from " + srmPredFile);
        for (Iterator<String[]> rows = dtrSrmPred.read(); rows.hasNext();) {

            String[] row = rows.next();
            String ensemblId = row[1];
            String peptide = row[2];

            if (!srmPredPeptide.containsKey(ensemblId)) {
                srmPredPeptide.put(ensemblId, new HashSet<String>());
            }
            srmPredPeptide.get(ensemblId).add(peptide);

        }

        logger.info("fetching peptide cardinality from database");

        PeptideGenerator pg = session.createPeptideGenerator("tryp");

        Map<String, Integer> peptide2Cardinality = pg.getPeptideSequenceDegeneracy();

        logger.info("writing join to file " + outputFile);
        //write join table to file
        //format: protein id | peptide sequence SRM identification | peptide sequence SRM prediction | peptide cardinality

        for (String ensemblId : srmIdentPeptide.keySet()) {

            for (String peptide : srmIdentPeptide.get(ensemblId)) {
                System.out.println(ensemblId + " " + peptide);
            }

        }

        for (String ensemblIdIdent : srmIdentPeptide.keySet()) {

            String ensemblIdPred = "null";

            Set<String> srmIdentPeptides = srmIdentPeptide.get(ensemblIdIdent);

            Set<String> srmPredPeptides = new HashSet<String>();

            if (srmPredPeptide.containsKey(ensemblIdIdent)) {
                ensemblIdPred = ensemblIdIdent;
                srmPredPeptides = srmPredPeptide.get(ensemblIdIdent);
            }


            for (String peptideIdent : srmIdentPeptides) {

                String peptidePred = "null";
                Integer peptideCardinality = peptide2Cardinality.get(peptideIdent);
                if (srmPredPeptides.contains(peptideIdent)) {
                    peptidePred = peptideIdent;
                }

                dtw.writeRow(ensemblIdIdent, ensemblIdPred, peptideIdent, peptidePred, peptideCardinality);


            }


        }

    }

    public static void convertTransitions(String inputFile, String outputFile) throws IOException {

        InputStream is = new FileInputStream(inputFile);
        OutputStream os = new FileOutputStream(outputFile);

        DelimitedTableReader dtr = new DelimitedTableReader(is, "\t");
        DelimitedTableWriter dtw = new DelimitedTableWriter(os, "\t", false);

        dtw.writeHeader("Use", "Q1 Mass", "Q3 Mass", "Sequence", "Start Position", "End Position", "Fragment Type", "Mr", "Charge", "CE");
        Iterator<String[]> rows = dtr.read();
        rows.next();
        while (rows.hasNext()) {

            String[] row = rows.next();

            String[] q3Masses = row[8].split(";");   //m/z products
            String[] types = row[7].split(";");   //product types
            String sequence = row[2];             //peptide sequence
            Peptide peptide = PeptideFactory.createPeptide(sequence);
            int charge = 3;
            double mr = SigPepUtil.round(peptide.getPrecursorIon().getNeutralMassPeptide(), 4);
            double q1Mass = SigPepUtil.round(peptide.getPrecursorIon().getMassOverCharge(charge), 4);   //m/z precursor            
            int[] startCoordinate = new int[types.length];
            int[] endCoordinate = new int[types.length];
            String[] fragmentType = new String[types.length];
            int t = 0;
            for (String type : types) {

                fragmentType[t] = type.split("_")[0];
                int fragmentLength = new Integer(type.split("_")[1]);
                startCoordinate[t] = sequence.length() - fragmentLength + 1;
                endCoordinate[t] = sequence.length();
                t++;

            }

            for (int i = 0; i < startCoordinate.length; i++) {
                dtw.writeRow("Use", q1Mass, q3Masses[i], sequence, startCoordinate[i], endCoordinate[i], fragmentType[i], mr, charge, "");
            }

        }

        is.close();
        os.close();

    }

    public static void exportProteinFastaFile(String filename) throws FileNotFoundException {

        SessionFactory sf = HibernateUtil.getSessionFactory(9606);
        Session s = sf.openSession();

        PrintWriter pw = new PrintWriter(filename);

        Query q = s.createQuery("from ProteinSequence");
        int counter = 0;
        for (Iterator<ProteinSequence> sequences = q.iterate(); sequences.hasNext();) {

            ProteinSequence sequence = sequences.next();
            String sequenceString = sequence.getSequenceString();
            StringBuilder accessions = new StringBuilder(">");
            for (Protein protein : sequence.getProteins()) {

                accessions.append(protein.getPrimaryDbXref().getAccession())
                        .append(";");

            }
            accessions.deleteCharAt(accessions.length() - 1);

            pw.println(accessions);
            pw.println(sequenceString);
            pw.println();
            counter++;
        }

        pw.close();
        System.out.println(counter + " sequences exported.");
    }

    public static void checkPeptideMapping() throws FileNotFoundException {

        try {

            String mappingFile = "/home/mmueller/data/sigpep/collab/qtrap/ident_qtrap_seqid_ipi_3_23_to_ensembl_45.csv";
            String identFile = "/home/mmueller/data/sigpep/collab/qtrap/ident_qtrap.csv";
            String sequenceFile = "/home/mmueller/data/sigpep/collab/qtrap/ident_qtrap_ensembl_45_protein_seq.csv";

            DelimitedTableReader mappingDtr = new DelimitedTableReader(new FileInputStream(mappingFile), "\t");
            DelimitedTableReader identDtr = new DelimitedTableReader(new FileInputStream(identFile), "\t");
            DelimitedTableReader sequenceDtr = new DelimitedTableReader(new FileInputStream(sequenceFile), "\t");


            Map<String, String> ipi2Ensembl = new HashMap<String, String>();
            Set<String> ensemblIds = new HashSet<String>();
            for (Iterator<String[]> mappingIt = mappingDtr.read(); mappingIt.hasNext();) {
                String[] row = mappingIt.next();
                ipi2Ensembl.put(row[0], row[1]);
                ensemblIds.add(row[1]);
            }

            Map<String, Set<String>> ipi2Pep = new HashMap<String, Set<String>>();
            for (Iterator<String[]> identIt = identDtr.read(); identIt.hasNext();) {
                String[] row = identIt.next();

                if (!ipi2Pep.containsKey(row[0])) {
                    ipi2Pep.put(row[0], new HashSet<String>());
                }

                ipi2Pep.get(row[0]).add(row[1]);

            }

            Map<String, String> ensembl2Seq = new HashMap<String, String>();
            for (Iterator<String[]> sequenceIt = sequenceDtr.read(); sequenceIt.hasNext();) {
                String[] row = sequenceIt.next();
                ensembl2Seq.put(row[0], row[1]);
            }

            for (String identification : ipi2Pep.keySet()) {

                if (!ipi2Ensembl.containsKey(identification)) {
                    continue;
                }

                String ensemblId = ipi2Ensembl.get(identification);
                String sequence = ensembl2Seq.get(ensemblId);
                Set<String> peptides = ipi2Pep.get(identification);

                for (String peptide : ipi2Pep.get(identification)) {

                    if (!sequence.contains(peptide)) {
                        System.out.println(identification + "\t" + ensemblId + "\t" + peptide + "\t" + peptides.size());
                    }

                }


            }


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static void signatureTransitionPredictionSanityCheck() throws FileNotFoundException {

        try {


            String transitionFile = "/home/mmueller/data/sigpep/collab/qstar_qtrap_signature_transitions.csv";
            String sequenceFile = "/home/mmueller/data/sigpep/collab/ident_qstar_qtrap_ensembl_45_protein_seq.csv";


            DelimitedTableReader transitionDtr = new DelimitedTableReader(new FileInputStream(transitionFile), "\t");
            DelimitedTableReader sequenceDtr = new DelimitedTableReader(new FileInputStream(sequenceFile), "\t");

            Map<String, Set<String>> ensembl2Pep = new HashMap<String, Set<String>>();
            for (Iterator<String[]> identIt = transitionDtr.read(); identIt.hasNext();) {
                String[] row = identIt.next();
                if (!row[0].equals("#")) {
                    if (!ensembl2Pep.containsKey(row[1])) {
                        ensembl2Pep.put(row[1], new HashSet<String>());
                    }
                    System.out.println(row[1]);
                    ensembl2Pep.get(row[1]).add(row[2]);
                }
            }

            Map<String, String> ensembl2Seq = new HashMap<String, String>();
            for (Iterator<String[]> sequenceIt = sequenceDtr.read(); sequenceIt.hasNext();) {
                String[] row = sequenceIt.next();
                ensembl2Seq.put(row[0], row[1]);
            }

            for (String ensemblId : ensembl2Pep.keySet()) {

                String sequence = ensembl2Seq.get(ensemblId);
                Set<String> peptides = ensembl2Pep.get(ensemblId);

                for (String peptide : ensembl2Pep.get(ensemblId)) {

                    if (!sequence.contains(peptide)) {
                        System.out.println(ensemblId + "\t" + ensemblId + "\t" + peptide + "\t" + peptides.size());
                    }

                }

            }


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static void findSignatureTransitions(Set<String> proteinAccessions, Set<String> proteaseNames, String outputDirectory, String transitionOutputFile) {

        ApplicationContext appContext = new ClassPathXmlApplicationContext("config/applicationContext.xml");
        SigPepSessionFactory sessionFactory = (SigPepSessionFactory) appContext.getBean("sigPepSessionFactory");
        Organism organism = sessionFactory.getOrganism(9606);
        SigPepSession session = sessionFactory.createSigPepSession(organism);

        Set<Integer> precursorIonChargeStates = new HashSet<Integer>();
        precursorIonChargeStates.add(3);

        Set<Integer> productIonChargeStates = new HashSet<Integer>();
        productIonChargeStates.add(3);

        double massAccuracy = 0.5;
        Set<ProductIonType> targetProductIonTypes = new HashSet<ProductIonType>();
        targetProductIonTypes.add(ProductIonType.Y);
        Set<ProductIonType> backgroundProductIonTypes = new HashSet<ProductIonType>();
        backgroundProductIonTypes.add(ProductIonType.Y);
        backgroundProductIonTypes.add(ProductIonType.B);
        Set<String> backgroundPtmNames = new HashSet<String>();
        backgroundPtmNames.add("cyscarbamidmeth");
        backgroundPtmNames.add("glnpyroglu");
        backgroundPtmNames.add("glupyroglu");
        backgroundPtmNames.add("protacetyl");
        Set<Modification> backgroundPtms = ModificationFactory.createPostTranslationalModifications(backgroundPtmNames);

        Set<String> targetPtmNames = new HashSet<String>();
        targetPtmNames.add("cyscarbamidmeth");
        Set<Modification> targetPtms = ModificationFactory.createPostTranslationalModifications(targetPtmNames);


        try {

            logger.info("creating peptide generator...");
            PeptideGenerator pg = session.createPeptideGenerator(proteaseNames);
            pg.setPostTranslationalModifications(backgroundPtms);

            logger.info("fetching peptides generated by " + proteaseNames);
            Set<Peptide> backgroundPeptides = pg.getPeptides();
            Map<String, Set<Peptide>> backgroundPeptideSequence2Peptide = new HashMap<String, Set<Peptide>>();

            logger.info(backgroundPeptides.size() + " peptides");

            logger.info("fetching signature peptides for target proteins...");
            pg.setPostTranslationalModifications(targetPtms);
            Map<String, Set<Peptide>> proteinAccession2SignaturePeptides = pg.getProteinAccessionToPeptideMap(proteinAccessions, 1);
            int signaturePeptideCount = 0;
            for (Set<Peptide> peptides : proteinAccession2SignaturePeptides.values()) {
                signaturePeptideCount = signaturePeptideCount + peptides.size();
            }
            logger.info(signaturePeptideCount + " peptides");

            logger.info("finding signature transitions for signature peptides...");
            PrintWriter pwTransitions = new PrintWriter(outputDirectory + "/" + transitionOutputFile);
            DelimitedTableWriter dtwTransition = new DelimitedTableWriter(pwTransitions, "\t", true);

            SignatureTransitionFinder stf = session.createSignatureTransitionFinder(backgroundPeptides,
                                targetProductIonTypes,
                                backgroundProductIonTypes,
                                precursorIonChargeStates,
                                productIonChargeStates,
                                massAccuracy,
                                1,
                                10,
                                SignatureTransitionFinderType.FIRST);

            for (String proteinAccession : proteinAccession2SignaturePeptides.keySet()) {
                Set<Peptide> signaturePeptides = proteinAccession2SignaturePeptides.get(proteinAccession);
                for (Peptide signaturePeptide : signaturePeptides) {

                    if (signaturePeptide.getSequenceString().length() > 14
                            || signaturePeptide.getResidueCount("H") > 0) {

                        Set<Peptide> peps = new HashSet<Peptide>();
                        peps.add(signaturePeptide);

                        List<SignatureTransition> transitions = stf.findSignatureTransitions(
                                peps);

                        SignatureTransition signatureTransition = (SignatureTransition) transitions.iterator().next();


                        if (signatureTransition.getProductIons().size() > 0) {

                            StringBuilder productIonNames = new StringBuilder();
                            StringBuilder productIonMasses = new StringBuilder();
                            StringBuilder productIonMassOverCharge = new StringBuilder();

                            for (ProductIon pi : signatureTransition.getProductIons()) {

                                productIonNames.append(pi.getType().toString())
                                        .append("_")
                                        .append(pi.getSequenceLength())
                                        .append(";");

                                productIonMasses.append(SigPepUtil.round(pi.getNeutralMassPeptide(), 4))
                                        .append(";");

                                productIonMassOverCharge.append(SigPepUtil.round(pi.getMassOverCharge(1), 4))
                                        .append(";");
                            }


                            StringBuilder modifications = new StringBuilder();
                            Peptide peptide = signatureTransition.getPeptide();
                            if (peptide.isModified()) {
                                ModifiedPeptide modPep = (ModifiedPeptide) peptide;
                                for (Integer pos : modPep.getPostTranslationalModifications().keySet()) {
                                    Modification ptm = modPep.getPostTranslationalModifications().get(pos);
                                    modifications.append(pos).append("-").append(ptm.getName());
                                }
                            } else {
                                modifications.append("null");
                            }

                            dtwTransition.writeRow(proteinAccession,
                                    signatureTransition.getPeptide().getSequenceString(),
                                    modifications,
                                    SigPepUtil.round(signaturePeptide.getPrecursorIon().getNeutralMassPeptide(), 4),
                                    SigPepUtil.round(signaturePeptide.getPrecursorIon().getMassOverCharge(3), 4),
                                    signatureTransition.getBackgroundPrecursorIonSetSize(),
                                    productIonNames,
                                    productIonMasses,
                                    productIonMassOverCharge);

                            //write background product ion distribution
                            PrintWriter pwBackground = new PrintWriter(outputDirectory + "/" + signaturePeptide.getSequenceString() + "_bg.csv");
                            DelimitedTableWriter dtwBackground = new DelimitedTableWriter(pwBackground, "\t", true);

                            //target Y ions
                            List<ProductIon> bgProductIons = signaturePeptide.getPrecursorIon().getProductIons(ProductIonType.Y);
                            StringBuffer productIonMz = new StringBuffer();
                            int i = 0;
                            for (ProductIon bgPi : bgProductIons) {
                                i++;
                                productIonMz.append(SigPepUtil.round(bgPi.getMassOverCharge(1), 4));
                                if (i < bgProductIons.size()) {
                                    productIonMz.append("\t");
                                }
                            }

                            dtwBackground.writeRow(0, "T", productIonMz);

                            //signature ions
                            bgProductIons = signatureTransition.getProductIons();
                            productIonMz = new StringBuffer();
                            i = 0;
                            for (ProductIon bgPi : bgProductIons) {
                                i++;
                                productIonMz.append(SigPepUtil.round(bgPi.getMassOverCharge(1), 4));
                                if (i < bgProductIons.size()) {
                                    productIonMz.append("\t");
                                }
                            }

                            dtwBackground.writeRow(0, "S", productIonMz);


                            int peptideCount = 0;
                            for (Peptide bgPeptide : signatureTransition.getBackgroundPeptides()) {

                                peptideCount++;

                                //background Y ions
                                bgProductIons = bgPeptide.getPrecursorIon().getProductIons(ProductIonType.Y);
                                productIonMz = new StringBuffer();

                                for (ProductIon bgPi : bgProductIons) {

                                    productIonMz.append(SigPepUtil.round(bgPi.getMassOverCharge(1), 4));
                                    if (i < bgProductIons.size()) {
                                        productIonMz.append("\t");
                                    }
                                }

                                dtwBackground.writeRow(peptideCount, "Y", productIonMz);

                                //background Z ions
                                bgProductIons = bgPeptide.getPrecursorIon().getProductIons(ProductIonType.B);
                                productIonMz = new StringBuffer();
                                i = 0;
                                for (ProductIon bgPi : bgProductIons) {
                                    i++;
                                    productIonMz.append(SigPepUtil.round(bgPi.getMassOverCharge(1), 4));
                                    if (i < bgProductIons.size()) {
                                        productIonMz.append("\t");
                                    }
                                }

                                dtwBackground.writeRow(peptideCount, "B", productIonMz);

                            }
                            pwBackground.close();

                        }
                    }
                }
            }

            pwTransitions.close();
            logger.info("done");

        }
        catch (FileNotFoundException e) {
            logger.error(e);
        }


    }

    public static void main(String[] args) {

        try {

//            String sequenceFile = "/home/mmueller/data/sigpep/collab/ident_qstar_qtrap_ensembl_45_protein_seq.csv";
            String transitionOutput = "ident_qstar_qtrap_signature_transitions_plus_3.tab";
//            DelimitedTableReader sequenceDtr = new DelimitedTableReader(new FileInputStream(sequenceFile), "\t");
//
//            Map<String, String> ensembl2Seq = new HashMap<String, String>();
//            for (Iterator<String[]> sequenceIt = sequenceDtr.read(); sequenceIt.hasNext();) {
//                String[] row = sequenceIt.next();
//                ensembl2Seq.put(row[0], row[1]);
//            }
//
//            Set<String> proteaseNames = new HashSet<String>();
//            proteaseNames.add("tryp");
//

//
//            CollabMapping.findSignatureTransitions(ensembl2Seq.keySet(), proteaseNames, "/home/mmueller/data/sigpep/collab/signature_transition/prediction/plus_3", transitionOutput);
//
//            CollabMapping.signatureTransitionPredictionSanityCheck();
//
//            CollabMapping.exportProteinFastaFile("/home/mmueller/data/sigpep/collab/ensembl_45.fasta");

            CollabMapping.convertTransitions("/home/mmueller/data/sigpep/collab/signature_transition/prediction/plus_3/" + transitionOutput,
                    "/home/mmueller/data/sigpep/collab/signature_transition/prediction/plus_3/ident_qstar_qtrap_signature_transitions_plus_3_transformed.tab");

            //CollabMapping.getPeptideCardinality("/home/mmueller/data/sigpep/collab/ident_qstar_qtrap_peptides.csv", "/home/mmueller/data/sigpep/collab/ident_qstar_qtrap_peptide_cardinality.csv");

//            CollabMapping.joinQtrapQstarSrmIdentifications(
//                    "/home/mmueller/data/sigpep/collab/ident_qstar_qtrap.tab",
//                    "/home/mmueller/data/sigpep/collab/results/27_05_2008_identified_proteins_and_peptides.tab",
//                    "/home/mmueller/data/sigpep/collab/results/qstar_qtrap_srm_union.tab");

//            CollabMapping.leftJoinSrmIdentificationsAndPredictions(
//                    "/home/mmueller/data/sigpep/collab/results/23_06_2008_identified_proteins_and_peptides.tab",
//                    "/home/mmueller/data/sigpep/collab/signature_transition/prediction/ident_qstar_qtrap_signature_transitions.csv",
//                    "/home/mmueller/data/sigpep/collab/results/srm_identification_prediction_join.tab");

        } catch (IOException e) {
            logger.error(e);

        }

    }

}
