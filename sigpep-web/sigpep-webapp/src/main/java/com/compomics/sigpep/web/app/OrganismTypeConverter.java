package com.compomics.sigpep.web.app;

import com.compomics.sigpep.ApplicationLocator;
import com.compomics.sigpep.SigPepApplication;
import com.compomics.sigpep.model.Organism;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 22-Jul-2008<br/>
 * Time: 15:12:20<br/>
 */
public class OrganismTypeConverter implements Converter {

    private static SigPepApplication sigPepApplication = ApplicationLocator.getInstance().getApplication();

    public Organism getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) throws ConverterException {

        Organism retVal;

        if (value == null) {
            throw new IllegalArgumentException("Value of taxon ID must not be null.");
        }

        if (value.length() == 0) {
            throw new IllegalArgumentException("Value of taxon ID must not have length 0.");
        }

        Integer taxonId = 0;
        try {
            taxonId = new Integer(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unable to convert value " + value + " to taxon ID.");

        }

        retVal = sigPepApplication.getSigPepSessionFactory().getOrganism(taxonId);

        if(retVal == null){
            throw new IllegalArgumentException("Taxon ID " + taxonId + " not mapped to Organism.");
        }

        return retVal;

    }

    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) throws ConverterException {

        if(!(o instanceof Organism)){
            throw new IllegalArgumentException("Object to convert must be instance of Organism.");
        }

        Organism organism = (Organism)o;

        return "" + organism.getTaxonId() ;
    
    }

    
}
