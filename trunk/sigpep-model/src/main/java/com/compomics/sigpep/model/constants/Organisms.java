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
 * @TODO: JavaDoc missing
 * <p/>
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

    /**
     * @return
     * @TODO: JavaDoc missing
     */
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

    /**
     * @param propertiesFile
     * @throws ConfigurationException
     * @TODO: JavaDoc missing
     */
    private Organisms(String propertiesFile) throws ConfigurationException {
        this.speciesConfig = new PropertiesConfiguration(propertiesFile);
        this.populateMaps();
    }

    /**
     * @TODO: JavaDoc missing
     */
    private void populateMaps() {

        speciesName2NcbiTaxonId = new TreeMap<String, Integer>();
        ncbiTaxonId2SpeciesName = new TreeMap<Integer, String>();

        for (Iterator<String> keys = speciesConfig.getKeys(); keys.hasNext(); ) {

            String key = keys.next();
            String speciesName = key.replace(".", " ");
            int ncbiTaxonId = speciesConfig.getInt(key);
            speciesName2NcbiTaxonId.put(speciesName, ncbiTaxonId);
            ncbiTaxonId2SpeciesName.put(ncbiTaxonId, speciesName);

        }
    }

    /**
     * @param speciesName
     * @return
     * @TODO: JavaDoc missing
     */
    public int getNcbiTaxonId(String speciesName) {

        if (speciesName2NcbiTaxonId.containsKey(speciesName))
            return speciesName2NcbiTaxonId.get(speciesName);
        else return 0;

    }

    /**
     * @param ncbiTaxonId
     * @return
     * @TODO: JavaDoc missing
     */
    public String getSpeciesName(int ncbiTaxonId) {

        if (ncbiTaxonId2SpeciesName.containsKey(ncbiTaxonId))
            return ncbiTaxonId2SpeciesName.get(ncbiTaxonId);
        else return "";
    }

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public Set<String> getSpeciesNames() {
        return speciesName2NcbiTaxonId.keySet();
    }

    /**
     * @return
     * @TODO: JavaDoc missing
     */
    public Set<Integer> getNcbiTaxonIds() {
        return ncbiTaxonId2SpeciesName.keySet();
    }

    /**
     * @param speciesName
     * @return
     * @TODO: JavaDoc missing
     */
    public boolean contains(String speciesName) {
        return speciesName2NcbiTaxonId.containsKey(speciesName);
    }

    /**
     * @param ncbiTaxonId
     * @return
     * @TODO: JavaDoc missing
     */
    public boolean contains(int ncbiTaxonId) {
        return ncbiTaxonId2SpeciesName.containsKey(ncbiTaxonId);
    }
}
