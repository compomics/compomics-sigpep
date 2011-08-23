package com.compomics.sigpep.webapp.factory;

import com.compomics.sigpep.SigPepSession;
import com.compomics.sigpep.analysis.SignatureTransitionFinderType;
import com.compomics.sigpep.model.Organism;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.form.SigPepForm;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 19/08/11
 * Time: 9:41
 * To change this template use File | Settings | File Templates.
 */
public class SigPepFormFieldFactory implements FormFieldFactory {
    private static Logger log = Logger.getLogger(SigPepFormFieldFactory.class);

    private Select iSpeciesSelect = new Select("Species");
    private TextField iMassTextField = new TextField("Mass accuracy");
    private TextField iMinimumCombinationSizeTextField = new TextField("Minimum combination size");
    private TextField iMaximumCombinationSizeTextField = new TextField("Maximum combination size");
    private Select iSignatureTransitionFinderTypeSelect = new Select("Signature transition finder");
    private Select iProteaseSelect = new Select("Protease");
    private TextField iProteinAccessionTextField = new TextField("Protein accession");
    private boolean iVisible = Boolean.FALSE;
    private MyVaadinApplication iMyVaadinApplication;

    public SigPepFormFieldFactory(MyVaadinApplication aMyVaadinApplication) {
        iMyVaadinApplication = aMyVaadinApplication;

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
                    log.info("Creating sigpep session.");
                    iMyVaadinApplication.setSigPepSession(MyVaadinApplication.getSigPepSessionFactory().createSigPepSession(lOrganism));
                    fillProteaseSelect();
                }
            }
        });

        setFormComponentsVisible(iVisible);
    }

    public Field createField(Item aItem, Object o, Component aComponent) {
        String pid = (String) o;

        if ("species".equals(pid)) {
            iSpeciesSelect.setRequired(Boolean.TRUE);
            iSpeciesSelect.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
            iSpeciesSelect.setItemCaptionPropertyId("scientificName");
            iSpeciesSelect.setImmediate(Boolean.TRUE);
            iSpeciesSelect.setNullSelectionAllowed(Boolean.FALSE);
            BeanItemContainer<Organism> lOrganismBeanItemContainer = new BeanItemContainer<Organism>(Organism.class);
            lOrganismBeanItemContainer.addAll(getOrganisms());
            iSpeciesSelect.setContainerDataSource(lOrganismBeanItemContainer);
            return iSpeciesSelect;
        } else if ("massAccuracy".equals(pid)) {
            iMassTextField.setRequired(Boolean.TRUE);
            return iMassTextField;
        } else if ("minimumCombinationSize".equals(pid)) {
            iMinimumCombinationSizeTextField.setRequired(Boolean.TRUE);
            return iMinimumCombinationSizeTextField;
        } else if ("maximumCombinationSize".equals(pid)) {
            iMaximumCombinationSizeTextField.setRequired(Boolean.TRUE);
            return iMaximumCombinationSizeTextField;
        } else if ("signatureTransitionFinderType".equals(pid)) {
            iSignatureTransitionFinderTypeSelect.setRequired(Boolean.TRUE);
            iSignatureTransitionFinderTypeSelect.setNullSelectionAllowed(Boolean.FALSE);
            for (SignatureTransitionFinderType sig : SignatureTransitionFinderType.values()) {
                iSignatureTransitionFinderTypeSelect.addItem(sig);
            }
            return iSignatureTransitionFinderTypeSelect;
        } else if ("proteaseName".equals(pid)) {
            iProteaseSelect.setRequired(Boolean.TRUE);
            iProteaseSelect.setNullSelectionAllowed(Boolean.FALSE);
            iSpeciesSelect.setImmediate(Boolean.TRUE);
            return iProteaseSelect;
        } else if ("proteinAccession".equals(pid)) {
            iProteinAccessionTextField.setRequired(Boolean.TRUE);
            return iProteinAccessionTextField;
        }
        return null;
    }

    private void setFormComponentsVisible(boolean setVisible) {
        iMassTextField.setVisible(setVisible);
        iMinimumCombinationSizeTextField.setVisible(setVisible);
        iMaximumCombinationSizeTextField.setVisible(setVisible);
        iSignatureTransitionFinderTypeSelect.setVisible(setVisible);
        iProteaseSelect.setVisible(setVisible);
        iProteinAccessionTextField.setVisible(setVisible);
    }

    private void fillProteaseSelect() {
        if (iProteaseSelect.size() != 0) {
            iProteaseSelect.removeAllItems();
        }
        for (String lProteaseName : iMyVaadinApplication.getSigPepSession().getSimpleQueryDao().getUsedProteaseNames()) {
            iProteaseSelect.addItem(lProteaseName);
        }
    }

    private Set<Organism> getOrganisms() {
        return MyVaadinApplication.getSigPepSessionFactory().getOrganisms();
    }

}