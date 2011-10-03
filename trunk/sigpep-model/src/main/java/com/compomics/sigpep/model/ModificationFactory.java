package com.compomics.sigpep.model;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import com.compomics.sigpep.model.constants.MonoElementMasses;
import com.compomics.sigpep.model.impl.ModificationImpl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @TODO: JavaDoc missing.
 * <p/>
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 12-Feb-2008<br/>
 * Time: 16:24:04<br/>
 */
public abstract class ModificationFactory {

    /**
     * the logger
     */
    private static Logger logger = Logger.getLogger(ModificationFactory.class);

    /**
     * to access the mono element masses
     */
    private static final MonoElementMasses elementMasses = MonoElementMasses.getInstance();

    /**
     * the PTM properties file name
     */
    private static final String PROPERTIES_FILE = "ptm.properties";

    /**
     * to access to the PTM properties file
     */
    private static PropertiesConfiguration configuration;

    private static Set<String> modificationKeys;

    static {

        //open configuration file
        try {

            configuration = new PropertiesConfiguration(PROPERTIES_FILE);


        } catch (ConfigurationException e) {
            throw new RuntimeException("Exception while accessing post translational modification properties in file + " + PROPERTIES_FILE + ".", e);
        }

        //read PTM keys
        try {

            modificationKeys = new TreeSet<String>();
            for (Iterator<String> propertyKeys = configuration.getKeys(); propertyKeys.hasNext(); ) {

                String key = propertyKeys.next();
                String modificationKey = key.split("\\.")[1];
                modificationKeys.add(modificationKey);

            }
        } catch (Exception e) {
            throw new RuntimeException("Exception while reading post translational modification properties in file + " + PROPERTIES_FILE + ".", e);
        }
    }

    /**
     * the PTM cache
     */
    private static final HashMap<String, Modification> ptmCache = new HashMap<String, Modification>();

    /**
     * Returns a post translational modification for the specified residue.
     *
     * @param key the PTM short key
     * @return the post translation modification object
     * @throws IllegalArgumentException if there is no PTM defined in the ptm.properties file with the specified key
     * @throws RuntimeException         if an exception occurs while loading the properties file containing the PTM properties
     */
    public static Modification createPostTranslationalModification(String key) {

        String ptmKey = key;

        if (ptmCache.containsKey(ptmKey)) {
            return ptmCache.get(ptmKey);
        }

        String residue = configuration.getString("ptm." + key + ".residue");

        if (residue == null) {
            throw new IllegalArgumentException("No modification identified by key '" + key + "'.");
        }

        //check if positional
        ModificationPosition position = ModificationPosition.NON_POSITIONAL;
        String positionString = "";
        boolean positional = false;

        if (residue.split(" ").length == 2) {
            positional = true;
            positionString = residue.split(" ")[1];
            residue = residue.split(" ")[0];
        }
        String formula = configuration.getString("ptm." + key + ".formula");
        String description = configuration.getString("ptm." + key + ".description");
        boolean isStable = configuration.getBoolean("ptm." + key + ".stable");
        boolean isStatic = configuration.getBoolean("ptm." + key + ".static");
        double massDelta = calculateMassDelta(formula);

        if (positional) {

            if (positionString.equalsIgnoreCase("N-terminal")) {
                position = ModificationPosition.N_TERMINAL;
            } else if (positionString.equalsIgnoreCase("C-terminal")) {
                position = ModificationPosition.C_TERMINAL;
            }

        }

        Modification ptm = new ModificationImpl(key, description, residue, formula, massDelta, isStable, isStatic, position);

        ptmCache.put(ptmKey, ptm);

        return ptm;
    }

    /**
     * @param keys
     * @return
     * @TODO: JavaDoc missing.
     */
    public static Set<Modification> createPostTranslationalModifications(String... keys) {

        Set<String> keySet = new HashSet<String>();
        Collections.addAll(keySet, keys);
        return createPostTranslationalModifications(keySet);
    }

    /**
     * Creates an ordered set of modifications. The order of the set is the same
     * as the order of the modification keys in the set passed as an argument.
     *
     * @param keys the modification keys
     * @return an ordered set of modifications
     */
    public static Set<Modification> createPostTranslationalModifications(Set<String> keys) {

        Set<Modification> retVal = new LinkedHashSet<Modification>();

        for (String name : keys) {
            try {
                Modification ptm = createPostTranslationalModification(name);
                retVal.add(ptm);
            } catch (IllegalArgumentException e) {
                //don't do anything
            }
        }

        return retVal;
    }

    /**
     * Returns all PTMs defenied in the ptm.properties file.
     *
     * @return an ordered set of modifications
     */
    public static Set<Modification> createPostTranslationalModifications() {

        Set<Modification> retVal = new LinkedHashSet<Modification>();

        for (String key : modificationKeys) {
            try {
                Modification ptm = createPostTranslationalModification(key);
                retVal.add(ptm);
            } catch (IllegalArgumentException e) {
                //don't do anything
            }
        }

        return retVal;
    }

    /**
     * Returns the monoisotopic mass for a chemical formula.
     *
     * @param formula the formula
     * @return the monoisotopic mass
     */
    private static double calculateMassDelta(String formula) {
        double retVal = 0;

        Pattern p = Pattern.compile("([A-Za-z]{1}\\(-?\\d{1}\\))");
        Matcher m = p.matcher(formula);

        while (m.find()) {

            String group = m.group();
            String element = "" + group.split("\\(")[0];
            int count = new Integer("" + group.split("\\(")[1].replace(")", ""));

            double elementMass = elementMasses.getDouble(element);
            retVal += elementMass * count;

        }

        return retVal;
    }
}
