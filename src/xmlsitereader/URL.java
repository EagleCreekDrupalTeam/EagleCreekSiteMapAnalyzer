package xmlsitereader;
/**
 *
 ** @author Curtis Conner and Steve Paden
 ** Company: Eagle Creek Software Services 
 ** Date: 2/27/2014
 *
 */
public class URL implements Comparable {
    private String urlPath;
    private String extension;
    private Boolean isPage;
    private Boolean isDocument;
    private Boolean isMedia;
    private URLType urlType;

    /**
     * Default empty constructor
     */
    public URL(){}
    /**
     * Overridden constructor
     * @param urlPath
     * @param extension
     * @param isPage
     * @param isDocument
     * @param isImage 
     */
    public URL(String urlPath, String extension, Boolean isPage, Boolean isDocument, Boolean isImage) {        
        this.urlPath = urlPath;
        this.extension = extension;
        this.isPage = isPage;
        this.isDocument = isDocument;
        this.isMedia = isImage;
    }
    /**
     * New constructor for refactoring
     * @param urlPath
     * @param extension
     * @param urlType 
     */
    public URL(String urlPath, String extension, URLType urlType) {
        this.urlPath = urlPath;
        this.extension = extension;
        this.urlType = urlType;
    }
    
    /**
     * 
     * @param urlPath 
     */
    public void setURL(String urlPath) { 
        this.urlPath =  urlPath; 
    }
    /**
     * 
     * @param extension 
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }
    /**
     * 
     * @param page 
     */
    public void setIsPage(Boolean page) { 
        this.isPage = page; 
    }    
    /**
     * 
     * @param document 
     */
    public void setIsDocument(Boolean document) { 
        this.isDocument = document; 
    }    
    /**
     * 
     * @param image 
     */
    public void setIsMedia(Boolean image) { 
        this.isMedia = image; 
    }
    /**
     * New for refactoring 
     * @param urlType 
     */
    public void setURLType(URLType urlType) {
        this.urlType = urlType;
    }
    
    /**
     * 
     * @return urlPath
     */
    public String getURL() { 
        return urlPath; 
    }
    /**
     * 
     * @return extension
     */
    public String getExtension() {
        return extension;
    }    
    /**
     * 
     * @return boolean
     */
    public Boolean isPage() { 
        return isPage; 
    } 
    /**
     * 
     * @return boolean
     */
    public Boolean isDocument() { 
        return isDocument; 
    }  
    /**
     * 
     * @return boolean
     */
    public Boolean isMedia() { 
        return isMedia; 
    }
    /**
     * New for refactoring
     * @return 
     */
    public URLType getURLType() {
        return urlType;
    }
    
    /**
     * Compares the extensions to sort alphabetically
     * @param obj
     * @return Object
     */
    @Override
    public int compareTo(Object obj) {
        URL url1 = (URL)obj;       
        return this.getExtension().compareTo(url1.getExtension());
    }
    /**
     * Overrides the default toString().
     * This overridden method is used in createReport().
     * @return String 
     */
    @Override
    public String toString() {        
        String output = (getURL() + "\n"); 
        return output;
    }  

}
