package com.compomics.sigpep.webapp.form.factory;

import com.compomics.sigpep.analysis.SignatureTransitionFinderType;
import com.compomics.sigpep.model.Organism;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.component.FormHelp;
import com.compomics.sigpep.webapp.configuration.PropertiesConfigurationHolder;
import com.compomics.sigpep.webapp.reader.PICRReader;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 19/08/11
 * Time: 9:41
 * To change this template use File | Settings | File Templates.
 */
public class ProteinFormFieldFactory implements FormFieldFactory {
    private static final Logger logger = Logger.getLogger(ProteinFormFieldFactory.class);

    private MyVaadinApplication iApplication;
    private FormHelp iFormHelp;

    private Select iSpeciesSelect;
    private TextField iMassTextField;
    private TextField iMinimumCombinationSizeTextField;
    private TextField iMaximumCombinationSizeTextField;
    private Select iSignatureTransitionFinderTypeSelect;
    private Select iProteaseSelect;
    private TextField iProteinAccessionTextField;

    private boolean iVisible = Boolean.FALSE;

    public ProteinFormFieldFactory(MyVaadinApplication aApplication) {
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
        iFormHelp.addHelpForComponent(iSpeciesSelect, PropertiesConfigurationHolder.getInstance().getString("form_help.species"));

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
        iProteaseSelect = new Select("Protease");
        iProteaseSelect.setRequired(Boolean.TRUE);
        iProteaseSelect.setNullSelectionAllowed(Boolean.FALSE);
        iSpeciesSelect.setImmediate(Boolean.TRUE);
        iFormHelp.addHelpForComponent(iProteaseSelect, PropertiesConfigurationHolder.getInstance().getString("form_help.protease"));

        //protein accession field
        iProteinAccessionTextField = new TextField("Protein accession");
        iProteinAccessionTextField.setRequired(Boolean.TRUE);
        iProteinAccessionTextField.setImmediate(Boolean.TRUE);
        //iProteinAccessionTextField.addValidator(new RegexpValidator("ENS[0-9]{11}", PropertiesConfigurationHolder.getInstance().getString("form_validation.protein_accession")));
        iFormHelp.addHelpForComponent(iProteinAccessionTextField, PropertiesConfigurationHolder.getInstance().getString("form_help.protein_accession"));

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
                        logger.info("Creating sigpep session and query service for organism " + lOrganism.getScientificName());
                        iApplication.setSigPepSession(iApplication.getSigPepSessionFactory().createSigPepSession(lOrganism));
                        iApplication.setSigPepQueryService(iApplication.getSigPepSession().createSigPepQueryService());
                    }
                    fillProteaseSelect();
                }
            }
        });

        iProteinAccessionTextField.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent aValueChangeEvent) {
                String lProteinAccession = (String) iProteinAccessionTextField.getValue();

                Organism lOrganism = (Organism) iSpeciesSelect.getValue();
                if (lOrganism != null) {
                    if (!(lProteinAccession == null || lProteinAccession.equals(""))) {
                        String lMappedProteinAccession = "";
                        if (!lProteinAccession.startsWith("ENS")) {
                            try {
                                //do PICR search
                                List<String> lMappedProteinAccessions = PICRReader.doPICR(lProteinAccession, "ENSEMBL", Integer.toString(lOrganism.getTaxonId()));
                                if (lMappedProteinAccessions.size() == 0) {
                                    iApplication.getMainWindow().showNotification("No ENSEMBL mapping found", MessageFormat.format(PropertiesConfigurationHolder.getInstance().getString("form_validation.protein_accession_mapping_not_found"), lProteinAccession, lOrganism.getScientificName()), Window.Notification.TYPE_ERROR_MESSAGE);
                                } else if (lMappedProteinAccessions.size() == 1) {
                                    lMappedProteinAccession = lMappedProteinAccessions.get(0);
                                    iApplication.getMainWindow().showNotification("ENSEMBL mapping found", MessageFormat.format(PropertiesConfigurationHolder.getInstance().getString("form_validation.protein_accession_mapping"), lProteinAccession, lMappedProteinAccession, lOrganism.getScientificName()), Window.Notification.TYPE_WARNING_MESSAGE);
                                } else {
                                    iApplication.getMainWindow().showNotification("Multiple ENSEMBL mappings found", MessageFormat.format(PropertiesConfigurationHolder.getInstance().getString("form_validation.protein_accession_mapping_multiple"), lProteinAccession, lOrganism.getScientificName()), Window.Notification.TYPE_ERROR_MESSAGE);
                                }
                            } catch (IOException e) {
                                logger.error(e.getMessage(), e);
                            }
                        } else {
                            if (iApplication.getSigPepSession().getSimpleQueryDao().getProteinCountByName(lProteinAccession) != 1) {
                                iApplication.getMainWindow().showNotification("No protein found", MessageFormat.format(PropertiesConfigurationHolder.getInstance().getString("form_validation.protein_accession_not_found"), lProteinAccession, lOrganism.getScientificName()), Window.Notification.TYPE_ERROR_MESSAGE);
                            }
                        }
                        iProteinAccessionTextField.setValue(lMappedProteinAccession);
                    }
                }
            }

        });

        setFormComponentsVisible(iVisible);

    }

    public Field createField(Item aItem, Object o, Component aComponent) {
        String pid = (String) o;

        if ("species".equals(pid)) {
            return iSpeciesSelect;
        } else if ("massAccuracy".equals(pid)) {
            return iMassTextField;
        } else if ("minimumCombinationSize".equals(pid)) {
            return iMinimumCombinationSizeTextField;
        } else if ("maximumCombinationSize".equals(pid)) {
            return iMaximumCombinationSizeTextField;
        } else if ("signatureTransitionFinderType".equals(pid)) {
            return iSignatureTransitionFinderTypeSelect;
        } else if ("proteaseName".equals(pid)) {
            return iProteaseSelect;
        } else if ("proteinAccession".equals(pid)) {
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

    protected void fillProteaseSelect() {
        if (iProteaseSelect.size() != 0) {
            iProteaseSelect.removeAllItems();
        }
        for (String lProteaseName : iApplication.getSigPepSession().getSimpleQueryDao().getUsedProteaseNames()) {
            iProteaseSelect.addItem(lProteaseName);
        }
    }

    protected Set<Organism> getOrganisms() {
        return iApplication.getSigPepSessionFactory().getOrganisms();
    }

}