package com.compomics.sigpep.web.app;

import com.compomics.sigpep.model.Protease;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 24-Jul-2008<br/>
 * Time: 11:43:21<br/>
 */
public class ParameterBean {

    private SigPepSessionBean sigPepSessionBean;
    private List<String> levels;

    public List<SelectItem> getAvailableProteases() {
        List<SelectItem> retVal = new ArrayList<SelectItem>();
        for (Protease protease : sigPepSessionBean.getAvailableProteases()) {
            retVal.add(new SelectItem(protease.getShortName(), protease.getFullName()));
        }
        return retVal;
    }

    public List<SelectItem> getAvailableLevels() {
        List<SelectItem> retVal = new ArrayList<SelectItem>();
        for (String level : levels) {
            retVal.add(new SelectItem(level));
        }
        return retVal;
    }

    public List<String> getLevels() {
        return levels;
    }

    public void setLevels(List<String> levels) {
        this.levels = levels;
    }

    public SigPepSessionBean getSigPepSessionBean() {
        return sigPepSessionBean;
    }

    public void setSigPepSessionBean(SigPepSessionBean sigPepSessionBean) {
        this.sigPepSessionBean = sigPepSessionBean;
    }

    public String send() {
        return "success";
    }

}
