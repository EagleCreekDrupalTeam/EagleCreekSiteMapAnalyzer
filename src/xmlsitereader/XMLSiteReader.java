package xmlsitereader;

import java.io.FileNotFoundException;
/**
 *
 ** @author Stephen Paden 
 ** Company: Eagle Creek Software Services 
 ** Date: 2/26/2014
 *
 */
public class XMLSiteReader {

    public static void main(String[] args) throws FileNotFoundException {
        
        // stepwise refinement testing below        
        String fileName = "placeHolder";
        String urlPath = "placeHolder";
        
        URL newURL = new URL(urlPath, true, false, false);
        URL newNewURL = new URL();
        
        newURL.setIsDocument(true);
        newURL.setIsImage(false);
        
        XML newXML = new XML(fileName);
        newXML.setFileName(fileName);
        newXML.parseXML();
        newXML.printResults();     
        
        System.out.println(newURL.getIsDocument());
        System.out.println(newURL.getURL() + " overloaded constructor");
        System.out.println(newNewURL.getURL() + " default constructor");
    }    
}
