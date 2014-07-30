package xmlsitereader;

import java.util.regex.Pattern;

/**
 *
 * @author cconner
 */
public class URLExtension {
    private String extension;
    private URLType urlType;
    private Pattern pattern;
    
    public URLExtension(){}
    
    public URLExtension(String extension, URLType urlType) {
        this.extension = extension;
        this.urlType = urlType;
        this.pattern = Pattern.compile(extension + "$");
    }
    
    public void setExtension(String extension) {
        this.extension = extension;
        this.pattern = Pattern.compile(extension + "$");
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
    
    public Pattern getPattern() {
        return pattern;
    }
}
