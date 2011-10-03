package com.compomics.sigpep.webapp.reader;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Nov 25, 2009
 * Time: 2:45:13 PM
 * <p/>
 * This class
 */
public class PICRReader {
    // Class specific log4j logger for com.compomics.sigpep.webapp.reader.PICRReader instances.
    private static final Logger logger = Logger.getLogger(PICRReader.class);
    private static final String PREFIX = "ENSP";

    private static final String NAMESPACE_1 = "http://model.picr.ebi.ac.uk";
    private static final String NAMESPACE_PREFIX_1 = "ns1";
    private static final String NAMESPACE_2 = "http://www.ebi.ac.uk/picr/AccessionMappingService";
    private static final String NAMESPACE__PREFIX_2 = "ns2";

    public static void main(String[] args) throws Exception {
        String lAccession = "P60709";
        String lTargetDatabase = "ENSEMBL";
        String lTaxonId = "9606";
        List<String> lMappedAccessions = doPICR(lAccession, lTargetDatabase, lTaxonId);
        if(lMappedAccessions.size() == 0){
            System.out.println("null");
        }
        for (String s : lMappedAccessions) {
            System.out.println(s);
        }
    }

    public static List<String> doPICR(final String aAccession, final String aTargetDatabase, final String aTaxonId) throws IOException {
        String lPICR = "http://www.ebi.ac.uk/Tools/picr/rest/getUPIForAccession?accession=" + aAccession + "&database=" + aTargetDatabase + "&taxid=" + aTaxonId;
        logger.info("sending REST request: " + lPICR);
        URL lURL = new URL(lPICR);

        InputStream lInputStream = lURL.openStream();

        List<String> lMappedAccessions = new ArrayList<String>();
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            DocumentBuilder builder = domFactory.newDocumentBuilder();

            Document doc = null;
            try {
                doc = builder.parse(lInputStream);
                //doc = builder.parse(new File("C:\\Users\\niels\\Desktop\\picrtest.xml"));
            } catch (SAXException e) {
                logger.error(e.getMessage(), e);
                return lMappedAccessions;
            }
            lInputStream.close();

            NamespaceContext lNamespaceContext = new NamespaceContext() {

                public String getNamespaceURI(String aPrefix) {
                    if (aPrefix.equals(NAMESPACE_PREFIX_1)) {
                        return NAMESPACE_1;
                    } else if (aPrefix.equals(NAMESPACE__PREFIX_2)) {
                        return NAMESPACE_2;
                    } else {
                        return null;
                    }
                }

                public String getPrefix(String namespaceURI) {
                    return null;
                }

                public Iterator getPrefixes(String namespaceURI) {
                    return null;
                }
            };

            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            xpath.setNamespaceContext(lNamespaceContext);
            XPathExpression expr = xpath.compile("//ns2:getUPIForAccessionReturn/ns1:logicalCrossReferences");

            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getChildNodes().item(0).getTextContent().startsWith(PREFIX)) {
                    lMappedAccessions.add(nodes.item(i).getChildNodes().item(0).getTextContent());
                }
            }

        } catch (ParserConfigurationException e) {
            logger.error(e.getMessage(), e);
        } catch (XPathExpressionException e) {
            logger.error(e.getMessage(), e);
        }

        return lMappedAccessions;
    }
}
