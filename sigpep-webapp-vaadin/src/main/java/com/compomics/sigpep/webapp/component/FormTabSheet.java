package com.compomics.sigpep.webapp.component;

import com.compomics.sigpep.model.Peptide;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.compomics.sigpep.webapp.bean.PeptideFormBean;
import com.compomics.sigpep.webapp.form.PeptideCheckForm;
import com.compomics.sigpep.webapp.form.PeptideForm;
import com.compomics.sigpep.webapp.form.ProteinForm;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 19/09/11
 * Time: 11:48
 * To change this template use File | Settings | File Templates.
 */
public class FormTabSheet extends VerticalLayout implements TabSheet.SelectedTabChangeListener {
    private MyVaadinApplication iMyVaadinApplication;

    private TabSheet iTabSheet;
    private ProteinForm iProteinForm;
    private PeptideCheckForm iPeptideCheckForm;
    private PeptideForm iPeptideForm;
    private VerticalLayout lV2;

    public FormTabSheet(MyVaadinApplication aMyVaadinApplication){
        iMyVaadinApplication = aMyVaadinApplication;


        VerticalLayout lV1 = new VerticalLayout();
        lV1.setMargin(Boolean.TRUE);
        iProteinForm =  new ProteinForm("Protein form", iMyVaadinApplication);
        lV1.addComponent(iProteinForm);

        lV2 = new VerticalLayout();
        lV2.setMargin(Boolean.TRUE);
        iPeptideCheckForm = new PeptideCheckForm("Peptide check form", iMyVaadinApplication);
        lV2.addComponent(iPeptideCheckForm);

        iTabSheet = new TabSheet();
        iTabSheet.addTab(lV1, "Protein", null);
        iTabSheet.addTab(lV2, "Peptide", null);

        this.addComponent(iTabSheet);
    }

    public void selectedTabChange(TabSheet.SelectedTabChangeEvent aSelectedTabChangeEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void proceedPeptideForm(PeptideFormBean aPeptideFormBean, Peptide aPeptide){
        lV2.removeComponent(iPeptideCheckForm);
        iPeptideForm = new PeptideForm("Peptide form", aPeptideFormBean, aPeptide, iMyVaadinApplication);
        lV2.addComponent(iPeptideForm);
    }

    public void cancelPeptideForm(){
        lV2.removeComponent(iPeptideForm);
        iPeptideCheckForm = new PeptideCheckForm("Peptide check form", iMyVaadinApplication);
        lV2.addComponent(iPeptideCheckForm);
    }
}
