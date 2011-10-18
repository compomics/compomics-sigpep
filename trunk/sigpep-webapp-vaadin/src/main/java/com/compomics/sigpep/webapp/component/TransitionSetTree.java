package com.compomics.sigpep.webapp.component;

import com.compomics.sigpep.jtraml.TransitionBean;
import com.compomics.sigpep.webapp.MyVaadinApplication;
import com.google.common.base.Joiner;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.Action;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is a
 */
public class TransitionSetTree extends VerticalLayout implements Action.Handler {
    private static Logger logger = Logger.getLogger(TransitionSetTree.class);

    private static final Action ACTION_DELETE = new Action("Delete transition");
    private static final Action[] ACTIONS = new Action[]{ACTION_DELETE};

    private static int NODETYPE_PROTEIN = 1;
    private static int NODETYPE_PEPTIDE = 2;
    private static int NODETYPE_TRANSITION = 3;

    private Tree tree;
    private Button deleteButton;
    private final MyVaadinApplication iApplication;

    public TransitionSetTree(MyVaadinApplication aApplication) {
        iApplication = aApplication;
        setSpacing(true);

        // Create new Tree object using a hierarchical container from
        // ExampleUtil class
        Iterable<TransitionBean> lSelectedTransitionList = iApplication.getSelectedTransitionList();
        Container lContainer = createTransitionBeanContainer(lSelectedTransitionList);
        tree = new Tree("Selected Transtions", lContainer);

        tree.setItemCaptionPropertyId("name");


        // Set multiselect mode
        tree.setMultiSelect(true);
        tree.setImmediate(true);


        // Add Actionhandler
        tree.addActionHandler(this);

        // Set tree to show the 'name' property as caption for items

        // Expand whole tree
        for (Iterator<?> it = tree.rootItemIds().iterator(); it.hasNext(); ) {
            tree.expandItemsRecursively(it.next());
        }

        addComponent(tree);

    }

    /*
    * Returns the set of available actions
    */
    public Action[] getActions(Object target, Object sender) {
        return ACTIONS;
    }

    /*
    * Handle actions
    */
    public void handleAction(Action action, Object sender, Object target) {
        if (action == ACTION_DELETE) {
            Item lItem = tree.getItem(target);
            int lType = Integer.parseInt(lItem.getItemProperty("type").toString());
            String lName = lItem.getItemProperty("name").getValue().toString();

            logger.debug("deleting " + target.getClass());
            logger.debug("typeid " + lType);
            logger.debug("name " + lName);
            logger.debug("ids " + Joiner.on(' ').join(lItem.getItemPropertyIds()));

            if(lType == NODETYPE_PROTEIN){
                logger.debug("removing protein node");
                // not yet implemented

            }else if(lType == NODETYPE_PEPTIDE){
                logger.debug("removing peptide node");
                iApplication.removeTransitionBeansBySequence("" + lName);

            }else if(lType == NODETYPE_TRANSITION){
                logger.debug("removing transition node");
                iApplication.removeTransitionBeansByProductIonName("" + lName);
            }
            tree.removeItem(target);
        }
    }

    private HierarchicalContainer createTransitionBeanContainer(Iterable<TransitionBean> lTransitionBeanSet) {
        HierarchicalContainer lContainer = new HierarchicalContainer();
        lContainer.addContainerProperty("name", Label.class, null);
        lContainer.addContainerProperty("type", Label.class, null);

        Map<String, Object> lProteinMap = new HashMap<String, Object>();
        Map<String, Object> lPeptideMap = new HashMap<String, Object>();

        for (TransitionBean lBean : lTransitionBeanSet) {
            // new protein?
            String lProteins = Joiner.on('/').join(lBean.getProteinAccessions());
            String lPeptide = lBean.getPeptideSequence();
            String lTransition = new String(lBean.getIonType()) + lBean.getIonNumber();


            Object lProteinID;
            if (lProteinMap.get(lProteins) == null) {
                lProteinID = lContainer.addItem();
                lContainer.getContainerProperty(lProteinID, "name").setValue(lProteins);
                lContainer.getContainerProperty(lProteinID, "type").setValue(NODETYPE_PROTEIN);
                lProteinMap.put(lProteins, lProteinID);
            } else {
                lProteinID = lProteinMap.get(lProteins);
            }

            // new protein?
            Object lPeptideID;
            if (lPeptideMap.get(lPeptide) == null) {
                lPeptideID = lContainer.addItem();
                lContainer.getContainerProperty(lPeptideID, "name").setValue(lPeptide);
                lContainer.getContainerProperty(lPeptideID, "type").setValue(NODETYPE_PEPTIDE);
                lContainer.setParent(lPeptideID, lProteinID);
                lPeptideMap.put(lPeptide, lPeptideID);
            } else {
                lPeptideID = lPeptideMap.get(lPeptide);
            }

            // always a new transition!

            Object lTransitionID = lContainer.addItem();
            lContainer.getContainerProperty(lTransitionID, "name").setValue(lTransition);
            lContainer.getContainerProperty(lTransitionID, "type").setValue(NODETYPE_TRANSITION);
            lContainer.setParent(lTransitionID, lPeptideID);
            lContainer.setChildrenAllowed(lTransitionID, false);

        }
        return lContainer;
    }
}