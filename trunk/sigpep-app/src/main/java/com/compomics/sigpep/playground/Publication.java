package com.compomics.sigpep.playground;

/**
 * @TODO: JavaDoc missing.
 * @TODO: all code in this class is commented out...
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 17-Apr-2008<br/>
 * Time: 14:49:25<br/>
 */
public class Publication {

//    /**
//     * Extracts the genome and proteome coverage by transition size from a SignatureTransitionFinderImpl result file.
//     */
//    public static void parseBarcodeCoverage(String inputFile, String outputFile, int taxonId) {
//
//        try {
//
//            SigPepDatabase sigPepDb = new SigPepDatabase("mmueller", "".toCharArray(), taxonId);
//            SigPepQueryServiceImpl sigPepQueryService = new SigPepQueryServiceImpl(sigPepDb);
//
//            Set<String> altSplicedGenes = sigPepQueryService.fetchAlternativelySplicedGenesTranscriptLevel();
//            Set<String> altSplicedProteins = sigPepQueryService.fetchAlternativelySplicedProteinsTranscriptLevel();
//
//
//            PrintWriter pw = new PrintWriter(outputFile);
//            DelimitedTableWriter dtw = new DelimitedTableWriter(pw, "\t", false);
//
//            InputStream is = new FileInputStream(inputFile);
//            DelimitedTableReader dtr = new DelimitedTableReader(is, "\t");
//
//            Map<String, TreeSet<Integer>> proteinAccessionToTransitionSize = new HashMap<String, TreeSet<Integer>>();
//            Map<String, TreeSet<Integer>> geneAccessionToTransitionSize = new HashMap<String, TreeSet<Integer>>();
//
//            TreeMap<Integer, Set<String>> minTransitionSizeToProteinAccessionAltSplice = new TreeMap<Integer, Set<String>>();
//            TreeMap<Integer, Set<String>> minTransitionSizeToProteinAccessionNonAltSplice = new TreeMap<Integer, Set<String>>();
//
//            TreeMap<Integer, Set<String>> minTransitionSizeToGeneAccessionAltSplice = new TreeMap<Integer, Set<String>>();
//            TreeMap<Integer, Set<String>> minTransitionSizeToGeneAccessionNonAltSplice = new TreeMap<Integer, Set<String>>();
//            int zeroTransition = 0;
//            for (Iterator<String[]> rows = dtr.read(); rows.hasNext();) {
//
//                String[] row = rows.next();
//
//                //try {
//
//                    int transitionSize = new Integer(row[7]);
//                    String proteinAccession = row[8];
//                    String geneAccession = row[9];
//
//                    if (!proteinAccessionToTransitionSize.containsKey(proteinAccession)) {
//                        proteinAccessionToTransitionSize.put(proteinAccession, new TreeSet<Integer>());
//                    }
//
//                    if (transitionSize > 0) {
//                        proteinAccessionToTransitionSize.get(proteinAccession).add(transitionSize);
//                    }
//                    if (!geneAccessionToTransitionSize.containsKey(geneAccession)) {
//                        geneAccessionToTransitionSize.put(geneAccession, new TreeSet<Integer>());
//                    }
//
//                    if (transitionSize > 0) {
//                        geneAccessionToTransitionSize.get(geneAccession).add(transitionSize);
//                    }
//
//            }
//
//
//            System.out.println("0 transitions = " + zeroTransition);
//
//            for (String proteinAccession : proteinAccessionToTransitionSize.keySet()) {
//
//                TreeSet<Integer> transitionSizes = proteinAccessionToTransitionSize.get(proteinAccession);
//                int minTransitionSize = 0;
//                try {
//                    minTransitionSize = transitionSizes.first();
//                } catch (NoSuchElementException e) {
//
//                }
//                if (!minTransitionSizeToProteinAccessionAltSplice.containsKey(minTransitionSize)) {
//                    minTransitionSizeToProteinAccessionAltSplice.put(minTransitionSize, new HashSet<String>());
//                }
//
//                if (!minTransitionSizeToProteinAccessionNonAltSplice.containsKey(minTransitionSize)) {
//                    minTransitionSizeToProteinAccessionNonAltSplice.put(minTransitionSize, new HashSet<String>());
//                }
//
//                if (altSplicedProteins.contains(proteinAccession)) {
//                    minTransitionSizeToProteinAccessionAltSplice.get(minTransitionSize).add(proteinAccession);
//                } else {
//                    minTransitionSizeToProteinAccessionNonAltSplice.get(minTransitionSize).add(proteinAccession);
//                }
//            }
//
//            for (String geneAccession : geneAccessionToTransitionSize.keySet()) {
//
//                TreeSet<Integer> transitionSizes = geneAccessionToTransitionSize.get(geneAccession);
//                int minTransitionSize = 0;
//                try {
//                    minTransitionSize = transitionSizes.first();
//                } catch (NoSuchElementException e) {
//
//                }
//                if (!minTransitionSizeToGeneAccessionAltSplice.containsKey(minTransitionSize)) {
//                    minTransitionSizeToGeneAccessionAltSplice.put(minTransitionSize, new HashSet<String>());
//                }
//
//                if (!minTransitionSizeToGeneAccessionNonAltSplice.containsKey(minTransitionSize)) {
//                    minTransitionSizeToGeneAccessionNonAltSplice.put(minTransitionSize, new HashSet<String>());
//                }
//
//                if (altSplicedGenes.contains(geneAccession)) {
//                    minTransitionSizeToGeneAccessionAltSplice.get(minTransitionSize).add(geneAccession);
//                } else {
//                    minTransitionSizeToGeneAccessionNonAltSplice.get(minTransitionSize).add(geneAccession);
//                }
//            }
//
//            dtw.writeHeader("transition_size",
//                    "coverage_proteome_altsplice",
//                    "coverage_proteome_non_altsplice",
//                    "genome_altsplice_coverage",
//                    "genome_non_altsplice_coverage");
//
//            for (Integer transitionSize : minTransitionSizeToGeneAccessionNonAltSplice.keySet()) {
//
//                dtw.writeRow(transitionSize,
//                        minTransitionSizeToProteinAccessionAltSplice.get(transitionSize).size(),
//                        minTransitionSizeToProteinAccessionNonAltSplice.get(transitionSize).size(),
//                        minTransitionSizeToGeneAccessionAltSplice.get(transitionSize).size(),
//                        minTransitionSizeToGeneAccessionNonAltSplice.get(transitionSize).size());
//
//            }
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (DatabaseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//
//    }
//
//    public static void printPeptideMasses(String outputFile,
//                                          int taxonId,
//                                          Set<String> proteaseNames,
//                                          Set<String> ptmNames) {
//
//        try {
//
//            ApplicationContext appContext = new ClassPathXmlApplicationContext("config/applicationContext.xml");
//            SigPepSessionFactory sessionFactory = (SigPepSessionFactory)appContext.getBean("sigPepSessionFactory");
//            SigPepSession session = sessionFactory.createSigPepSession(9606);
//
//            SigPepDatabase sigPepDb = new SigPepDatabase("mmueller", "".toCharArray(), taxonId);
//            SigPepQueryServiceImpl sigPepQueryService = new SigPepQueryServiceImpl(sigPepDb);
//
//            Set<Modification> ptms = new HashSet<Modification>();
//            ptms.addAll(ModificationFactory.createPostTranslationalModifications(ptmNames));
//
//            PeptideGeneratorImpl pg = session.createPeptideGenerator(proteaseNames);
//            if (ptms.size() != 0) {
//                pg.setPostTranslationalModifications(ptms);
//            }
//
//            PrintWriter pw = new PrintWriter(outputFile);
//            for (Peptide peptide : pg.getPeptideIons()) {
//                pw.println(SigPepUtil.round(peptide.getPrecursorIon().getNeutralMassPeptide(), 4));
//                pw.flush();
//            }
//            pw.close();
//
//        } catch (DatabaseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }
//
//    public static void generateSignatureTransitionExample() {
//
//        try {
//
//            ApplicationContext appContext = new ClassPathXmlApplicationContext("config/applicationContext.xml");
//            SigPepSessionFactory sessionFactory = (SigPepSessionFactory)appContext.getBean("sigPepSessionFactory");
//            SigPepSession session = sessionFactory.createSigPepSession(9606);
//
//            SigPepDatabase sigPepDb = new SigPepDatabase("mmueller", "".toCharArray(), 9606);
//
//            SigPepQueryServiceImpl sigPepQueryService = new SigPepQueryServiceImpl(sigPepDb);
//
//            Set<String> proteaseNames = new HashSet<String>();
//            proteaseNames.add("lysc");
//            proteaseNames.add("argc");
//            proteaseNames.add("v8e");
//
//            PeptideGeneratorImpl pg = session.createPeptideGenerator(proteaseNames);
//            Set<Peptide> backgroundPeptides = pg.getPeptideIons();
//
//            SignatureTransitionFinderImpl sigTransFinder = new SignatureTransitionFinderImpl(backgroundPeptides);
//
//            Set<Peptide> targetPeptides = pg.getPeptideIons("ENSP", 1);
//            Set<ProductIonType> targetProductIonTypes = new HashSet<ProductIonType>();
//            Set<ProductIonType> backgroundProductIonTypes = new HashSet<ProductIonType>();
//
//
//            List<Transition> transitions = sigTransFinder.findSignatureTransition(targetPeptides, targetProductIonTypes, backgroundProductIonTypes, 2, 2, 1);
//
//
//        } catch (DatabaseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//
//    }
//
//    public static void main(String[] args) {
//
////        String outputFile = args[0];
////        int taxonId = new Integer(args[1]);
////        String proteaseFilter = args[2];
////        String ptms = args[3];
////
////        Set<String> proteaseNames = new HashSet<String>();
////        Collections.addAll(proteaseNames, proteaseFilter.split(","));
////
////        Set<String> ptmNames = new HashSet<String>();
////        Collections.addAll(ptmNames, ptms.split(","));
////
////        Publication.printPeptideMasses(outputFile, taxonId, proteaseNames, ptmNames);
//
//
//        Publication.parseBarcodeCoverage(
//                "/home/mmueller/svn/manuscripts/sigpep/data/figure_6/barcodes_9606_zmin2_zmax2_acc1_lysc,argc,v8e_ptm_metdiox,trpdiox,cystriox_new_no_ids.tab",
//                "/home/mmueller/svn/manuscripts/sigpep/data/figure_6/dummy.tab",
//                9606
//        );
//
//    }

}


