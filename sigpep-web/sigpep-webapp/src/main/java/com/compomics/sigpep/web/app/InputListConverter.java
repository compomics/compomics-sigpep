package com.compomics.sigpep.web.app;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 25-Jul-2008<br/>
 * Time: 16:32:32<br/>
 */
public class InputListConverter implements Converter {


    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) throws ConverterException {
        List<String> retVal = new ArrayList<String>();

        for(String item : s.split("[;,\\n]")){
            retVal.add(item.replaceAll("[\\n\\s]", ""));
        }

        return retVal;
    }

    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) throws ConverterException {

        List<String> items = (List<String>)o;
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        for(String item : items){

            buffer.append(item);
            if(++i < items.size()){
                buffer.append("\n");
            }

        }
        return buffer.toString();

    }

}
