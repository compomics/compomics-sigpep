package com.compomics.sigpep.web.app;

import com.compomics.sigpep.ApplicationLocator;
import com.compomics.sigpep.SigPepApplication;
import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.SigPepSessionFactory;
import com.compomics.sigpep.model.Organism;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 01-Apr-2008<br/>
 * Time: 13:44:48<br/>
 */
public class SigPepWebApplication {

    private static SigPepApplication sigPepApplication = ApplicationLocator.getInstance().getApplication();
    private static SigPepSessionFactory sigPepsessionFactory = (SigPepSessionFactory) sigPepApplication.getSigPepSessionFactory();
    private static Map<Integer, Organism> availableOrganisms;

    public SigPepSession createSigPepSession(Organism organism) {
        return sigPepsessionFactory.createSigPepSession(organism);
    }

    public void setAvailableOrganisms(Set<Organism> organisms) {
        availableOrganisms = new HashMap<Integer, Organism>();
        for (Organism organism : organisms) {
            availableOrganisms.put(organism.getTaxonId(), organism);
        }
    }

    public Collection<Organism> getAvailableOrganisms() {
        if (availableOrganisms == null) {
            setAvailableOrganisms(sigPepsessionFactory.getOrganisms());
        }

        return availableOrganisms.values();
    }

    public Organism getOrganismByTaxonId(int taxonId) {

        if (availableOrganisms == null) {
            setAvailableOrganisms(sigPepsessionFactory.getOrganisms());
        }

        Organism retVal = null;
        if (availableOrganisms.containsKey(taxonId)) {
            retVal = availableOrganisms.get(taxonId);
        }

        return retVal;
    }

}
