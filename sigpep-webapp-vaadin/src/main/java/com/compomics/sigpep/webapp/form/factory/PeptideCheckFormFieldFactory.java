package com.compomics.sigpep.webapp.form.factory;

import com.compomics.sigpep.model.Organism;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.component.FormHelp;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 20/09/11
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
public class PeptideCheckFormFieldFactory implements FormFieldFactory {
    private static Logger log = Logger.getLogger(PeptideCheckFormFieldFactory.class);

    private MyVaadinApplication iApplication;
    private FormHelp iFormHelp;

    private Select iSpeciesSelect;
    private Select iProteaseSelect;
    private TextField iPeptideSequenceTextField;
    private boolean iVisible = Boolean.FALSE;

    public PeptideCheckFormFieldFactory(MyVaadinApplication aApplication) {
        iApplication = aApplication;
        iFormHelp = iApplication.getFormHelp();

        //species field
        iSpeciesSelect = new Select("Species");
        iSpeciesSelect.setRequired(Boolean.TRUE);
        iSpeciesSelect.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        iSpeciesSelect.setItemCaptionPropertyId("scientificName");
        iSpeciesSelect.setImmediate(Boolean.TRUE);
        iSpeciesSelect.setNullSelectionAllowed(Boolean.FALSE);
        BeanItemContainer<Organism> lOrganismBeanItemContainer = new BeanItemContainer<Organism>(Organism.class);
        lOrganismBeanItemContainer.addAll(getOrganisms());
        iSpeciesSelect.setContainerDataSource(lOrganismBeanItemContainer);
        iFormHelp.addHelpForComponent(iSpeciesSelect, iFormHelp.getFormHelpProperties().getProperty("form_help.species"));

        //protease field
        iProteaseSelect = new Select("Protease");
        iProteaseSelect.setRequired(Boolean.TRUE);
        iProteaseSelect.setNullSelectionAllowed(Boolean.FALSE);
        iProteaseSelect.setImmediate(Boolean.TRUE);
        iFormHelp.addHelpForComponent(iProteaseSelect, iFormHelp.getFormHelpProperties().getProperty("form_help.protease"));

        //peptide field
        iPeptideSequenceTextField = new TextField("Peptide sequence");
        iPeptideSequenceTextField.setRequired(Boolean.TRUE);
        iPeptideSequenceTextField.addValidator(new RegexpValidator("[A-Z]+", "Incorrect peptide sequence format"));
        iFormHelp.addHelpForComponent(iPeptideSequenceTextField, iFormHelp.getFormHelpProperties().getProperty("form_help.peptide_sequence"));

        iSpeciesSelect.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent aValueChangeEvent) {
                Organism lOrganism = (Organism) iSpeciesSelect.getValue();
                if (lOrganism == null) {
                    iVisible = Boolean.FALSE;
                    setFormComponentsVisible(iVisible);
                    return;
                } else {
                    if (!iVisible) {
                        iVisible = Boolean.TRUE;
                        setFormComponentsVisible(iVisible);
                    }
                    if (iApplication.getSigPepSession() == null || !iApplication.getSigPepSession().getOrganism().getScientificName().equals(lOrganism.getScientificName())) {
                        log.info("Creating sigpep session for organism " + lOrganism.getScientificName());
                        iApplication.setSigPepSession(iApplication.getSigPepSessionFactory().createSigPepSession(lOrganism));
                    }
                    fillProteaseSelect();
                }
            }
        });

        setFormComponentsVisible(iVisible);
    }

    public Field createField(Item aItem, Object o, Component aComponent) {
        String pid = (String) o;

        if ("species".equals(pid)) {
            return iSpeciesSelect;
        } else if ("proteaseName".equals(pid)) {
            return iProteaseSelect;
        } else if ("peptideSequence".equals(pid)) {
            return iPeptideSequenceTextField;
        }
        return null;
    }

    private void setFormComponentsVisible(boolean setVisible) {
        iProteaseSelect.setVisible(setVisible);
        iPeptideSequenceTextField.setVisible(setVisible);
    }

    private void fillProteaseSelect() {
        if (iProteaseSelect.size() != 0) {
            iProteaseSelect.removeAllItems();
        }
        for (String lProteaseName : iApplication.getSigPepSession().getSimpleQueryDao().getUsedProteaseNames()) {
            iProteaseSelect.addItem(lProteaseName);
        }
    }

    private Set<Organism> getOrganisms() {
        return iApplication.getSigPepSessionFactory().getOrganisms();
    }

}

