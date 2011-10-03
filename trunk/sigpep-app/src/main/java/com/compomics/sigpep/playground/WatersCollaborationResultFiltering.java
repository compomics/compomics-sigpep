package com.compomics.sigpep.playground;

import com.compomics.sigpep.model.Modification;
import com.compomics.sigpep.model.ModificationFactory;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.PeptideFactory;
import com.compomics.sigpep.util.DelimitedTableReader;
import com.compomics.sigpep.util.DelimitedTableWriter;
import com.compomics.sigpep.util.SigPepUtil;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @TODO: JavaDoc missing.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 22-Aug-2008<br/>
 * Time: 16:31:08<br/>
 */
public class WatersCollaborationResultFiltering {

    /**
     * @param args
     * @TODO: JavaDoc missing.
     */
    public static void main(String[] args) {

        String idMapInput = "/home/mmueller/data/sigpep/collab_waters/heart_and_lysosomal_sp_acc_to_ensembl_gene_id.txt";
        String observedFragmentsInput = "/home/mmueller/data/sigpep/collab_waters/ApoHuman.tsv";
        String transitionsInput = "/home/mmueller/data/sigpep/collab_waters/transitions/results/all_results_unmapped.tsv";
        String transitionsFilteredOutput = "/home/mmueller/data/sigpep/collab_waters/transitions/results/all_results_mod_filtered_frag_filtered.tsv";

        Map<String, String> spAccToEnsemblId = new HashMap<String, String>();
        Map<String, String> ensemblIdToSpAcc = new HashMap<String, String>();

        try {

            InputStream isIdMap = new FileInputStream(idMapInput);
            DelimitedTableReader dtrIdMap = new DelimitedTableReader(isIdMap, "\t");
            for (Iterator<String[]> rows = dtrIdMap.read(); rows.hasNext(); ) {

                String[] row = rows.next();

                String spAcc = row[0];
                String ensemblId = row[1];
                if (ensemblId.equals("NULL")) {
                    ensemblId = null;
                }

                spAccToEnsemblId.put(spAcc, ensemblId);

                if (ensemblId != null) {
                    ensemblIdToSpAcc.put(ensemblId, spAcc);
                }
            }

            isIdMap.close();

            System.out.println("spAccToEnsemblId.size() = " + spAccToEnsemblId.size());

            Map<String, Map<String, Set<String>>> acc2Pept2Ion = new HashMap<String, Map<String, Set<String>>>();

            Set<String> accs = new HashSet<String>();

            InputStream isObservedFragments = new FileInputStream(observedFragmentsInput);
            DelimitedTableReader dtrObservedFragments = new DelimitedTableReader(isObservedFragments, "\t");
            for (Iterator<String[]> rows = dtrObservedFragments.read(); rows.hasNext(); ) {

                String[] row = rows.next();
                if (row.length > 1) {

                    String spAcc = row[1];
                    String peptide = row[4];
                    String ionType = row[18];
                    String fragmentLength = row[19];
                    String fragmentLoss = row[20];

                    accs.add(spAcc);

                    if (ionType.equals("y") && fragmentLoss.equals("NULL")) {

//                        System.out.println(spAcc + "\t" +
//                                peptide + "\t" +
//                                ionType + "\t" +
//                                fragmentLength + "\t" +
//                                fragmentLoss);

                        if (!acc2Pept2Ion.containsKey(spAcc)) {
                            acc2Pept2Ion.put(spAcc, new HashMap<String, Set<String>>());
                        }

                        if (!acc2Pept2Ion.get(spAcc).containsKey(peptide)) {
                            acc2Pept2Ion.get(spAcc).put(peptide, new HashSet<String>());
                        }

                        String fragment = ionType.toUpperCase() + "" + fragmentLength;
                        acc2Pept2Ion.get(spAcc).get(peptide).add(fragment);
                    }
                }
            }

            System.out.println("acc2Pept2Ion.size() = " + acc2Pept2Ion.size());
            System.out.println("accs.size() = " + accs.size());

            Modification cyscarbamidmeth = ModificationFactory.createPostTranslationalModification("cyscarbamidmeth");
            double cyscarbamidmethMassDelta = cyscarbamidmeth.getMassDifference();


            InputStream isTransitions = new FileInputStream(transitionsInput);
            DelimitedTableReader dtrTransitions = new DelimitedTableReader(isTransitions, "\t");
            OutputStream os = new FileOutputStream(transitionsFilteredOutput);
            //OutputStream os = System.out;
            DelimitedTableWriter dtwTransitionsFiltered = new DelimitedTableWriter(os, "\t", false);
            for (Iterator<String[]> rows = dtrTransitions.read(); rows.hasNext(); ) {

                // ENSG00000074370
                // SQMAAVEPER
                // true
                // 10937
                // 1132.5183
                // 2
                // 0.9968
                // 567.267
                // Y7,Y5,Y8,Y9
                // 771.4001,629.3259,918.4355,1046.4941
                // 0.9
                String[] row = rows.next();
                String ensemblId = row[0];
                String peptide = row[1];
                String modified = row[2];
                String background = row[3];
                String mass = row[4];
                String charge = row[5];
                String pCharge = row[6];
                String mz = row[7];
                String combination = row[8];
                Set<String> combinationSet = new HashSet<String>();
                for (String frag : combination.split(",")) {
                    combinationSet.add(frag);
                }
                String mzFragment = row[9];
                String score = row[10];

                Pattern pMet = Pattern.compile("M");
                Pattern pCys = Pattern.compile("C");

                Matcher mMet = pMet.matcher(peptide);
                Matcher mCys = pCys.matcher(peptide);

                int countMet = 0;
                int countCys = 0;
                while (mMet.find()) {
                    countMet++;
                }

                while (mCys.find()) {
                    countCys++;
                }

                boolean containsMet = (countMet != 0);
                boolean containsCys = (countCys != 0);

                Peptide pep = PeptideFactory.createPeptide(peptide);
                double calculatedMass = pep.getPrecursorIon().getNeutralMassPeptide();

                boolean write = false;

                if (ensemblIdToSpAcc.containsKey(ensemblId)) {

                    //get sp accession
                    String spAcc = ensemblIdToSpAcc.get(ensemblId);

                    if (acc2Pept2Ion.containsKey(spAcc)) {

                        //get peptides
                        if (acc2Pept2Ion.get(spAcc).containsKey(peptide)) {

                            //get observed fragments
                            Set<String> observableIons = acc2Pept2Ion.get(spAcc).get(peptide);

                            //if the signature combination consists of
                            //observable fragments...
                            if (observableIons.containsAll(combinationSet)) {

                                //filter for non-methionine oxidised peptides
                                //...if the peptide contains met by not cys
                                if (containsMet && !containsCys) {

                                    //...only write if the peptide is not modified
                                    if (modified.equals("false")) {

                                        write = true;

                                    }

                                    //...if the peptide contains met and cys

                                } else if (containsMet && containsCys) {

                                    //...only write if the peptide mass
                                    //equals the mass of cyscarbamidmeth modified
                                    //peptide
                                    double modMass = SigPepUtil.round(calculatedMass + countCys * cyscarbamidmethMassDelta, 4);
                                    if (modMass == new Double(mass)) {

                                        //System.out.println(Arrays.toString(row));
                                        write = true;

                                    }

                                } else {
                                    write = true;
                                }

                                if (write) {

                                    dtwTransitionsFiltered.writeRow(spAcc,
                                            ensemblId,
                                            peptide,
                                            modified,
                                            background,
                                            mass,
                                            charge,
                                            pCharge,
                                            mz,
                                            combination,
                                            mzFragment,
                                            score);
                                    os.flush();
                                }
                            }
                        }
                    }
                }
            }

            os.close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
