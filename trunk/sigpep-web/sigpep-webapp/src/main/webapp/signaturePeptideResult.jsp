<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>
<html>
<head>
    <title>Result</title>
</head>
<body>
<f:view>

    <h:panelGrid columns="3" border="1">

        <h:panelGrid columns="1" border="1">
            <h:outputText value="Species"/>
            <h:outputText value="#{sigPepSessionBean.organism.scientificName}"/>
        </h:panelGrid>

        <h:panelGrid columns="1" border="1">
            <h:outputText value="Protease"/>
            <h:dataTable var="protease" value="#{sigPepSessionBean.selectedProteases}">
                <h:column>
                    <h:outputText value="#{protease}"/>
                </h:column>
            </h:dataTable>

        </h:panelGrid>

        <h:panelGrid columns="1" border="1">
            <h:outputText value="Level"/>
            <h:outputText value="#{sigPepSessionBean.selectedLevel}"/>
        </h:panelGrid>



        <h:panelGrid border="1">
            <h:form>
            <t:dataTable id="signaturePeptideTable"
                         var="signaturePeptideFeature"
                         value="#{signaturePeptideResultBean.signaturePeptideFeatures}"
                         rows="10">
                <h:column>
                    <f:facet name="header">
                        <h:outputText value="start"/>
                    </f:facet>
                    <h:outputText value="#{signaturePeptideFeature.location.start}"/>
                </h:column>
                <h:column>
                    <f:facet name="header">
                        <h:outputText value="end"/>
                    </f:facet>
                    <h:outputText value="#{signaturePeptideFeature.location.end}"/>
                </h:column>
                <h:column>
                    <f:facet name="header">
                        <h:outputText value="sequence"/>
                    </f:facet>
                    <h:outputText value="#{signaturePeptideFeature.featureObject.sequenceString}"/>
                </h:column>
                <h:column>
                    <f:facet name="header">
                        <h:outputText value="length"/>
                    </f:facet>
                    <h:outputText value="#{signaturePeptideFeature.featureObject.sequenceLength}"/>
                </h:column>
                <f:facet name="footer">
                    <t:dataScroller id="signaturePeptideTableScroller"
                            for="signaturePeptideTable"
                            paginator="true"/>     
                </f:facet>
            </t:dataTable>
            </h:form>
        </h:panelGrid>

        <%--<h:panelGrid>--%>
            <%----%>
        <%--</h:panelGrid>--%>

    </h:panelGrid>

    <f:verbatim>
        <h:outputText value="new"/>
    </f:verbatim>

    <h:form>
        <h:commandLink id="backLink" action="back">
            <h:outputText id="backLinkText" value="back"/>
        </h:commandLink>
    </h:form>

</f:view>
</body>
</html>