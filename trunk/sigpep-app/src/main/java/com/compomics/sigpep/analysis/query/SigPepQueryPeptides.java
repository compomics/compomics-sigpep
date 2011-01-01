package com.compomics.sigpep.analysis.query;

import com.compomics.dbtools.DatabaseException;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.TreeMap;

/**
 * @TODO: JavaDoc missing.
 * @TODO: All code in this class is commented out...
 *
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 02-Nov-2007<br>
 * Time: 16:50:43<br>
 */
public class SigPepQueryPeptides  {

//    private static String SQL_SELECT_PROTEIN_COUNT_BY_PEPTIDE =
//            "SELECT p.peptide_id, COUNT(DISTINCT pro2seq.protein_id)\n" +
//            "  FROM peptide p,\n" +
//            "       protein2sequence pro2seq,\n" +
//            "       peptide2protease pep2prot,\n" +
//            "       protease prot\n" +
//            " WHERE p.peptide_id=pep2prot.peptide_id\n" +
//            "   AND pep2prot.protease_id=prot.protease_id\n" +
//            "   AND p.peptide_id\n" +
//            "   AND prot.name=?\n" +
//            "   AND p.peptide_id\n" +
//            "   AND p.sequence_id=pro2seq.sequence_id\n" +
//            "GROUP BY p.peptide_id";
//
//    public SigPepQueryPeptides(String userName, char[] password, int ncbiTaxonId, String outputDirectory) throws DatabaseException {
//        super(userName, password, ncbiTaxonId, outputDirectory);
//    }
//
//    public Map<Integer, Integer> reportProteinCountPerPeptide(String protease) throws SQLException {
//
//        Map<Integer, Integer> retVal = new TreeMap<Integer, Integer>();
//
//        PreparedStatement ps = getSigPepDatabase().getConnection().prepareStatement(SQL_SELECT_PROTEIN_COUNT_BY_PEPTIDE);
//        ps.setString(1, protease);
//        ResultSet rs = ps.executeQuery();
//
//        while(rs.next()){
//
//            int proteinCount = rs.getInt(2);
//            if(!retVal.containsKey(proteinCount))
//                retVal.put(proteinCount, 0);
//
//            int cases = retVal.get(proteinCount);
//            cases++;
//            retVal.put(proteinCount, cases);
//
//        }
//
//
//        for(Integer proteinCount : retVal.keySet()){
//
//            int cases = retVal.get(proteinCount);
//            System.out.println(proteinCount + "\t" + cases);
//
//        }
//
//        return retVal;
//
//
//    }
//
//    public static void main(String[] args) {
//        SigPepQueryPeptides query;
//        try {
//
//            query = new SigPepQueryPeptides(
//                    args[0],
//                    args[1].toCharArray(),
//                    Integer.parseInt(args[2]),
//                    args[3]);
//
//            query.reportProteinCountPerPeptide("tryp");
//
//        } catch (DatabaseException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (SQLException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }
}
