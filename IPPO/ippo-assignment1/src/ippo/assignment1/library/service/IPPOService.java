// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.service;

import ippo.assignment1.library.Picture;
import ippo.assignment1.library.utils.Properties;

import java.io.*;
import java.net.URL;

import javafx.scene.image.Image;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A service for the PictureViewer application which retrieves pictures from the service
 * on <a href="§MUNROURL">
 * the IPPO website</a>.
 * <p>The following properties are supported:
 * <ul>
 * <li><code>service.debug</code> - <code>true</code> or <code>false</code> to enable/disable debugging.</li>
 * </ul>
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
public class IPPOService implements Service {

	// the URL for the IJP API
	private static final String serviceURLString = "http://35.178.204.69/munros";
	// the required image index
	private int currentIndex;
	// the required subject
	private String requiredsubject;
	// the URL of the image 
    private String photoURL;
    // enable debugging
    private boolean debugging = false;
    
	/**
	 * Create a service object to return pictures from the IPPO website.
	 */
    public IPPOService() {
    	this.debugging = Properties.getBool("service.debug");
    }

	/**
	 * Return the service name.
	 *
	 * @return the name of the service.
	 *
	 */
    public String serviceName() {
    	return "IPPO Service";
    }
    
    // Handles events during the SAX parsing of the XML from the REST request
    private class IJPHandler extends DefaultHandler {

    	// sloppy match for subjects
    	private Boolean subjectMatch(String a, String b) {
    		String a2 = a.toLowerCase().replaceAll(" ","").replaceAll("'","");
    		String b2 = b.toLowerCase().replaceAll(" ","").replaceAll("'","");
    		return a2.equals(b2);
    	}
    	
    	// we are only interested in element start events
    	// stubs for the other events are provided by the default handler
	    public void startElement(
	    		String uri,
	    		String localName,
	    		String qName,
	    		Attributes attributes) {
	    	
    		if (qName.equals("a")) {
    			String title = attributes.getValue("title");
    			if (subjectMatch(title,requiredsubject) && --currentIndex==0) {
        			photoURL = attributes.getValue("href");
        			photoURL = serviceURLString + "/images/" + photoURL.replaceFirst(".*/", "");
    			}
      		}
      	}
    }
	    
    // Handle errors during the SAX parsing	    
    private class IJPErrorHandler implements ErrorHandler {

    	public void	error(SAXParseException ex) {
    		System.err.println("[error] IPPOService: SAX error: " + ex);
    	}
	    public void	fatalError(SAXParseException ex) {
	    	System.err.println("[error] IPPOService SAX fatal error: " + ex);
	    }
	    public void	warning(SAXParseException ex) {
	    	System.err.println("[error] IPPOService SAX warning: " + ex);
	    }
    }

    // return errors as a status message in a photo
    private Picture badPicture(String msg, String searchText, int index) {
    	String errorMessage = "IPPOService failed to retrieve photo " + index
    			+ " for search string: " + searchText + "\n- " + msg;
    	return new Picture(errorMessage);
    }

    /**
    * Return a picture object from thr IPPO website. If there
    * is more than one match, the <code>index</code>'th matching picture
    * is returned. <p> If there is no matching image, or <code>index</code> is greater
    * than the number of matching images, or there is any other kind of error,
    * then the returned Picture object will contain the error message.
    *
    * @param subject the free-text subject string
    * @param index the index of the matching picture to return
    * @return the requested picture
    */
    public Picture getPicture(String subject, int index) {

     	try {

    		// initialise the URL
    		// this instance variable is filled in by the SAX tree walker
    		// when it encounters an appropriate node.
    		this.photoURL = null;

    		String urlString = serviceURLString + "/index.html";
 
    		if (debugging) {
    			System.err.println("[debug] IPPOService: fetching: "+urlString);
    		}
	            
    		// this handles the SAX parse events
    		IJPHandler handler = new IJPHandler();
    		IJPErrorHandler errorHandler = new IJPErrorHandler();
    		
    		try {
	            // set the required index & subject
    			currentIndex = index;
    	    	requiredsubject = subject;
   			
    			// extract the photo url from the metadata
    			// from: http://bit.ly/lvDqMB
    			InputStream stream = new URL(urlString).openStream();
      			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    			SAXParser parser = parserFactory.newSAXParser();
    			XMLReader myReader = parser.getXMLReader();
    			myReader.setContentHandler(handler);
    			myReader.setErrorHandler(errorHandler);
    			myReader.parse(new InputSource(stream));

    			// something went wrong - we never saw any photo info ...
    			if (photoURL == null) {
    	    		return new Picture("IPPOService has no picture #"+index+" of \""+subject+"\"");
    			}
	            		
    		} catch (SAXException | ParserConfigurationException ex) {
    			return badPicture(ex.getMessage(), subject, index);
    		}

    		if (debugging) {
    			System.err.println("[debug] IPPOService: fetching photo: " + photoURL);
    		}

    	} catch (IOException ex) {
    		return badPicture(ex.getMessage(), subject, index);
    	}

     	return new Picture(new Image(photoURL),subject,serviceName(),index);
     }
}
