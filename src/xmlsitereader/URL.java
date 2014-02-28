package xmlsitereader;
/**
 *
 ** @author Stephen Paden and Curtis Conner
 ** Company: Eagle Creek Software Services 
 ** Date: 2/27/2014
 *
 */
public class URL {
    private String urlPath;
    private Boolean isPage;
    private Boolean isDocument;
    private Boolean isImage;

    public URL(){}
    // "watch his every move, super constructor!" -- Rush
    public URL(String urlPath, Boolean isPage, Boolean isDocument, Boolean isImage) {        
        this.urlPath = urlPath;
        this.isPage = isPage;
        this.isDocument = isDocument;
        this.isImage = isImage;
    }
    
    // get & set
    public void setURLPath(String urlPath) { this.urlPath =  urlPath; }
    public void setIsPage(Boolean page) { this.isPage = page; }    
    public void setIsDocument(Boolean document) { this.isDocument = document; }    
    public void setIsImage(Boolean image) { this.isImage = image; }    
    public String getURL() { return urlPath; }    
    public Boolean isPage() { return isPage; }    
    public Boolean isDocument() { return isDocument; }    
    public Boolean isImage() { return isImage; }
    

}
