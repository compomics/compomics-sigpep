package com.compomics.sigpep.webapp.factory;

import com.compomics.sigpep.PeptideGenerator;
import com.compomics.sigpep.SigPepQueryService;
import com.compomics.sigpep.analysis.SignatureTransitionFinderType;
import com.compomics.sigpep.model.Organism;
import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.model.Protease;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.bean.ProteinFormBean;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.event.MouseEvents;
import com.vaadin.ui.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import org.apache.log4j.Logger;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 20/09/11
 * Time: 9:37
 * To change this template use File | Settings | File Templates.
 */
public class PeptideFormFieldFactory implements FormFieldFactory {
    private static Logger log = Logger.getLogger(PeptideFormFieldFactory.class);

    private TextField iSpeciesTextField;
    private TextField iMassTextField;
    private TextField iMinimumCombinationSizeTextField;
    private TextField iMaximumCombinationSizeTextField;
    private Select iSignatureTransitionFinderTypeSelect;
    private TextField iProteaseTextField;
    private TextField iPeptideSequenceTextField;
    private boolean iVisible = Boolean.FALSE;

    public PeptideFormFieldFactory() {
        //species field
        iSpeciesTextField = new TextField("Species");
        iSpeciesTextField.setEnabled(Boolean.FALSE);

        //mass field
        iMassTextField = new TextField("Mass accuracy");
        iMassTextField.setRequired(Boolean.TRUE);
        iMassTextField.addValidator(new DoubleValidator("Value must be a double"));

        //min combination size field
        iMinimumCombinationSizeTextField = new TextField("Minimum combination size");
        iMinimumCombinationSizeTextField.setRequired(Boolean.TRUE);
        iMinimumCombinationSizeTextField.addValidator(new IntegerValidator("Value must be an integer"));

        //max combination size field
        iMaximumCombinationSizeTextField = new TextField("Maximum combination size");
        iMaximumCombinationSizeTextField.setRequired(Boolean.TRUE);
        iMaximumCombinationSizeTextField.addValidator(new IntegerValidator("Value must be an integer"));

        //signature transition field
        iSignatureTransitionFinderTypeSelect = new Select("Signature transition finder");
        iSignatureTransitionFinderTypeSelect.setRequired(Boolean.TRUE);
        iSignatureTransitionFinderTypeSelect.setNullSelectionAllowed(Boolean.FALSE);
        for (SignatureTransitionFinderType sig : SignatureTransitionFinderType.values()) {
            iSignatureTransitionFinderTypeSelect.addItem(sig);
        }

        //protease field
        iProteaseTextField = new TextField("Protease");
        iProteaseTextField.setEnabled(Boolean.FALSE);

        //protein accession field
        iPeptideSequenceTextField = new TextField("Peptide sequence");
        iPeptideSequenceTextField.setEnabled(Boolean.FALSE);

    }

    public Field createField(Item aItem, Object o, Component aComponent) {
        String pid = (String) o;

        if ("scientificName".equals(pid)) {
            return iSpeciesTextField;
        } else if ("massAccuracy".equals(pid)) {
            return iMassTextField;
        } else if ("minimumCombinationSize".equals(pid)) {
            return iMinimumCombinationSizeTextField;
        } else if ("maximumCombinationSize".equals(pid)) {
            return iMaximumCombinationSizeTextField;
        } else if ("signatureTransitionFinderType".equals(pid)) {
            return iSignatureTransitionFinderTypeSelect;
        } else if ("proteaseName".equals(pid)) {
            return iProteaseTextField;
        } else if ("peptideSequence".equals(pid)) {
            return iPeptideSequenceTextField;
        }
        return null;

    }

}