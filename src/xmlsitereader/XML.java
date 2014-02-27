package xmlsitereader;

import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 ** @author Stephen Paden 
 ** Company: Eagle Creek Software Services 
 ** Date: 2/26/2014
 *
 */

public class XML {
    private int sumPages = 0;
    private int sumDocuments = 0;
    private int sumOtherItems = 0;
    private int sumTotal = 0;
    public String fileName;
    private static int queryStrings = 0;
    public XML(String fName) {
        fileName = fName;
    }

    public void setFileName(String fName) {
        fileName = fName;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void parseXML() throws FileNotFoundException {

        try {
            
            File newFile = new File("sitemap.xml");
            DocumentBuilderFactory newDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder newBuilder = newDocumentBuilderFactory.newDocumentBuilder();
            Document doc = newBuilder.parse(newFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("loc");

            System.out.println("Base element :" + doc.getDocumentElement().getNodeName());

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node newNode = nodeList.item(i);
                if (newNode.getChildNodes().toString().contains("loc")) {
                    if (newNode.getTextContent().toLowerCase().endsWith(".html")
                            || newNode.getTextContent().toLowerCase().endsWith(".htm")
                            || newNode.getTextContent().toLowerCase().endsWith(".asp")
                            || newNode.getTextContent().toLowerCase().endsWith(".jsp")
                            || newNode.getTextContent().toLowerCase().endsWith(".php")
                            || newNode.getTextContent().toLowerCase().endsWith(".htm")) {
                        sumPages++;
                    } else if (newNode.getTextContent().toLowerCase().endsWith(".doc")
                            || newNode.getTextContent().toLowerCase().endsWith(".pdf")
                            || newNode.getTextContent().toLowerCase().endsWith(".docx")
                            || newNode.getTextContent().toLowerCase().endsWith(".txt")
                            || newNode.getTextContent().toLowerCase().endsWith(".odt")
                            || newNode.getTextContent().toLowerCase().endsWith(".odg")
                            || newNode.getTextContent().toLowerCase().endsWith(".csv")
                            || newNode.getTextContent().toLowerCase().endsWith(".xls")
                            || newNode.getTextContent().toLowerCase().endsWith(".xlsx")) {
                        sumDocuments++;
                    } else if (newNode.getTextContent().contains("")){                        
                       System.out.println("<-------------------------------------<Dynamic Path>------------------------------------------->");
                        queryStrings++;
                    } else {
                        System.out.println(newNode.getFirstChild().getTextContent() 
                                + " <--------------------------------------------------------------------"
                                + " ********this might be a null object or something we cannot parse yet ********* ");
                        sumOtherItems++;                       
                    }
                }                
                System.out.println(newNode.getTextContent());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int calculateResults() {
        return sumTotal = sumPages + sumDocuments + sumOtherItems + queryStrings;
    }

    public void printResults() {
        System.out.println();
        System.out.println("Number of pages: " + sumPages);
        System.out.println("Number of documents: " + sumDocuments);
        System.out.println("Number of other items: " + sumOtherItems);
        System.out.println("Number of dynamic paths: " + queryStrings);
        System.out.println("Total number of elements: " + calculateResults());
    }
    
    public String createReport() {
        String output = "";
        
        
        
        
        return output;
    }
}
