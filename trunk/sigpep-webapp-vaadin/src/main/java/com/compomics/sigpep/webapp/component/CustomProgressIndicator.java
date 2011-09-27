package com.compomics.sigpep.webapp.component;


import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 22/09/11
 * Time: 13:40
 * To change this template use File | Settings | File Templates.
 */
public class CustomProgressIndicator extends VerticalLayout {

    private int iMaxStepNumber;
    private int iStepNumber;

    private ProgressIndicator iProgressIndicator;
    private Label iLabel;

    public CustomProgressIndicator(String aCaption, int aMaxStepNumber){
        iLabel = new Label();
        iLabel.setCaption(aCaption);
        iMaxStepNumber = aMaxStepNumber;
        iStepNumber = 0;

        iProgressIndicator = new ProgressIndicator();
        iProgressIndicator.setIndeterminate(Boolean.FALSE);
        iProgressIndicator.setValue(0f);
        iProgressIndicator.setSizeFull();

        this.setSpacing(Boolean.FALSE);
        //this.setMargin(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
        this.addComponent(iLabel);
        this.addComponent(iProgressIndicator);
    }

    public void setLabel(Label aLabel) {
        iLabel = aLabel;
    }

    public void proceed(String aMessage){
        if(iStepNumber != iMaxStepNumber){
            iLabel.setCaption(aMessage);
            iProgressIndicator.setValue((float ) iStepNumber / iMaxStepNumber);
            iStepNumber ++;
        }
        else {
            iProgressIndicator.setValue(1f);
        }
    }
}
