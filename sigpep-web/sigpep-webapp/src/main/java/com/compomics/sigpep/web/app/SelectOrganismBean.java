package com.compomics.sigpep.web.app;

import com.compomics.sigpep.model.Organism;

import javax.faces.model.SelectItem;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 02-Jun-2008<br/>
 * Time: 14:06:17<br/>
 */
public class SelectOrganismBean {

    private SigPepWebApplication sigPepWebApplication;

    public Set<SelectItem> getAvailableOrganisms() {

        Set<SelectItem> retVal = new LinkedHashSet<SelectItem>();
        for (Organism organism : sigPepWebApplication.getAvailableOrganisms()) {
            retVal.add(new SelectItem("" + organism.getTaxonId(), organism.getScientificName()));
        }
        return retVal;

    }

    public String send() {
        return "success";
    }

    public SigPepWebApplication getSigPepWebApplication() {
        return sigPepWebApplication;
    }

    public void setSigPepWebApplication(SigPepWebApplication sigPepWebApplication) {
        this.sigPepWebApplication = sigPepWebApplication;
    }



}
