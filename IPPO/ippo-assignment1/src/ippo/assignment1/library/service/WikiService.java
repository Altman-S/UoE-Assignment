// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.service;

import ippo.assignment1.library.Picture;
import ippo.assignment1.library.utils.Properties;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A service for the PictureViewer application which retrieves pictures from
 * <a href="https://www.wikipedia.com">Wikipedia</a>.
 * <p>Source code for WikiService.java is included in the assignment zip file.
 * No custom error handler is needed as the API functions perfectly
 * 
 * @author  Ross McKenzie &lt;r.m.mckenzie@ed.ac.uk&gt; and Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version 1.0.0, 01 Sep 2017
 */
public class WikiService implements Service {

	// the URL to request the images from a wiki page
	private static final String wikiURLString = "https://en.wikipedia.org/w/api.php";
	
	private String photoURL;
	private String photoName;
    // enable debugging
    private boolean debugging = false;
    
    private final String action = "query";
    private final String format = "xml";
    private final String requestInfo = "pageimages";
    private final String requestPhoto = "imageinfo";
    private final String info = "url";
    
	/**
	 * Create a Wiki service object.
	 */
    public WikiService() {
    	this.debugging = Properties.getBool("service.debug");
    }

	/**
	 * Return the service name.
	 *
	 * @return the name of the service ("Wikipedia Service").
	 *
	 */
    public String serviceName() {
    	return "Wikipedia Service";
    }
    
    // Handles events during the SAX parsing of the XML from the REST request
    private class WikiHandler extends DefaultHandler {

    	// we are only interested in element start events
    	// stubs for the other events are provided by the default handler
	    public void startElement(
	    		String uri,
	    		String localName,
	    		String qName,
	    		Attributes attributes) {
	   
    		if (debugging) {
    			System.err.println("[debug] WikiService: element: "+qName);
    		}

    		// for the first request we need the pageimage from the page element
    		if (qName.equals("page")) {

    			// extract the relevant attributes from the photo
    			photoName = "File:"+attributes.getValue("pageimage");
    		}
    		// for the second request we need the url from the imageinfo
    		// image properties are contained within the ii element
    		if (qName.equals("ii")) {

    			// extract the relevant attributes from the photo
    			photoURL = attributes.getValue("url");
    		}
    	}
    }
	    

    // return errors as a status message in a photo
    private Picture badPicture(String msg, String searchText, int index) {
    	String errorMessage = "WikiService failed to retrieve photo " + index
    			+ " for search string: " + searchText + "\n- " + msg;
    	return new Picture(errorMessage);
    }

    /**
    * Return a picture object from a free-text search of Wikipedia. Normally, there
    * will only be one image.
    * <p> If there is no matching image, or <code>index</code> is greater
    * than the 1, or there is any other kind of error,
    * then the returned Picture object will contain the error message.
    *
    * @param subject the free-text subject string
    * @param index the index of the matching picture to return
    * @return the requested picture
    */
    public Picture getPicture(String subject, int index) {

    	// the MediaWiki API is described here: https://www.mediawiki.org/wiki/API:Page_info_in_search_results

    	// the image of the downloaded photo
    	BufferedImage bufferedImage;

    	try {

    		// initialise the URL
    		// this instance variable is filled in by the SAX tree walker
    		// when it encounters an appropriate node.
    		this.photoName = null;
    		this.photoURL = null;

    		// based on code from here: http://bit.ly/M6Ee6N
    		String urlInfo = wikiURLString
    				+ "?action=" + this.action
    				+ "&format=" + this.format
    				+ "&prop=" + this.requestInfo
    				+ "&titles=" + URLEncoder.encode(subject, StandardCharsets.UTF_8);

    		if (debugging) {
    			System.err.println("[debug] WikiService: fetching: "+urlInfo);
    		}
	            
    		// this handles the SAX parse events
    		WikiHandler handler = new WikiHandler();
	            	
    		try {
	            		
    			// extract the photo name from api return
    			// from: http://bit.ly/lvDqMB
    			InputStream stream1 = new URL(urlInfo).openStream();
    			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    			SAXParser parser = parserFactory.newSAXParser();
    			XMLReader myReader = parser.getXMLReader();
    			myReader.setContentHandler(handler);
    			myReader.parse(new InputSource(stream1));
    			
    			// second request needed to extract photo url
    			String urlPhoto = wikiURLString
        				+ "?action=" + this.action
        				+ "&format=" + this.format
        				+ "&prop=" + this.requestPhoto
        				+ "&titles=" + URLEncoder.encode(this.photoName, StandardCharsets.UTF_8)
        				+ "&iiprop=" + this.info;
    			
    			InputStream stream2 = new URL(urlPhoto).openStream();
    			myReader.parse(new InputSource(stream2));
    			// something went wrong - we never saw any photo info ...
    			if (photoURL == null || index != 1) {
    	    		return new Picture("WikiService has no picture #"+index+" of \""+subject+"\"");
    			}
	            		
    		} catch (SAXException | ParserConfigurationException ex) {
    			return badPicture(ex.getMessage(), subject, index);
    		}
	   
    		if (debugging) {
    			System.err.println("[debug] WikiService: fetching photo: " + photoURL);
    		}

    		// go fetch the image itself
    		bufferedImage = ImageIO.read(new URL(photoURL).openStream());

    	} catch (IOException ex) {
    		return badPicture(ex.getMessage(), subject, index);
    	}

    	// this is the code that all the other services use to fetch their images.
    	// for some reason or other, this produces blank images from Wiki
    	// >>>> return new Picture(new Image(photoURL.toString()),subject,serviceName(),index);
    	// so we do the messing around above to create a bufferedImage & that seems to work
    	
    	return new Picture(bufferedImage,subject,serviceName(),index);
    }
}
