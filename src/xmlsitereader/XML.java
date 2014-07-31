package xmlsitereader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Matcher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 ** @author Curtis Conner and Stephen Paden * Company: Eagle Creek Software
 * Services * Date: 2/26/2014
 * Re-factored by Curtis Conner 7-30-2014
 *
 */
public class XML {

    private static int sumPages = 0;
    private static int sumDocuments = 0;
    private static int sumMedia = 0;
    private static int sumTotal = 0;
    private static final String defaults = "defaults.txt";
    private static final String preferences = "preferences.txt";
    private String fileName;
    private File file;
    private ArrayList<URL> urls;
    private String[] fullPaths;    
    private ArrayList<URLExtension> urlExtensions = new ArrayList<URLExtension>();
    private ArrayList<URLExtension> defaultURLExtensions = new ArrayList<URLExtension>();
    
    private ArrayList<String> dpe = new ArrayList<String>(Arrays.asList(".html",".htm",".aspx",".jsp",".php",".asp",".shtml"));
    private ArrayList<String> dde = new ArrayList<String>(Arrays.asList(".docx",".doc",".dot",".pdf",".txt",".rft",".odt",".odg",".csv",".xls",".xlsx",".xlt",".ppt",".pptx"));
    private ArrayList<String> dme = new ArrayList<String>(Arrays.asList(".gif",".jpg",".png",".jpeg",".bmp",".ico"));
            
    /**
     * Constructor
     */
    public XML() {
        
//        for (String string : dpe) {
//            defaultURLExtensions.add(new URLExtension(string, URLType.Page));
//        }
//        for (String string : dde) {
//            defaultURLExtensions.add(new URLExtension(string, URLType.Document));
//        }
//        for (String string : dme) {
//            defaultURLExtensions.add(new URLExtension(string, URLType.Media));
//        }
//        
//        try {
//            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(defaults));
//            output.writeObject(defaultURLExtensions);
//        }
//        catch(IOException e) {
//            
//        }
//        
//        try {
//            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(preferences));
//            output.writeObject(defaultURLExtensions);
//        }
//        catch(IOException e) {
//            
//        }
        
        // Load the default extensions and user created extensions from files
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(defaults));
            defaultURLExtensions = (ArrayList<URLExtension>)input.readObject();
        }
        catch(ClassNotFoundException | IOException e) {
            System.out.println("BAD THINGS HAPPENED WHEN READING " + e);
        }
        
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(preferences));
            urlExtensions = (ArrayList<URLExtension>)input.readObject();
        }
        catch(ClassNotFoundException | IOException e) {
            System.out.println("BAD THINGS HAPPENED WHEN READING OR CASTING " + e);
        }
    }
    /**
     * 
     * @param fName 
     */   
    public XML(String fName) {
        fileName = fName;
    }
    /**
     * 
     * @param fName 
     */
    public void setFileName(String fName) {
        fileName = fName;
    }
    /**
     * 
     * @return 
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * 
     * @param file 
     */
    public void setFile(File file) {
        this.file = file;
        setFileName(file.getName());
    }
    /**
     * 
     * @return 
     */
    public File getFile() {
        return file;
    }
    /**
     * 
     * @return 
     */
    public URL[] getURLs() {
        return urls.toArray(new URL[urls.size()]);
    }    
    /**
     * Return an Array of urls of a specified type
     * @param urlType
     * @return 
     */
    public URL[] getURLsOfType(URLType urlType) {
        ArrayList<URL> urlsOfType = new ArrayList<>();
        for (URL url : urls) {
            if (url.getURLType() == urlType) {
                urlsOfType.add(url);
            }
        }
        return urlsOfType.toArray(new URL[urlsOfType.size()]);
    }    
    /**
     * Return a String of extensions of a specified type
     * @param urlType
     * @return 
     */
    public String getExtensionsOfType(URLType urlType) {
        ArrayList<String> extensionsOfType = new ArrayList<>();
        for (URLExtension extension : urlExtensions) {
            if (extension.getURLType() == urlType) {
                extensionsOfType.add(extension.getExtension());
            }
        }
        return join(extensionsOfType, ",");
    }
    
    /**
     * Set extensions of specified type to user input and save to preferences
     * @param extensions
     * @param urlType 
     */
    public void setExtensionsOfType(String extensions, URLType urlType) {
        ArrayList<String> list = split(extensions, ",");
        clearExtensionsOfType(urlType);
        for (String extension : list) {
            urlExtensions.add(new URLExtension(extension, urlType));
        }
        saveExtensions();
    }
    
    /**
     * Set extensions of specified type to defaults and save to preferences
     * @param urlType 
     */
    public void resetExtensionsOfType(URLType urlType) {
        clearExtensionsOfType(urlType);
        for (URLExtension urlExtension : defaultURLExtensions) {
            if (urlExtension.getURLType() == urlType) {
                urlExtensions.add(urlExtension);
            }            
        }
        saveExtensions();
    }
    
    /**
     * Clear extensions of a specified type from list
     * @param urlType 
     */
    private void clearExtensionsOfType(URLType urlType) {
        ArrayList<URLExtension> newURLExtensions = new ArrayList<>();
        for (URLExtension urlExtension : urlExtensions) {
            if (urlExtension.getURLType() != urlType) {
                newURLExtensions.add(urlExtension);
            }
        }
        urlExtensions = newURLExtensions;
    }
    
    /**
     * Save list of extensions to preferences
     */
    private void saveExtensions() {
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(preferences));
            output.writeObject(urlExtensions);
        }
        catch(IOException e) {
            
        }
    }
    
    /**
     * Parses through the xml sitemap to build the list of urls
     *
     * @throws FileNotFoundException
     */
    public void parseXML() throws FileNotFoundException {

        try {
            if (file != null) {
                DocumentBuilderFactory newDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder newBuilder = newDocumentBuilderFactory.newDocumentBuilder();
                Document doc = newBuilder.parse(file);
                doc.getDocumentElement().normalize();            
                
                NodeList nodeList = doc.getElementsByTagName("loc");             
                
                fullPaths = new String[nodeList.getLength()];

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node newNode = nodeList.item(i);
                    fullPaths[i] = newNode.getTextContent().toLowerCase();
                }
                //Initialize ArrayList objects
                urls = new ArrayList<>();
                
                for(String fullPath : fullPaths) {
                    boolean stored = false; // Flag to see if we've matched an extension
                    // Check for query string in path
                    if (fullPath.contains("?")) {
                        fullPath = getBaseURL(fullPath); // Strip off query string
                        // Check for a duplicate of the base url
                        stored = isDuplicateURL(fullPath);
                    }
                    
                    if (!stored) {
                        boolean matched = matchPath(urlExtensions, fullPath);
                        if (!matched) {
                            urls.add(new URL(fullPath, "other", URLType.Page));
                        }
                    }
                }               
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            System.out.println(e);
        }

        Collections.sort(urls);

    }
    /**
     * Get the base url from one with a query string
     *
     * @param url
     * @return
     */
    public String getBaseURL(String url) {
        int queryIndex = url.indexOf("?");
        return url.substring(0, queryIndex);
    }
    /**
     * Check the list of urls for a duplicate
     *
     * @param baseURL
     * @return
     */
    public boolean isDuplicateURL(String baseURL) {
        for (URL url : urls) {
            if (url.getURL().equals(baseURL)) {
                return true;
            }
        }
        return false;
    }
    // KEEP
    /**
     * Join a list of Strings as one String separated by a given delimeter
     *
     * @param list
     * @param delimiter
     * @return
     */
    public String join(ArrayList<String> list, String delimiter) {
        StringBuilder builder = new StringBuilder();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            builder.append(iter.next());
            if (!iter.hasNext()) {
                break;
            }
            builder.append(delimiter);
        }
        return builder.toString();
    }
    /**
     * Split a String into a list of Strings at a given delimeter
     *
     * @param extensions
     * @param delimeter
     * @return
     */
    public ArrayList<String> split(String extensions, String delimeter) {
        String[] temp = extensions.split(delimeter);
        return new ArrayList<String>(Arrays.asList(temp));
    }
    /**
     * 
     * @param extensions
     * @param path
     * @return 
     */
    public boolean matchPath(ArrayList<URLExtension> extensions, String path) {
        
        for (URLExtension extension : extensions) {
            Matcher matcher = extension.getPattern().matcher(path);
            if (matcher.find()) {
                urls.add(new URL(path, matcher.group(), extension.getURLType()));
                return true;
            }
        }
        return false;
    }
    
    /**
     * Generate counts of url types
     */
    public void calculateResults() {
        resetCounts();
        for (URL url : urls) {
            if (url.getURLType() == URLType.Page) {
                sumPages++;
            }
            else if (url.getURLType() == URLType.Document) {
                sumDocuments++;
            }
            else {
                sumMedia++;
            }
        }
        sumTotal = urls.size();
    }
    /**
     * Reset url counts so analysis can be ran more than once
     */
    public void resetCounts() {
        sumPages = 0;
        sumDocuments = 0;
        sumMedia = 0;
        sumTotal = 0;
    }
    /**
     * Build a String with the results to be displayed
     *
     * @return
     */
    public String printResults() {
        calculateResults();
        StringBuilder builder = new StringBuilder();
        builder.append("Number of pages: ").append(sumPages).append("\n");
        builder.append("Number of documents: ").append(sumDocuments).append("\n");
        builder.append("Number of media: ").append(sumMedia).append("\n");
        builder.append("Total number of elements: ").append(sumTotal);

        return builder.toString();
    }

    // added by Stephen Paden, 3/6/14
    /**
     * 
     * @param newFile
     * @param defaultExtension
     * @throws FileNotFoundException 
     * This method is responsible for formatting the output to a hard-coded .xls file.
     */
    public void createReport(File newFile, String defaultExtension) throws FileNotFoundException {
        PrintWriter newPrintWriter = new PrintWriter(newFile + ".xls");
        try {
            newPrintWriter.write(printResults() + "\n");
            newPrintWriter.write("Eagle Creek Sitemap Analyzer Report for: " + getFileName() + "\n");
            newPrintWriter.write("********************************************\n");
            newPrintWriter.write("Page URLS: \n");
            newPrintWriter.write("********************************************\n");
//            newPrintWriter.write(pageURLs.toString().replace(",", "")
//                    .replace("[", "")
//                    .replace("]", "") + "\n");
            newPrintWriter.write("");
            newPrintWriter.write("********************************************\n");
            newPrintWriter.write("Document URLS: \n");
            newPrintWriter.write("********************************************\n");
//            newPrintWriter.write(documentURLs.toString().replace(",", "")
//                    .replace("[", "")
//                    .replace("]", "") + "\n");
            newPrintWriter.write("");
            newPrintWriter.write("********************************************\n");
            newPrintWriter.write("Media URLs: \n");
            newPrintWriter.write("********************************************\n");
//            newPrintWriter.write(mediaURLs.toString().replace(",", "")
//                    .replace("[", "")
//                    .replace("]", "") + "\n");
            newPrintWriter.write("********************************************\n");
            newPrintWriter.write("Summary: \n");
            newPrintWriter.write("********************************************\n");            
            newPrintWriter.close();

        } catch (SecurityException se) {
            System.out.println(se);
        }
    }
}
