<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<html>
<head>
    <title>Parameters</title>
</head>
<body>
<f:view>

    <h:outputText value="You have selected #{sigPepSessionBean.organism.scientificName}."/>


    <h:form id="parameterForm">

        <p>
        <h:outputLabel id="selectLevelLabel" for="selectLevel" value="Level: "/>
        <h:selectOneRadio id="selectLevel"
                          value="#{sigPepSessionBean.selectedLevel}">
            <f:selectItems value="#{parameterBean.availableLevels}"/>
        </h:selectOneRadio>
        </p>

        <p>
        <h:outputLabel id="accessionFilterLabel" for="accessionFilter" value="Accessions: "/>
        <h:inputTextarea id="accessionFilter"
                         value="#{sigPepSessionBean.accessionFilter}"
                         converter="inputListConverter"/>
        </p>

        <p>
        <h:outputLabel id="selectProteasesLabel" for="selectProteases" value="Proteases: "/>
        <h:selectManyCheckbox id="selectProteases"
                              value="#{sigPepSessionBean.selectedProteases}">
            <f:selectItems value="#{parameterBean.availableProteases}"/>
        </h:selectManyCheckbox>
        </p>

        <p>
        <h:commandButton id="submitButton" value="submit" action="#{parameterBean.send}"/>
        </p>

        <h:commandLink id="backLink" action="back">
            <h:outputText id="backLinkText" value="back"/>
        </h:commandLink>
    </h:form>

</f:view>
</body>
</html>