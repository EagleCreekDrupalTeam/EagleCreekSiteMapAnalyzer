package xmlsitereader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
/**
 *
 ** @author Stephen Paden and Curtis Conner
 ** Company: Eagle Creek Software Services 
 ** Date: 2/26/2014
 *
 */
public class XMLSiteReader {
    
    private XMLSiteReaderFrame readerFrame;
    
    public XMLSiteReader() {
        readerFrame = new XMLSiteReaderFrame();
    }
    

    public static void main(String[] args) throws FileNotFoundException {
        
        XMLSiteReader siteReader = new XMLSiteReader();
    }
    
    class XMLSiteReaderFrame extends JFrame {
        
        public static final String frameTitle = "Eagle Creek Sitemap Analyzer";
        public static final int frameHeight = 1300;
        public static final int frameWidth = 768;
        private JMenuBar menuBar;
        private JMenu fileMenu, optionsMenu, helpMenu;
        private JButton fileButton, analyzeButton;
        private XMLSiteReaderPanel panel; //Not sure if we need the custom Panel class
        private JPanel panel2;
        private JFileChooser chooser;
        private JTable table;
        private XML xml;
        
        
        
        public XMLSiteReaderFrame() {
            setTitle(frameTitle);  	
            setSize(frameWidth, frameWidth);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
            xml = new XML();
            menuBar = new JMenuBar();
            setJMenuBar(menuBar);
            fileMenu = new JMenu("File");
            menuBar.add(fileMenu);
            optionsMenu = new JMenu("Options");
            menuBar.add(optionsMenu);
            helpMenu = new JMenu("Help");
            menuBar.add(helpMenu);
            panel = new XMLSiteReaderPanel();
            GroupLayout layout = new GroupLayout(panel);
            panel.setLayout(layout);
            
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);
            
            fileButton = new JButton("Browse");
            fileButton.addActionListener(new BrowseButtonListener());
            analyzeButton = new JButton("Analyze");
            analyzeButton.setEnabled(false);
            analyzeButton.addActionListener(new AnalyzeButtonListener());
            
            panel.add(fileButton);
            panel.add(analyzeButton);
            chooser = new JFileChooser();
            
            
            add(panel);
            setTitle(frameTitle);  	
            setSize(frameWidth, frameWidth);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }
        
        private void buildTable() {
            URL[] urls = xml.getURLs();
            System.out.println("Number of URLs " + urls.length);
            
            String[] columnNames = {"URL", "Page", "Document", "Image"};
            Object[][] data = new Object[urls.length][columnNames.length];
            for (int i = 2; i < urls.length; i++) {
                for (int j = 0; j < columnNames.length; j++) {
                    switch (j) {
                        case 0:
                            System.out.println(urls[i].getURL());
                            data[i][j] = urls[i].getURL();
                            break;
                        case 1:
                            data[i][j] = urls[i].isPage();
                            System.out.println(urls[i].isPage());
                            break;
                        case 2:
                            data[i][j] = urls[i].isDocument();
                            System.out.println(urls[i].isDocument());
                            break;
                        case 3:
                            data[i][j] = urls[i].isImage();
                            System.out.println(urls[i].isImage());
                            break;
                        default:
                    }
                }
            }
            
            table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane);
            add(panel);
            panel.repaint();       
            
        }
        
        private class BrowseButtonListener implements ActionListener {
                @Override
		public void actionPerformed(ActionEvent event) {
                    
                    int returnOption = chooser.showOpenDialog(panel);
                    
                    switch(returnOption)  {
                        case JFileChooser.CANCEL_OPTION :
                            JOptionPane.showMessageDialog(null, "You must select a file to continue.", "Warning", JOptionPane.WARNING_MESSAGE);
                            analyzeButton.setEnabled(false);
                            break;
                        case JFileChooser.APPROVE_OPTION :                            
                            if (chooser.getTypeDescription(chooser.getSelectedFile()).equals("XML File")) {
                                xml.setFile(chooser.getSelectedFile());
                                try {
                                    xml.parseXML();
                                    analyzeButton.setEnabled(true);
                                }
                                catch (FileNotFoundException e) {
                                    System.out.println(e);
                                }
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "You must choose a .xml file.", "Wrong File Type", JOptionPane.INFORMATION_MESSAGE);
                                analyzeButton.setEnabled(false);
                            }                            
                            break;
                        case JFileChooser.ERROR_OPTION :
                            JOptionPane.showMessageDialog(null, "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
                            analyzeButton.setEnabled(false);
                            break;
                        default :
                            JOptionPane.showMessageDialog(null, "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
                            analyzeButton.setEnabled(false);
                    }                        
		}
	}
        
        private class AnalyzeButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    xml.parseXML();
                    buildTable();
                }catch(FileNotFoundException e) {
                    
                }
            }
        }        
    }
    
    class XMLSiteReaderPanel extends JPanel {
        
    }
}
