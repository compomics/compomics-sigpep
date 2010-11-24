package com.compomics.sigpep.web.app;

import com.compomics.sigpep.SigPepQueryService;
import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.model.Organism;
import com.compomics.sigpep.model.PeptideFeature;
import com.compomics.sigpep.model.Protease;
import com.compomics.sigpep.model.Protein;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 22-Jul-2008<br/>
 * Time: 15:15:07<br/>
 */
public class SigPepSessionBean {

    public SigPepWebApplication sigPepWebApplication;
    public SigPepSession sigPepSession;
    public SigPepQueryService sigPepQueryService;

    public Organism organism;

    public String selectedOrganism;
    public String selectedLevel;
    public List<String> selectedProteases = new ArrayList<String>();
    public List<String> accessionFilter = new ArrayList<String>();

    public SigPepWebApplication getSigPepWebApplication() {
        return sigPepWebApplication;
    }

    public void setSigPepWebApplication(SigPepWebApplication sigPepWebApplication) {
        this.sigPepWebApplication = sigPepWebApplication;
    }

    public Set<Protease> getAvailableProteases(){
        return sigPepQueryService.getAllProteases();
    }

    public List<String> getSelectedProteases() {
        return selectedProteases;
    }

    public void setSelectedProteases(List<String> selectedProteases) {
        this.selectedProteases = selectedProteases;
    }

    public String getSelectedLevel() {
        return selectedLevel;
    }

    public void setSelectedLevel(String level) {
        this.selectedLevel = level;
    }

    public String getSelectedOrganism() {
        return selectedOrganism;
    }

    public void setSelectedOrganism(String selectedOrganism) {
        this.selectedOrganism = selectedOrganism;

        int taxonId = new Integer(selectedOrganism);
        Organism organism = this.sigPepWebApplication.getOrganismByTaxonId(taxonId);
        this.setOrganism(organism);

    }

    public List<String> getAccessionFilter() {
        return accessionFilter;
    }

    public void setAccessionFilter(List<String> accessionFilter) {
        this.accessionFilter = accessionFilter;
    }

    public Organism getOrganism() {
        return organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
        this.sigPepSession = sigPepWebApplication.createSigPepSession(organism);
        this.sigPepQueryService = sigPepSession.createSigPepQueryService();
    }

    public List<PeptideFeature> getSignaturePeptides(){

        Set<Protein> proteins = this.sigPepQueryService.getProteinSetByAccession(new HashSet<String>(accessionFilter));
        Set<Protease> proteases = this.sigPepQueryService.getProteaseSetByShortName(new HashSet<String>(this.getSelectedProteases()));
        Set<PeptideFeature> signaturePeptideFeatures = this.sigPepQueryService.getSignaturePeptidesForProteinSet(proteins, proteases);    

        List<PeptideFeature> retVal = new ArrayList<PeptideFeature>();
        retVal.addAll(signaturePeptideFeatures);
        return retVal;

    }

}
