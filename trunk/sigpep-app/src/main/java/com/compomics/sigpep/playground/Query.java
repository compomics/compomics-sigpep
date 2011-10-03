package com.compomics.sigpep.playground;

import com.compomics.sigpep.analysis.query.ProteomeCoverage;
import com.compomics.dbtools.DatabaseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @TODO: JavaDoc missing.
 * @TODO: all code in this class is commented out...
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 30-Nov-2007<br/>
 * Time: 13:38:33<br/>
 */
public class Query {

//    public static void main(String[] args) {
//
//        String barcodesInput = "/home/mmueller/data/sigpep/barcodes_9606_zmin2_zmax2_acc1_argc,lysc,v8de.tab";
//
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(barcodesInput));
//
//            Set<Integer> peptideIds = new HashSet<Integer>();
//
//            String line;
//            while ((line = br.readLine()) != null) {
//                try{
//
//                String[] columns = line.split("\t");
//                Integer peptideId = new Integer(columns[0]);
//                Integer transitionCount = new Integer(columns[6]);
//
////                System.out.println(columns.length);
//
//                if(transitionCount > 0){
//
//                    peptideIds.add(peptideId);
//                }
//
//                } catch (NumberFormatException e){
//                    System.out.println(e.getMessage());
//
//                }
//            }
//
//            ProteomeCoverage pc = new ProteomeCoverage("mmueller",
//                    "".toCharArray(), 9606, "/home/mmueller/data/sigpep");
//
//            pc.reportSignaturePeptideProteomeCoverageSummary();
//            pc.reportPeptideSetProteomeCoverageSummary("argc,lysc,v8de", peptideIds);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (DatabaseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//
//
//    }

}
