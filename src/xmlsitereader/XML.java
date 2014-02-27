package xmlsitereader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private String fileName;
    private File file;
    private URL[] urls;
    private String[] fullPaths;
    private final ArrayList<String> documentExtensions = new ArrayList(Arrays.asList(".doc", ".docx", ".pdf", ".txt", ".odt",".odg", ".csv", ".xls", ".xlsx"));
    private final ArrayList<String> pageExtensions = new ArrayList(Arrays.asList(".htm", ".html", ".asp", ".jsp", ".php"));
    private final ArrayList<String> imageExtensions = new ArrayList(Arrays.asList());
   
    private static int queryStrings = 0;
    
    public XML() {
    }
    public XML(String fName) {
        fileName = fName;
    }

    public void setFileName(String fName) {
        fileName = fName;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFile(File file) {
        this.file = file;
    }
    
    public File getFile() {
        return file;
    }
    
    public void setURLs(URL[] urls) {
        this.urls = urls;
    }
    
    public URL[] getURLs() {
        return urls;
    }
    
        
    public void parseXML() throws FileNotFoundException {

        try {
            if (file != null) {
                DocumentBuilderFactory newDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder newBuilder = newDocumentBuilderFactory.newDocumentBuilder();
                Document doc = newBuilder.parse(file);
                doc.getDocumentElement().normalize();            
                
                NodeList nodeList = doc.getElementsByTagName("loc");
                
                System.out.println("Number of Nodes: " + nodeList.getLength());
                
                fullPaths = new String[nodeList.getLength()];
                
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node newNode = nodeList.item(i);
                    fullPaths[i] = newNode.getTextContent();
                }
                
                urls = new URL[nodeList.getLength()];
                
                for (int i = 0; i < fullPaths.length; i++) {
                    boolean stored = false;
                    for (String extension : documentExtensions) {
                        if (fullPaths[i].endsWith(extension)) {
                            urls[i] = new URL(fullPaths[i], false, true, false);
                            stored = true;
                        }
                    }
                    for (String extension : pageExtensions) {
                        if (fullPaths[i].endsWith(extension)) {
                            urls[i] = new URL(fullPaths[i], true, false, false);
                            stored = true;
                        }                    
                    }
                    
                    if (!stored) {
                        urls[i] = new URL(fullPaths[i], true, false, false);
                    }
                    
                }
                
                for (int i = 0; i < urls.length; i++) {
                    System.out.println(urls[i].getURL());
                }
   
                
                System.out.println("Base element :" + doc.getDocumentElement().getNodeName());
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
