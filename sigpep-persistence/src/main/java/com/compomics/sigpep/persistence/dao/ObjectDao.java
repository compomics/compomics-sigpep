package com.compomics.sigpep.persistence.dao;

import com.compomics.sigpep.model.*;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 29-May-2008<br/>
 * Time: 17:51:53<br/>
 */
public interface ObjectDao {

    //////////////////
    //organism queries
    //////////////////

    public Organism getOrganism();

    //////////////////
    //protease queries
    //////////////////

    public Set<Protease> getAllProteases();

    public Protease getProteaseByShortName(String shortName);

    public Set<Protease> getProteaseSetByShortName(Set<String> shortName);

    //////////////
    //gene queries
    //////////////

    public Set<Gene> getAllGenes();

    public Gene getGeneByAccession(String accession);

    public Set<Gene> getGeneSetByAccession(Set<String> accession);

    /////////////////
    //protein queries
    /////////////////

    public Set<Protein> getAllProteins();

    public Protein getProteinByAccession(String accession);

    public Set<Protein> getProteinSetByAccession(Set<String> accession);

    //////////////////
    //sequence queries
    //////////////////

    public Set<ProteinSequence> getAllProteinSequences();

    /////////////////
    //petpide queries
    /////////////////

    public Peptide getPeptideById(String peptideSequence);

    public PeptideFeature getPeptideFeatureBySequence(String peptideSequence);




}
