<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<html>
<head>
    <title>Welcome to Signature Peptide DB</title>
</head>
<body>
<f:view>

    <f:verbatim><h2></f:verbatim>
    <h:outputText id="welcomeOutput" value="Welcome to Signature Peptide DB"/>
    <f:verbatim></h2></f:verbatim>

    <h:form id="organismChoiceForm">

        <p>
            <h:outputLabel id="organismInputLabel" value="Please choose an organism " for="organismInput"/>
            <h:selectOneMenu id="organismInput"
                                value="#{sigPepSessionBean.selectedOrganism}"
                                required="true">
                <f:selectItems value="#{selectOrganismBean.availableOrganisms}"/>
            </h:selectOneMenu>

            <h:commandButton id="submitButton" value="submit" action="#{selectOrganismBean.send}"/>
        </p>

        <p>
            <h:message id="organismChoiceErrors" for="submitButton"/>
        </p>
    </h:form>
</f:view>
</body>
</html>
