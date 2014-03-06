package xmlsitereader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 ** @author Stephen Paden and Curtis Conner
 ** Company: Eagle Creek Software Services 
 ** Date: 2/26/2014
 *
 */

public class XML {
    private static int sumPages = 0;
    private static int sumDocuments = 0;
    private static int sumImages = 0;
    private static int sumOtherItems = 0;
    private static int sumTotal = 0;
    private static int sumVideos = 0;
    private static int sumRSSFeeds = 0;
    private String fileName;
    private File file;
    private ArrayList<URL> urls;
    private ArrayList<URL> documentURLs;
    private ArrayList<URL> pageURLs;
    private ArrayList<URL> imageURLs;
    private String[] fullPaths;
    
    private ArrayList<String> defaultDocumentExtensions = new ArrayList(Arrays.asList(".docx", ".doc", ".pdf", ".txt", ".odt",".odg", ".csv", ".xls", ".xlsx", ".xlt"));
    private ArrayList<String> defaultPageExtensions = new ArrayList(Arrays.asList(".html", ".htm", ".aspx", ".jsp", ".php", ".asp", ".shtml"));
    private ArrayList<String> defaultImageExtensions = new ArrayList(Arrays.asList(".gif", ".jpg", ".png", ".jpeg", ".bmp"));
        
    private ArrayList<String> updatedDocumentExtensions = new ArrayList();
    private ArrayList<String> updatedPageExtensions = new ArrayList();
    private ArrayList<String> updatedImageExtensions = new ArrayList();
        
    private ArrayList<Pattern> documentExtensionPatterns = new ArrayList<Pattern>();
    private ArrayList<Pattern> pageExtensionPatterns = new ArrayList<Pattern>();
    private ArrayList<Pattern> imageExtensionPatterns = new ArrayList<Pattern>();
    
    private HashMap<String, Integer> queriedURLs = new HashMap<String, Integer>();
    
    private Pattern test = Pattern.compile("hello");
   
    private static int queryStrings = 0;
    
    public XML() {
        //Initialize the pattern lists from the default extension lists 
        buildPatterns(documentExtensionPatterns, defaultDocumentExtensions);
        buildPatterns(pageExtensionPatterns, defaultPageExtensions);
        buildPatterns(imageExtensionPatterns, defaultImageExtensions);
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
        setFileName(file.getName());
    }
    
    public File getFile() {
        return file;
    }
    
    public URL[] getURLs() {
        return urls.toArray(new URL[urls.size()]);
    }
    
    public URL[] getDocumentURLs() {
        return documentURLs.toArray(new URL[documentURLs.size()]);
    }
    public URL[] getPageURLs() {
        return pageURLs.toArray(new URL[pageURLs.size()]);
    }
    public URL[] getImageURLs() {
        return imageURLs.toArray(new URL[imageURLs.size()]);
    }
    
    public void setPageExtensions(String extensions) {
        this.updatedPageExtensions = split(extensions, ",");
        pageExtensionPatterns.clear();
        buildPatterns(pageExtensionPatterns, updatedPageExtensions);
    }
    
    public void setDocumentExtensions(String extensions) {
        this.updatedDocumentExtensions = new ArrayList(split(extensions, ","));
        documentExtensionPatterns.clear();
        buildPatterns(documentExtensionPatterns, updatedDocumentExtensions);
    }
    
    public void setImageExtensions(String extensions) {        
        this.updatedImageExtensions = split(extensions, ",");
        imageExtensionPatterns.clear();
        buildPatterns(imageExtensionPatterns, updatedImageExtensions);
    }
    
    public void resetPageExtensions()  {
        pageExtensionPatterns.clear();
        buildPatterns(pageExtensionPatterns, defaultPageExtensions);
    }
    
    public void resetDocumentExtensions() {
        documentExtensionPatterns.clear();
        buildPatterns(documentExtensionPatterns, defaultDocumentExtensions);
    }
    
    public void resetImageExtensions() {
        imageExtensionPatterns.clear();
        buildPatterns(imageExtensionPatterns, defaultImageExtensions);
    }
    
    public String getPageExtensions() {
        return join(updatedPageExtensions, ",");
    }
    
    public String getDocumentExtensions() {
        return join(updatedDocumentExtensions, ",");
    }
    
    public String getImageExtensions() {
        return join(updatedImageExtensions, ",");
    }
    
    public String getDefaultPageExtensions() {
        return join(defaultPageExtensions, ",");
    }
    
    public String getDefaultDocumentExtensions() {
        return join(defaultDocumentExtensions, ",");
    }
    
    public String getDefaultImageExtensions() {
        return join(defaultImageExtensions, ",");
    }
    /**
     * Builds list of Patterns from the list of Strings, resets lists if append is false
     * @param patterns
     * @param extensions
     * @param append 
     */
    public void buildPatterns(ArrayList<Pattern> patterns, ArrayList<String> extensions) {
        System.out.println("Before: " + patterns.size());
        for (String extension : extensions) {
            patterns.add(Pattern.compile(extension + "$"));
        }
        System.out.println("After build: " + patterns.size());
                
    }
    
    /**
     * Parses through the xml sitemap to build the lists of urls
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
                
                System.out.println("Number of Nodes: " + nodeList.getLength());
                
                fullPaths = new String[nodeList.getLength()];
                
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node newNode = nodeList.item(i);
                    fullPaths[i] = newNode.getTextContent();
                }
                
                urls = new ArrayList<>();
                documentURLs = new ArrayList<>();
                pageURLs = new ArrayList<>();
                imageURLs = new ArrayList<>();
                
                for (int i = 0; i < fullPaths.length; i++) {
                    String fullPath = fullPaths[i];
                    boolean stored = false; //Flag to see if we've had a match
                    //Check to see if url is for a page first
                    for (Pattern pattern : pageExtensionPatterns) {
                        //Check for query string first
                        if (fullPath.contains("?")) {
                            fullPath = getBaseURL(fullPath);
                            if (isDuplicateURL(fullPath)) {
                                queriedURLs.put(fullPath, new Integer(queriedURLs.get(fullPath).intValue() + 1));
                                stored = true;
                            }
                            else {
                                queriedURLs.put(fullPath, new Integer(1));
                            }
                        }
                        Matcher matcher = pattern.matcher(fullPath.toLowerCase());
                        if (matcher.find() && !stored) {
                            urls.add(new URL(fullPath, matcher.group(), Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
                            pageURLs.add(new URL(fullPath, matcher.group(), Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
                            sumPages++;
                            stored = true;
                        }
                    }
                    //If the url wasn't a page check to see if url is for a document
                    if (!stored) {
                        for (Pattern pattern : documentExtensionPatterns) {
                            Matcher matcher = pattern.matcher(fullPath.toLowerCase());
                            if (matcher.find()) {
                                urls.add(new URL(fullPath, matcher.group(), Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
                                documentURLs.add(new URL(fullPath, matcher.group(), Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
                                sumDocuments++;
                                stored = true;
                            }
                        }
                    }                    
                    //If the url wasn't a page or document heck to see if url is for an image
                    if (!stored) {
                        for (Pattern pattern : imageExtensionPatterns) {
                            Matcher matcher = pattern.matcher(fullPath.toLowerCase());
                            if (matcher.find()) {
                                urls.add(new URL(fullPath, matcher.group(), Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));
                                imageURLs.add(new URL(fullPath, matcher.group(), Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));

                                sumImages++;
                                stored = true;
                            }
                        }
                    }
                    //If the url didn't contain any of the extensions we are checking for check to see if it
                    if (!stored) {
                        String extension = "other";
                        urls.add(new URL(fullPath, extension,  Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
                        pageURLs.add(new URL(fullPath, extension, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
                        sumPages++;
                    }
                    
                }
                System.out.println("Base element :" + doc.getDocumentElement().getNodeName());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        sortUrls();
        
    }
    /**
     * Get the base url from one with a query string
     * @param url
     * @return 
     */
    public String getBaseURL(String url) {
        int queryIndex = url.indexOf("?");
        return url.substring(0, queryIndex);
    }
    /**
     * Check the list of urls for a duplicate
     * @param baseURL
     * @return 
     */
    public boolean isDuplicateURL(String baseURL) {
        for (URL url : urls) {
            if (url.getURL().equals(baseURL)) {
                System.out.println(baseURL);
                return true;
            }
        }
        return false;
    }
    /**
     * Sort all the lists
     */
    public void sortUrls() {
        Collections.sort(urls);
        Collections.sort(pageURLs);
        Collections.sort(documentURLs);
        Collections.sort(imageURLs);
    }
    /**
     * Join a list of Strings as one String separated by a given delimeter
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
     * @param extensions
     * @param delimeter
     * @return 
     */
    public ArrayList<String> split(String extensions, String delimeter) {
        String[] temp = extensions.split(delimeter);
        for (String s : temp) {
            System.out.println(s);
        }
    
        return new ArrayList<String>(Arrays.asList(temp));
    }
    /**
     * Sum up the counts of the different types of urls
     * @return 
     */
    public int calculateResults() {
        return sumTotal = sumPages + sumDocuments + sumImages;
    }
    /**
     * Reset url counts so analysis can be ran more than once
     */
    public void resetCounts() {
        sumPages = 0;
        sumDocuments = 0;
        sumImages = 0;
        sumOtherItems = 0;
        sumTotal = 0;
        sumVideos = 0;
        sumRSSFeeds = 0;
    }
    /**
     * Build a String with the results to be displayed
     * @return 
     */
    public String printResults() {
        
        String output = "";
        
        output += ("Number of pages: " + sumPages + "\n");
        output +=("Number of documents: " + sumDocuments + "\n");
        output +=("Number of images: " + sumImages + "\n");
        //output +=("Number of videos: " + sumVideos + "\n");
        //output +=("Number of rss feeds: " + sumRSSFeeds + "\n");
        //output +=("Number of dynamic paths: " + queryStrings + "\n");
        output +=("Total number of elements: " + calculateResults());
        
        return output;
    }
    
    public String createReport() {
        String output = "";
        
        
        
        
        return output;
    }
}
