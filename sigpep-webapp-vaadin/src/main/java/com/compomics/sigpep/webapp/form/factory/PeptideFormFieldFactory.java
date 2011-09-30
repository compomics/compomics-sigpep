package com.compomics.sigpep.webapp.form.factory;

import com.compomics.sigpep.analysis.SignatureTransitionFinderType;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.component.FormHelp;
import com.compomics.sigpep.webapp.configuration.PropertiesConfigurationHolder;
import com.vaadin.data.Item;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 20/09/11
 * Time: 9:37
 * To change this template use File | Settings | File Templates.
 */
public class PeptideFormFieldFactory implements FormFieldFactory {
    private static Logger log = Logger.getLogger(PeptideFormFieldFactory.class);

    private FormHelp iFormHelp;

    private TextField iSpeciesTextField;
    private TextField iMassTextField;
    private TextField iMinimumCombinationSizeTextField;
    private TextField iMaximumCombinationSizeTextField;
    private Select iSignatureTransitionFinderTypeSelect;
    private TextField iProteaseTextField;
    private TextField iPeptideSequenceTextField;
    private boolean iVisible = Boolean.FALSE;

    public PeptideFormFieldFactory(MyVaadinApplication aApplication) {
        iFormHelp = aApplication.getFormHelp();

        //species field
        iSpeciesTextField = new TextField("Species");
        iSpeciesTextField.setEnabled(Boolean.FALSE);

        //mass field
        iMassTextField = new TextField("Mass accuracy");
        iMassTextField.setRequired(Boolean.TRUE);
        iMassTextField.addValidator(new DoubleValidator(PropertiesConfigurationHolder.getInstance().getString("form_validation.double")));
        iFormHelp.addHelpForComponent(iMassTextField, PropertiesConfigurationHolder.getInstance().getString("form_help.mass_accuracy"));

        //min combination size field
        iMinimumCombinationSizeTextField = new TextField("Minimum combination size");
        iMinimumCombinationSizeTextField.setRequired(Boolean.TRUE);
        iMinimumCombinationSizeTextField.addValidator(new IntegerValidator(PropertiesConfigurationHolder.getInstance().getString("form_validation.integer")));
        iFormHelp.addHelpForComponent(iMinimumCombinationSizeTextField, PropertiesConfigurationHolder.getInstance().getString("form_help.minimum_combination_size"));

        //max combination size field
        iMaximumCombinationSizeTextField = new TextField("Maximum combination size");
        iMaximumCombinationSizeTextField.setRequired(Boolean.TRUE);
        iMaximumCombinationSizeTextField.addValidator(new IntegerValidator(PropertiesConfigurationHolder.getInstance().getString("form_validation.integer")));
        iFormHelp.addHelpForComponent(iMaximumCombinationSizeTextField, PropertiesConfigurationHolder.getInstance().getString("form_help.maximum_combination_size"));

        //signature transition field
        iSignatureTransitionFinderTypeSelect = new Select("Signature transition finder");
        iSignatureTransitionFinderTypeSelect.setRequired(Boolean.TRUE);
        iSignatureTransitionFinderTypeSelect.setNullSelectionAllowed(Boolean.FALSE);
        for (SignatureTransitionFinderType sig : SignatureTransitionFinderType.values()) {
            iSignatureTransitionFinderTypeSelect.addItem(sig);
        }
        iFormHelp.addHelpForComponent(iSignatureTransitionFinderTypeSelect, PropertiesConfigurationHolder.getInstance().getString("form_help.signature_transition_finder"));

        //protease field
        iProteaseTextField = new TextField("Protease");
        iProteaseTextField.setEnabled(Boolean.FALSE);

        //peptide sequence field
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