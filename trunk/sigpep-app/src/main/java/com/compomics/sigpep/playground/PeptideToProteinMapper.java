package com.compomics.sigpep.playground;

/**
 * @TODO: JavaDoc missing.
 * @TODO: all code in this class is commented out...
 *
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 18-Mar-2008<br/>
 * Time: 14:42:23<br/>
 */
public class PeptideToProteinMapper {

     /**
     * The log4j logger
     */
//    protected static Logger logger = Logger.getLogger(SigPepQueryServiceImpl.class);
//
//    public static void main(String[] args) {
//
//        try {
//
//            String inputFile = "/home/mmueller/svn/manuscripts/sigpep/data/figure_6/barcodes_9606_zmin2_zmax2_acc1_lysc,argc,v8e_ptm_metdiox,trpdiox,cystriox.tab";
//            String outputFileProteins ="/home/mmueller/svn/manuscripts/sigpep/data/figure_6/barcodes_9606_zmin2_zmax2_acc1_lysc,argc,v8e_ptm_metdiox,trpdiox,cystriox_proteins.tab";
//            String outputFileGenes = "/home/mmueller/svn/manuscripts/sigpep/data/figure_6/barcodes_9606_zmin2_zmax2_acc1_lysc,argc,v8e_ptm_metdiox,trpdiox,cystriox_genes.tab";
//
//            PrintWriter outputWriter = new PrintWriter(outputFileProteins);
//            PrintWriter outputWriterGenes = new PrintWriter(outputFileGenes);
//
//            logger.info("reading peptide sequences from " + inputFile);
//            DelimitedTableReader dtr = new DelimitedTableReader(new FileInputStream(inputFile), "\t");
//            Set<String> queryPeptideSequences = new HashSet<String>();
//
//            int noTransition = 0;
//            for(Iterator<String[]> rows = dtr.read(); rows.hasNext();){
//                 //LQEQEEMIR       1206.555        9       true    4292    0               0
//                String[] row = rows.next();
//                String peptideSequence = row[0];
//                String transition = row[5];
//
//                if(!transition.equals("0")){
//                    queryPeptideSequences.add(peptideSequence);
//                } else {
//                    noTransition++;
//                }
//            }
//
//            logger.info(noTransition + " peptides without transition");
//
//            logger.info("querying database");
//            Set<String> proteaseFilter = new HashSet<String>();
//            proteaseFilter.add("lysc");
//            proteaseFilter.add("argc");
//            proteaseFilter.add("v8e");
//
//            SigPepDatabase sigPepDb = new SigPepDatabase("mmueller", "".toCharArray(), 9606);
//            SigPepQueryServiceImpl sigPepQueryService = new SigPepQueryServiceImpl(sigPepDb);
//
//            PeptideGeneratorImpl peptideGenerator = sigPepQueryService.createPeptideGenerator(proteaseFilter);
//
//
//            logger.info("writing query result proteins to " + outputFileProteins);
//
//            Map<String, Set<String>> peptideSequenceToProteinAccessionMap = peptideGenerator.getPeptideSequenceToProteinAccessionMap();
//            Set<String> uniqueProteinAccessions = new HashSet<String>();
//            for(String peptideSequence : queryPeptideSequences){
//
//                Set<String> proteinAccessions = peptideSequenceToProteinAccessionMap.get(peptideSequence);
//                uniqueProteinAccessions.addAll(proteinAccessions);
//
//
//            }
//
//            DelimitedTableWriter dtw = new DelimitedTableWriter(outputWriter, "\t", false);
//            for(String proteinAccession : uniqueProteinAccessions){
//                dtw.writeRow(proteinAccession);
//            }
//            outputWriter.close();
//
//            logger.info("writing query result genes to " + outputFileGenes);
//
//            Map<String, Set<String>> peptideSequenceToGeneAccessionMap = peptideGenerator.getPeptideSequenceToGeneAccessionMap();
//            Set<String> uniqueGeneAccessions = new HashSet<String>();
//            for(String peptideSequence : queryPeptideSequences){
//
//                Set<String> geneAccessions = peptideSequenceToGeneAccessionMap.get(peptideSequence);
//                uniqueGeneAccessions.addAll(geneAccessions);
//
//
//            }
//
//            DelimitedTableWriter dtwGenes = new DelimitedTableWriter(outputWriterGenes, "\t", false);
//            for(String geneAccession : uniqueGeneAccessions){
//                dtwGenes.writeRow(geneAccession);
//            }
//            outputWriterGenes.close();
//
//        } catch (DatabaseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//
//    }
}
