package xmlsitereader;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 *
 * @author Curtis Conner * Company: Eagle Creek Software
 * Services * Date: 7-30-2014
 */
public class URLExtension implements Serializable {
    private String extension;
    private URLType urlType;
    private Pattern pattern;
    /**
     * 
     */
    public URLExtension(){}
    /**
     * 
     * @param extension
     * @param urlType 
     */
    public URLExtension(String extension, URLType urlType) {
        this.extension = extension;
        this.urlType = urlType;
        this.pattern = Pattern.compile(extension + "$");
    }
    /**
     * 
     * @param extension 
     */
    public void setExtension(String extension) {
        this.extension = extension;
        this.pattern = Pattern.compile(extension + "$");
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
     * @return 
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
     * 
     * @return 
     */
    public Pattern getPattern() {
        return pattern;
    }
    /**
     * 
     * @param urlExtension
     * @return 
     */
    public boolean equals(URLExtension urlExtension) {
        return this.extension.equals(urlExtension.getExtension());
    }
}
