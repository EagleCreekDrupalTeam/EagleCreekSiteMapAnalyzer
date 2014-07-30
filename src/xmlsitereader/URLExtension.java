package xmlsitereader;

/**
 *
 * @author cconner
 */
public class URLExtension {
    private String extension;
    private URLType urlType;
    
    public URLExtension(){}
    
    public URLExtension(String extension, URLType urlType) {
        this.extension = extension;
        this.urlType = urlType;
    }
    
    public void setExtension(String extension) {
        this.extension = extension;
    }
    
    public void setURLType(URLType urlType) {
        this.urlType = urlType;
    }
    
    public String getExtension() {
        return extension;
    }
    
    public URLType getURLType() {
        return urlType;
    }
}
