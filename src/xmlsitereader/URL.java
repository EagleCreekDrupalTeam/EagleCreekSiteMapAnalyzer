package xmlsitereader;
/**
 *
 ** @author Stephen Paden and Curtis Conner
 ** Company: Eagle Creek Software Services 
 ** Date: 2/27/2014
 *
 */
public class URL implements Comparable {
    private String urlPath;
    private String extension;
    private Boolean isPage;
    private Boolean isDocument;
    private Boolean isImage;

    public URL(){}
    // "watch his every move, super constructor!" -- Rush
    public URL(String urlPath, String extension, Boolean isPage, Boolean isDocument, Boolean isImage) {        
        this.urlPath = urlPath;
        this.extension = extension;
        this.isPage = isPage;
        this.isDocument = isDocument;
        this.isImage = isImage;
    }
    
    // get & set
    public void setURL(String urlPath) { 
        this.urlPath =  urlPath; 
    }
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public void setIsPage(Boolean page) { 
        this.isPage = page; 
    }    
    public void setIsDocument(Boolean document) { 
        this.isDocument = document; 
    }    
    public void setIsImage(Boolean image) { 
        this.isImage = image; 
    }    
    public String getURL() { 
        return urlPath; 
    }
    public String getExtension() {
        return extension;
    }
    public Boolean isPage() { 
        return isPage; 
    }    
    public Boolean isDocument() { 
        return isDocument; 
    }    
    public Boolean isImage() { 
        return isImage; 
    }
    @Override
    public int compareTo(Object obj) {
        URL url1 = (URL)obj;       
        return this.getExtension().compareTo(url1.getExtension());
    }
    

}
