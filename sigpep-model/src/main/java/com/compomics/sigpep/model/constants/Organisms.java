package com.compomics.sigpep.model.constants;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.<br>
 * User: mmueller<br>
 * Date: 25-Sep-2007<br>
 * Time: 18:06:51<br>
 */
public class Organisms {

    private static Organisms ourInstance;
    private Configuration speciesConfig;
    private static Logger logger = Logger.getLogger(Organisms.class);
    private Map<String, Integer> speciesName2NcbiTaxonId = new TreeMap<String, Integer>();
    private Map<Integer, String> ncbiTaxonId2SpeciesName = new TreeMap<Integer, String>();

    public static Organisms getInstance() {
        if (ourInstance == null) {
            try {
                ourInstance = new Organisms("organisms.properties");
            } catch (ConfigurationException e) {
                logger.error(e);
            }
        }
        return ourInstance;
    }

    private Organisms(String propertiesFile) throws ConfigurationException {       
        this.speciesConfig = new PropertiesConfiguration(propertiesFile);
        this.populateMaps();
    }

    private void populateMaps() {

        speciesName2NcbiTaxonId = new TreeMap<String, Integer>();
        ncbiTaxonId2SpeciesName = new TreeMap<Integer, String>();

        for (Iterator<String> keys = speciesConfig.getKeys(); keys.hasNext();) {

            String key = keys.next();
            String speciesName = key.replace(".", " ");
            int ncbiTaxonId = speciesConfig.getInt(key);
            speciesName2NcbiTaxonId.put(speciesName, ncbiTaxonId);
            ncbiTaxonId2SpeciesName.put(ncbiTaxonId, speciesName);

        }
    }

    public int getNcbiTaxonId(String speciesName) {

        if (speciesName2NcbiTaxonId.containsKey(speciesName))
            return speciesName2NcbiTaxonId.get(speciesName);
        else return 0;

    }

    public String getSpeciesName(int ncbiTaxonId) {

        if (ncbiTaxonId2SpeciesName.containsKey(ncbiTaxonId))
            return ncbiTaxonId2SpeciesName.get(ncbiTaxonId);
        else return "";

    }

    public Set<String> getSpeciesNames() {
        return speciesName2NcbiTaxonId.keySet();
    }

    public Set<Integer> getNcbiTaxonIds() {
        return ncbiTaxonId2SpeciesName.keySet();
    }

    public boolean contains(String speciesName) {
        return speciesName2NcbiTaxonId.containsKey(speciesName);
    }

    public boolean contains(int ncbiTaxonId) {
        return ncbiTaxonId2SpeciesName.containsKey(ncbiTaxonId);
    }

}
