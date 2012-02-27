package com.compomics.sigpep.webapp.component;

import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Link;

/**
 * This class is a
 */
public class InfoLink extends Link {

    private MyVaadinApplication iApplication;

    public enum InfoPages{GENERAL, GRAPH, FORM_OPTIONS, CALCULATION};


    /**
     * Creates a new push button. The value of the push button is false and it
     * is immediate by default.
     */
    public InfoLink(MyVaadinApplication aApplication, InfoPages aInfoPage) {
        super();
        iApplication = aApplication;
        setIcon(new ClassResource("/images/info.gif", iApplication));
        if(aInfoPage.equals(InfoPages.GENERAL)){
            setResource(new ExternalResource("http://code.google.com/p/compomics-sigpep/wiki/General"));
            setDescription("SigPep Wiki - General");
        }else if(aInfoPage.equals(InfoPages.GRAPH)){
            setResource(new ExternalResource("http://code.google.com/p/compomics-sigpep/wiki/General#web_graph_reports_(1)", "_blank"));
            setDescription("SigPep Wiki - Graph Details");
        }else if(aInfoPage.equals(InfoPages.FORM_OPTIONS)){
            setResource(new ExternalResource("http://code.google.com/p/compomics-sigpep/wiki/General#form_options", "_blank"));
            setDescription("SigPep Wiki - Form Options");
        }else if(aInfoPage.equals(InfoPages.CALCULATION)){
            setResource(new ExternalResource("http://code.google.com/p/compomics-sigpep/wiki/General#peptide_signature_transition_set_calculation_(2)", "_blank"));
            setDescription("SigPep Wiki - Background Calculation");
        }else {
            setResource(new ExternalResource("http://code.google.com/p/compomics-sigpep/wiki/General", "_blank"));
            setDescription("SigPep Wiki");
        }

    }
}
