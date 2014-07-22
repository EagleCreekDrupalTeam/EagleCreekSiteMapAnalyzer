package xmlsitereader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 ** @author Curtis Conner and Stephen Paden * Company: Eagle Creek Software
 * Services * Date: 2/26/2014
 *
 */
public class XML {

    private static int sumPages = 0;
    private static int sumDocuments = 0;
    private static int sumMedia = 0;
    private static int sumTotal = 0;
    private String fileName;
    private File file;
    private ArrayList<URL> urls;
    private ArrayList<URL> documentURLs;
    private ArrayList<URL> pageURLs;
    private ArrayList<URL> mediaURLs;
    private String[] fullPaths;
    
    private ArrayList<String> defaultDocumentExtensions = new ArrayList(Arrays.asList(".docx", ".doc", ".dot", ".pdf", ".txt", ".rft", ".odt",".odg", ".csv", ".xls", ".xlsx", ".xlt", ".ppt", ".pptx"));
    private ArrayList<String> defaultPageExtensions = new ArrayList(Arrays.asList(".html", ".htm", ".aspx", ".jsp", ".php", ".asp", ".shtml"));
    private ArrayList<String> defaultMediaExtensions = new ArrayList(Arrays.asList(".gif", ".jpg", ".png", ".jpeg", ".bmp", ".ico"));

    private ArrayList<String> updatedDocumentExtensions = new ArrayList();
    private ArrayList<String> updatedPageExtensions = new ArrayList();
    private ArrayList<String> updatedMediaExtensions = new ArrayList();

    private ArrayList<Pattern> documentExtensionPatterns = new ArrayList<Pattern>();
    private ArrayList<Pattern> pageExtensionPatterns = new ArrayList<Pattern>();
    private ArrayList<Pattern> mediaExtensionPatterns = new ArrayList<Pattern>();

    private HashMap<String, Integer> queriedURLs = new HashMap<String, Integer>();

    public XML() {
        //Initialize the pattern lists from the default extension lists 
        buildPatterns(documentExtensionPatterns, defaultDocumentExtensions);
        buildPatterns(pageExtensionPatterns, defaultPageExtensions);
        buildPatterns(mediaExtensionPatterns, defaultMediaExtensions);
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

    public URL[] getMediaURLs() {
        return mediaURLs.toArray(new URL[mediaURLs.size()]);
    }
/**
 * Takes user supplied list of extensions 
 * and updates the list of patterns for Pages
 * @param extensions 
 */
    public void setPageExtensions(String extensions) {
        this.updatedPageExtensions = split(extensions, ",");
        pageExtensionPatterns.clear();
        buildPatterns(pageExtensionPatterns, updatedPageExtensions);
    }
/**
 * Takes user supplied list of extensions
 * and updates the list of patterns for Documents
 * @param extensions 
 */
    public void setDocumentExtensions(String extensions) {
        this.updatedDocumentExtensions = new ArrayList(split(extensions, ","));
        documentExtensionPatterns.clear();
        buildPatterns(documentExtensionPatterns, updatedDocumentExtensions);
    }
/**
 * Takes user supplied list of extensions
 * and updates the list of patterns for Media
 * @param extensions 
 */
    public void setMediaExtensions(String extensions) {
        this.updatedMediaExtensions = split(extensions, ",");
        mediaExtensionPatterns.clear();
        buildPatterns(mediaExtensionPatterns, updatedMediaExtensions);
    }
/**
 * Resets list patterns for Pages to default
 */
    public void resetPageExtensions() {
        pageExtensionPatterns.clear();
        buildPatterns(pageExtensionPatterns, defaultPageExtensions);
    }
/**
 * Resets list of patterns for Documents to default
 */
    public void resetDocumentExtensions() {
        documentExtensionPatterns.clear();
        buildPatterns(documentExtensionPatterns, defaultDocumentExtensions);
    }
/**
 * Resets list of patterns for Media to default
 */
    public void resetediaExtensions() {
        mediaExtensionPatterns.clear();
        buildPatterns(mediaExtensionPatterns, defaultMediaExtensions);
    }
/**
 * Returns a String representation of the list of patterns for Pages
 * @return 
 */
    public String getPageExtensions() {
        return join(updatedPageExtensions, ",");
    }
/**
 * Returns a String representation of the list of patterns for Documents
 * @return 
 */
    public String getDocumentExtensions() {
        return join(updatedDocumentExtensions, ",");
    }
/**
 * Returns a String representation of the list of patterns for Media
 * @return 
 */
    public String getMediaExtensions() {
        return join(updatedMediaExtensions, ",");
    }
/**
 * Returns a String representation of the default list of patterns for Pages
 * @return 
 */
    public String getDefaultPageExtensions() {
        return join(defaultPageExtensions, ",");
    }
/**
 * Returns a String representation of the default list of patterns for Documents
 * @return 
 */
    public String getDefaultDocumentExtensions() {
        return join(defaultDocumentExtensions, ",");
    }
/**
 * Returns a String representation of the default list of patterns for Media
 * @return 
 */
    public String getDefaultMediaExtensions() {
        return join(defaultMediaExtensions, ",");
    }

    /**
     * Builds list of Patterns from the list of Strings
     *
     * @param patterns
     * @param extensions
     */
    public void buildPatterns(ArrayList<Pattern> patterns, ArrayList<String> extensions) {
        
        for (String extension : extensions) {
            patterns.add(Pattern.compile(extension + "$"));
        }                        
    }

    /**
     * Parses through the xml sitemap to build the lists of urls
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
                    fullPaths[i] = newNode.getTextContent();
                }

                urls = new ArrayList<>();
                documentURLs = new ArrayList<>();
                pageURLs = new ArrayList<>();
                mediaURLs = new ArrayList<>();

                for (int i = 0; i < fullPaths.length; i++) {
                    String fullPath = fullPaths[i].toLowerCase();
                    boolean stored = false; //Flag to see if we've had a match
                    //Check to see if url is for a page first
                    for (Pattern pattern : pageExtensionPatterns) {
                        //Check for query string first
                        if (fullPath.contains("?")) {
                            fullPath = getBaseURL(fullPath);
                            //Check for a duplicate of the base url
/* Storing each base url used for a query string and the number of times each
   is used in a query, but data is currently not being used.
*/                            
                            if (isDuplicateURL(fullPath)) {
                                //Check the hashmap to see if the base url is in there
                                if (queriedURLs.get(fullPath) == null) {
                                    queriedURLs.put(fullPath, new Integer(1));
                                }
                                else {
                                    queriedURLs.put(fullPath, new Integer(queriedURLs.get(fullPath).intValue() + 1));
                                }
                                stored = true;
                            } else {
                                queriedURLs.put(fullPath, new Integer(1));
                            }
                        }
                        Matcher matcher = pattern.matcher(fullPath);
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
                            Matcher matcher = pattern.matcher(fullPath);
                            if (matcher.find()) {
                                urls.add(new URL(fullPath, matcher.group(), Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
                                documentURLs.add(new URL(fullPath, matcher.group(), Boolean.FALSE, Boolean.TRUE, Boolean.FALSE));
                                sumDocuments++;
                                stored = true;
                            }
                        }
                    }
                    //If the url wasn't a page or document check to see if url is for a media
                    if (!stored) {
                        for (Pattern pattern : mediaExtensionPatterns) {
                            Matcher matcher = pattern.matcher(fullPath);
                            if (matcher.find()) {
                                urls.add(new URL(fullPath, matcher.group(), Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));
                                mediaURLs.add(new URL(fullPath, matcher.group(), Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));

                                sumMedia++;
                                stored = true;
                            }
                        }
                    }
                    //If the url didn't contain any of the extensions we are checking for 
                    //set extension to other and count as a page
                    if (!stored) {
                        String extension = "other";
                        urls.add(new URL(fullPath, extension, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
                        pageURLs.add(new URL(fullPath, extension, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
                        sumPages++;
                    }

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        sortUrls();

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

    /**
     * Sort all the lists
     */
    public void sortUrls() {
        Collections.sort(urls);
        Collections.sort(pageURLs);
        Collections.sort(documentURLs);
        Collections.sort(mediaURLs);
    }

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
     * Sum up the counts of the different types of urls
     *
     * @return
     */
    public int calculateResults() {
        return sumTotal = sumPages + sumDocuments + sumMedia;
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
        StringBuilder builder = new StringBuilder();
        builder.append("Number of pages: ").append(sumPages).append("\n");
        builder.append("Number of documents: ").append(sumDocuments).append("\n");
        builder.append("Number of media: ").append(sumMedia).append("\n");
        builder.append("Total number of elements: ").append(calculateResults());

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
            newPrintWriter.write(pageURLs.toString().replace(",", "")
                    .replace("[", "")
                    .replace("]", "") + "\n");
            newPrintWriter.write("");
            newPrintWriter.write("********************************************\n");
            newPrintWriter.write("Document URLS: \n");
            newPrintWriter.write("********************************************\n");
            newPrintWriter.write(documentURLs.toString().replace(",", "")
                    .replace("[", "")
                    .replace("]", "") + "\n");
            newPrintWriter.write("");
            newPrintWriter.write("********************************************\n");
            newPrintWriter.write("Media URLs: \n");
            newPrintWriter.write("********************************************\n");
            newPrintWriter.write(mediaURLs.toString().replace(",", "")
                    .replace("[", "")
                    .replace("]", "") + "\n");
            newPrintWriter.write("********************************************\n");
            newPrintWriter.write("Summary: \n");
            newPrintWriter.write("********************************************\n");            
            newPrintWriter.close();

        } catch (SecurityException se) {
            System.out.println(se);
        }
    }
}
