package com.compomics.sigpep.webapp.component;

import com.compomics.sigpep.jtraml.TransitionBean;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is a
 */
public class TransitionSetTree extends VerticalLayout implements Action.Handler {
    private static Logger logger = Logger.getLogger(TransitionSetTree.class);

    private static final Action ACTION_ADD = new Action("Add child item");
    private static final Action ACTION_DELETE = new Action("Delete");
    private static final Action[] ACTIONS = new Action[]{ACTION_ADD, ACTION_DELETE};

    private Tree tree;
    private Button deleteButton;

    public TransitionSetTree(ArrayList<TransitionBean> aTransitionBeanSet) {
        setSpacing(true);

        // Create new Tree object using a hierarchical container from
        // ExampleUtil class
        Container lContainer = createTransitionBeanContainer(aTransitionBeanSet);
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
        if (action == ACTION_ADD) {
            // Allow children for the target item
            tree.setChildrenAllowed(target, true);

            // Create new item, disallow children, add name, set parent
            Object itemId = tree.addItem();
            tree.setChildrenAllowed(itemId, false);
            String newItemName = "New Item # " + itemId;
            Item item = tree.getItem(itemId);
            item.getItemProperty(target).setValue(newItemName);
            tree.setParent(itemId, target);
            tree.expandItem(target);
        } else if (action == ACTION_DELETE) {
            Object parent = tree.getParent(target);
            tree.removeItem(target);
            // If the deleted object's parent has no more children, set it's
            // childrenallowed property to false
            if (parent != null && tree.getChildren(parent).size() == 0) {
                tree.setChildrenAllowed(parent, false);
            }
            logger.debug("implement treenode deletion!!");
        }
    }

    private HierarchicalContainer createTransitionBeanContainer(Iterable<TransitionBean> lTransitionBeanSet) {
        HierarchicalContainer lContainer = new HierarchicalContainer();
        lContainer.addContainerProperty("name", Label.class, null);

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
                lProteinMap.put(lProteins, lProteinID);
            } else {
                lProteinID = lProteinMap.get(lProteins);
            }

            // new protein?
            Object lPeptideID;
            if (lPeptideMap.get(lPeptide) == null) {
                lPeptideID = lContainer.addItem();
                lContainer.getContainerProperty(lPeptideID, "name").setValue(lPeptide);
                lContainer.setParent(lPeptideID, lProteinID);
                lPeptideMap.put(lPeptide, lPeptideID);
            } else {
                lPeptideID = lPeptideMap.get(lPeptide);
            }

            // always a new transition!

            Object lTransitionID = lContainer.addItem();
            lContainer.getContainerProperty(lTransitionID, "name").setValue(lTransition);
            lContainer.setParent(lTransitionID, lPeptideID);
            lContainer.setChildrenAllowed(lTransitionID, false);

        }
        return lContainer;
    }
}