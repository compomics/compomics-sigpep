package com.compomics.sigpep.analysis;

import org.apache.log4j.Logger;
import com.compomics.sigpep.*;
import com.compomics.sigpep.model.*;
import com.compomics.sigpep.util.SigPepUtil;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-Aug-2008<br/>
 * Time: 13:54:43<br/>
 */
public class SignatureTransitionFinderExecutor {

    protected static Logger logger = Logger.getLogger(SignatureTransitionFinderExecutor.class);

    private static Configuration config = Configuration.getInstance();
    private static int massPrecission = config.getInt("sigpep.app.monoisotopic.mass.precision");

    public static void writeResultEntry(PrintWriter printWriter,
                                        String proteinAccession,
                                        String geneAccession,
                                        SignatureTransition transition) {

        ////
        //1. target peptide ID
        ////
        printWriter.print(transition.getPeptide().getSequenceString() + "\t");

        ////
        //2. target peptide mass
        ////
        printWriter.print(SigPepUtil.round(transition.getPeptide().getPrecursorIon().getNeutralMassPeptide(), massPrecission) + "\t");

        ////
        //3. target peptide length
        ////
        printWriter.print(transition.getPeptide().getSequenceLength() + "\t");

        ////
        //4. target peptide is modified
        ////
        printWriter.print(transition.getPeptide().isModified() + "\t");

        ////
        //5. mass overlap frequency
        ////
        printWriter.print(transition.getBackgroundPrecursorIonSetSize() + "\t");

        ////
        //6. unique product ion combination
        ////
        int i = 1;
        for (ProductIon productIon : transition.getProductIons()) {
            String productIonName = productIon.getType().getName();
            int length = productIon.getSequenceLength();
            if (transition.getProductIons().size() == i++) {
                printWriter.print(productIonName + "_" + length);
            } else {
                printWriter.print(productIonName + "_" + length + ",");
            }

        }
        if (transition.getProductIons().size() == 0) {
            printWriter.print("0");
        }

        printWriter.print("\t");

        ////
        //7. unique product ion mass combination
        ////
        i = 1;
        for (ProductIon productIon : transition.getProductIons()) {
            double productIonMass = productIon.getNeutralMassPeptide();
            if (transition.getProductIons().size() == i++) {
                printWriter.print(SigPepUtil.round(productIonMass, massPrecission));
            } else {
                printWriter.print(SigPepUtil.round(productIonMass, massPrecission) + ",");
            }

        }
        printWriter.print("\t");

        ////
        //8. combination size
        ////
        if (transition.getProductIons().size() == 0) {
            printWriter.print("0");
        } else {
            printWriter.print(transition.getProductIons().size());
        }
        printWriter.print("\t");

        ////
        //9. protein accession
        ////
        printWriter.print(proteinAccession);
        printWriter.print("\t");

        ////
        //10. gene accession
        ////
        printWriter.println(geneAccession);



        ////
        //11. score
        ////
        printWriter.println(transition.getExclusionScore());

        printWriter.flush();

    }


    public static Map<String, String> parseCommandLineArguments(String[] args) {

        //parse command line arguments
        Map<String, String> retVal = new HashMap<String, String>();

        for (String arg : args) {

            String key = arg.split("=")[0].replace("--", "");
            String value = "";
            if (arg.split("=").length > 1) {
                value = arg.split("=")[1];
            }

            retVal.put(key, value);

        }

        return retVal;

    }

    public static void main(String[] args) {

        //default values
        int lowerMassCutOff = 600;
        int upperMassCutOff = 4000;
        Set<Integer> precursorChargeStates = new TreeSet<Integer>();
        precursorChargeStates.add(2);
        Set<Integer> productChargeStates = new TreeSet<Integer>();
        precursorChargeStates.add(1);
        double massAccuracy = 1;
        int connectionTimeIntervalSeconds = 30;
        int connectionAttempts = 180;

        //--user=mmueller --password=******* --taxon=9606 --out=/home/mmueller/transfindtest9606.txt --protease=tryp --ptm=metdiox,trpdiox,cystriox --precursorMinCharge=2 --precursorMaxCharge=2 --massAccuracy=1 --precursorLowerMassCutOff=2000 --precursorUpperMassCutOff=2010

        String usage = "SignatureTransitionFinderExecutor \n" +
                "--user=SIGPEPDB_USERNAME \n" +
                "--password=SIGPEPDB_PASSWORD \n" +
                "--taxon=NCBI_TAXON_ID \n" +
                "--protease=PROTEASE_SHORT_NAME [PROTEASE_SHORT_NAME,...]\n" +
                "[--ptm=POST_TRANSLATIONAL_MODIFICATION[,POST_TRANSLATIONAL_MODIFICATION,...]]\n" +
                "[--precursorIonChargeStates=ALLOWED_CHARGE_STATES_PRECURSOR_IONS,...] (default = 2)\n" +
                "[--productIonChargeStates=ALLOWED_CHARGE_STATES_PRODUCT_IONS,...] (default = 1)\n" +
                "[--massAccuracy=MS_MASS_ACCURACY] (default = 1)\n " +
                "[--precursorLowerMassCutOff=LOWER_MASS_LIMIT_PRECURSOR] (defautl = 600 Da)\n" +
                "[--precursorUpperMassCutOff=UPPER_MASS_LIMIT_PRECURSOR] (default = 4000 Da)\n" +
                "[--peptides=PEPTIDE_ID_INPUT_FILENAME]\n" +
                "--out=PATH_TO_OUTPUT_FILE";

        Map<String, String> commandLineArgs = parseCommandLineArguments(args);

        if (!(commandLineArgs.containsKey("user") &&
                commandLineArgs.containsKey("password") &&
                commandLineArgs.containsKey("taxon") &&
                commandLineArgs.containsKey("protease") &&
                commandLineArgs.containsKey("out"))) {

            System.out.println(usage);
            System.exit(1);

        }

        try {

            String userName = commandLineArgs.get("user");
            String password = commandLineArgs.get("password");
            int taxonId = new Integer(commandLineArgs.get("taxon"));
            String proteaseNames = commandLineArgs.get("protease");
            Set<String> proteases = new HashSet<String>();
            if (proteaseNames.split(",").length > 0) {
                for (String proteaseName : proteaseNames.split(",")) {
                    proteases.add(proteaseName.replaceAll(" ", ""));
                }
            } else {
                logger.error("You must specify at least on protease.");
            }
            String output = commandLineArgs.get("out");
            String precursorChargeStatesString = "";
            if (commandLineArgs.containsKey("precursorIonChargeStates")) {
                precursorChargeStatesString = commandLineArgs.get("precursorIonChargeStates");
                precursorChargeStates = new HashSet<Integer>();
                for (String chargeState : precursorChargeStatesString.split(",")) {
                    precursorChargeStates.add(new Integer(chargeState.trim()));
                }
            }
            String productChargeStatesString = "";
            if (commandLineArgs.containsKey("productIonChargeStates")) {
                productChargeStatesString = commandLineArgs.get("productIonChargeStates");
                productChargeStates = new HashSet<Integer>();
                for (String chargeState : productChargeStatesString.split(",")) {
                    productChargeStates.add(new Integer(chargeState.trim()));
                }
            }
            if (commandLineArgs.containsKey("precursorLowerMassCutOff")) {
                lowerMassCutOff = new Integer(commandLineArgs.get("precursorLowerMassCutOff"));
            }
            if (commandLineArgs.containsKey("precursorUpperMassCutOff")) {
                upperMassCutOff = new Integer(commandLineArgs.get("precursorUpperMassCutOff"));
            }
            if (commandLineArgs.containsKey("massAccuracy")) {
                massAccuracy = new Double(commandLineArgs.get("massAccuracy"));
            }
            String ptmNamesString = "";
            Set<String> ptmNames = new HashSet<String>();
            if (commandLineArgs.containsKey("ptm") && !commandLineArgs.get("ptm").equals("unmod")) {
                ptmNamesString = commandLineArgs.get("ptm");
                Collections.addAll(ptmNames, ptmNamesString.split(","));
            }

            Set<Modification> ptms = ModificationFactory.createPostTranslationalModifications(ptmNames);
            Set<Modification> staticPtms = new HashSet<Modification>();
            for (Modification ptm : ptms) {
                if (ptm.isStatic()) {
                    staticPtms.add(ptm);
                }
            }

            //SigPepDatabase sigPepDatabase = new SigPepDatabase(userName, password.toCharArray(), ncbiTaxonId);

//            ApplicationContext appContext = new ClassPathXmlApplicationContext("config/applicationContext.xml");
//            SigPepSessionFactory sessionFactory = (SigPepSessionFactory) appContext.getBean("sigPepSessionFactory");
            logger.info("locating application...");
            SigPepApplication app = ApplicationLocator.getInstance().getApplication();

            //get session factory
            logger.info("creating session...");
            SigPepSessionFactory sessionFactory = app.getSigPepSessionFactory();

            //get organism instance for organism
            Organism organism = sessionFactory.getOrganism(taxonId);

            //create session for organism
            SigPepSession session = sessionFactory.createSigPepSession(organism);


            logger.info("finding peptide signature transitions...");
            logger.info("species                    : " + session.getOrganism().getScientificName());
            logger.info("proteaseFilter             : " + proteaseNames);
            logger.info("PTM                        : " + ptmNamesString);
            logger.info("peptide mass interval      : " + lowerMassCutOff + " - " + upperMassCutOff + " Da");
            logger.info("precursor ion charge states: " + precursorChargeStates);
            logger.info("product ion charge states  : " + productChargeStates);
            logger.info("MS mass accuracy           : " + massAccuracy);
            logger.info("");
            
            //SigPepQueryService sigPepQuery = session.createSigPepQueryService();
            Map<String, String> proteinAccessionToGeneAccessionMap = session.getSimpleQueryDao().getProteinAccessionToGeneAccessionMap();


            logger.info("fetching peptides generated by protease set " + proteases + "...");
            PeptideGenerator peptideGenerator = session.createPeptideGenerator(proteases);

            //connection.close();

            //get signature peptides
            logger.info("signature peptides...");
            //peptideGenerator.setPostTranslationalModifications(staticPtms);
            Map<String, Set<Peptide>> signaturePeptides = peptideGenerator.getProteinAccessionToPeptideMap(1);


            logger.info("background peptides...");
            peptideGenerator.setPostTranslationalModifications(ptms);
            Set<Peptide> backgroundPeptides = peptideGenerator.getPeptides();
            logger.info(backgroundPeptides.size() + " peptides");


            Set<ProductIonType> targetProductIonTypes = new HashSet<ProductIonType>();
            targetProductIonTypes.add(ProductIonType.Y);

            Set<ProductIonType> backgroundProductIonTypes = new HashSet<ProductIonType>();
            backgroundProductIonTypes.add(ProductIonType.Y);
            backgroundProductIonTypes.add(ProductIonType.B);


            SignatureTransitionFinder finder = session.createSignatureTransitionFinder(
                    backgroundPeptides,
                    targetProductIonTypes,
                    backgroundProductIonTypes,
                    precursorChargeStates,
                    productChargeStates,
                    massAccuracy,
                    1,
                    5,
                    SignatureTransitionFinderType.ALL);

            PrintWriter outputPrintWriter = new PrintWriter(output);
//            PrintWriter outputPrintWriter = new PrintWriter(System.out);

            int processedPeptideCounter = 0;
            int transitionCounter = 0;

            //filter for pepides in mass range
            int peptidesInMassRangeCount = 0;
            Map<String, Set<Peptide>> signaturePeptidesInMassRange = new HashMap<String, Set<Peptide>>();
            for (String proteinAccession : signaturePeptides.keySet()) {

                Set<Peptide> peptidesInMassRange = new HashSet<Peptide>();
                for (Peptide signaturePeptide : signaturePeptides.get(proteinAccession)) {
                    if (signaturePeptide.getPrecursorIon().getNeutralMassPeptide() >= lowerMassCutOff &&
                            signaturePeptide.getPrecursorIon().getNeutralMassPeptide() <= upperMassCutOff) {

                        peptidesInMassRange.add(signaturePeptide);
                        peptidesInMassRangeCount++;
                    }
                }

                signaturePeptidesInMassRange.put(proteinAccession, peptidesInMassRange);

            }
            logger.info(peptidesInMassRangeCount + " signature peptides in mass interval");

            for (String proteinAccession : signaturePeptidesInMassRange.keySet()) {
                for (Peptide signaturePeptide : signaturePeptidesInMassRange.get(proteinAccession)) {

                    List<Peptide> targetPeptide = new ArrayList<Peptide>();
                    targetPeptide.addAll(signaturePeptide.applyModifications(staticPtms));
                    if (targetPeptide.size() > 1) {
                        logger.warn("more then one modified target peptide!" + signaturePeptide);
                    } else if (targetPeptide.size() == 0) {
                        targetPeptide.add(signaturePeptide);
                    }

                    List<SignatureTransition> signatureTransitions = finder.findSignatureTransitions(targetPeptide);

                    //get gene accession
                    String geneAccession = "null";
                    if (proteinAccessionToGeneAccessionMap.containsKey(proteinAccession)) {
                        geneAccession = proteinAccessionToGeneAccessionMap.get(proteinAccession);
                    }

                    for (Transition signatureTransition : signatureTransitions) {
                        transitionCounter++;
                        SignatureTransition transition = (SignatureTransition) signatureTransition;
                        writeResultEntry(outputPrintWriter, proteinAccession, geneAccession, transition);
                    }

                    processedPeptideCounter++;

                    //some user feedback
                    if (processedPeptideCounter % 100 == 0) {
                        logger.info(processedPeptideCounter + " target peptides of " + peptidesInMassRangeCount + " processed, " + transitionCounter + " transitions found ...");
                    }

                }
            }

            if (processedPeptideCounter != peptidesInMassRangeCount) {
                logger.warn("Not all input peptides have been processed!!! " + peptidesInMassRangeCount + " input peptides, " + processedPeptideCounter + " processed.");
            }

            logger.warn("done");

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }


    }

//    private Map<Double, Set<Peptide>> populateTargetPrecursorStore(Collection<? extends Peptide> peptides) {
//
//        Map<Double, Set<Peptide>> retVal = new TreeMap<Double, Set<Peptide>>();
//
//        for (Peptide peptide : peptides) {
//
//            double mass = SigPepUtil.round(peptide.getPrecursorIon().getNeutralMassPeptide(), 4);
//
//            if (!retVal.containsKey(mass)) {
//                retVal.put(mass, new HashSet<Peptide>());
//            }
//            retVal.get(mass).add(peptide);
//        }
//
//        return retVal;
//
//    }

//    private Set<Peptide> getOverlappingPrecursors(double peptideMass,
//                                                  int minimumPrecursorIonCharge,
//                                                  int maximumPrecursorIonCharge,
//                                                  double massAccuracy) {
//
//        Set<Peptide> retVal = new HashSet<Peptide>();
//
//        MassOverChargeRange targetPeptideMassOverChargeRange = new MassOverChargeRangeImpl(peptideMass, minimumPrecursorIonCharge, maximumPrecursorIonCharge, massAccuracy);
//
//        for (MassOverChargeRange[] backgroundPeptideMassOverChargeRange : targetPeptideMassOverChargeRange.getFlankingPeptideMassOverChargeRanges()) {
//
//            double lowerFlankingMass = backgroundPeptideMassOverChargeRange[0].getNeutralPeptideMass();
//            double upperFlankingMass = backgroundPeptideMassOverChargeRange[1].getNeutralPeptideMass();
//
////            try {
//            Map<Double, Set<Peptide>> backgroundPeptidesInMassRange = backgroundPrecursorStore.subMap(lowerFlankingMass, upperFlankingMass);
//
////                Map<Double, Set<Peptide>> backgroundPeptidesInMassRange = temporaryPrecursorDatabase.fetchPeptidesByMass(lowerFlankingMass, upperFlankingMass);
//            for (Double mass : backgroundPeptidesInMassRange.keySet()) {
//                for (Peptide peptide : backgroundPeptidesInMassRange.get(mass)) {
//                    retVal.add(peptide);
//                }
//            }
////            } catch (SQLException e) {
////                logger.error("Exception while fetching peptides overlapping with target peptide mass " + peptideMass + " Da.", e);
////            }
//
//        }
//
//        return retVal;
//
//    }
}
