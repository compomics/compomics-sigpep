package com.compomics.sigpep.webapp.form;

import com.compomics.sigpep.webapp.bean.MainFormBean;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;

/**
 * Created by IntelliJ IDEA.
 * User: niels
 * Date: 17/08/11
 * Time: 13:34
 * To change this template use File | Settings | File Templates.
 */
public class MainForm extends Form {

    MainFormBean iMainFormBean;

    public MainForm() {
        iMainFormBean = new MainFormBean();
        BeanItem lBeanItem = new BeanItem(iMainFormBean);
        this.setItemDataSource(lBeanItem);
    }

}
