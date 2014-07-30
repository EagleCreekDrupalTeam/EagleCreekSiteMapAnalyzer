package xmlsitereader;
/**
 *
 ** @author Curtis Conner and Steve Paden
 ** Company: Eagle Creek Software Services 
 ** Date: 2/27/2014
 * Re-factored by Curtis Conner 7-30-2014
 *
 */
public class URL implements Comparable {
    private String urlPath;
    private String extension;
    private URLType urlType;

    /**
     * Default empty constructor
     */
    public URL(){}

    /**
     * Constructor
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
