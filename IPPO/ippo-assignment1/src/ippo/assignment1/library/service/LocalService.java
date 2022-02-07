// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.service;

import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import ippo.assignment1.library.utils.HashMap;
import ippo.assignment1.library.Picture;
import ippo.assignment1.library.utils.Properties;

/**
 * A service for the PictureViewer application which retrieves pictures from
 * a local directory.
 * <p>The following properties are supported:<ul>
 * <li><code>service.debug</code> - <code>true</code> or <code>false</code> to enable/disable debugging.</li>
 * <li><code>service.local.path</code> - a colon-separated list of locations containing images.</li></ul>
 * <p>Locations may be fully qualified pathnames which refer to directories in the local filesystem.
 * Other locations are relative to the CLASSPATH.
 * In particular, the location <code>images</code> contains some builtin images.
 * <p>Each location must contain an <code>index.txt</code> file.
 * Each line of the index file specifies the filename and description for one image (colon separated).
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
public class LocalService implements Service {

	static String indexFile = "index.txt";
	private final HashMap<String,URL> urlBySubject = new HashMap<String,URL>();
    private boolean debugging = false;
    
	/**
	 * Create a service object to return pictures from a local directory.
	 */
    public LocalService() {
    	init(Properties.get("service.local.path"));
    }
    
	/**
	 * Create a service object to return pictures from a local directory.
	 * 
	 * @param path a colon-separated list of picture directories.
	 */
    public LocalService(String path) {
    	init(path);
     }
    
    private void init(String path) {
    	debugging = Properties.getBool("service.debug");
		String[] picturePath = path.split(":");
		for (String location : picturePath) {
			URL indexURL = null;
			if (location.startsWith("/")) {
				try {
					indexURL = new URL("file:" + location.trim() + "/" + indexFile);
				} catch (MalformedURLException ex) {
	    			System.err.println("[error] LocalService: bad URL in path: file:" + location.trim());
	    			System.exit(1);
				}
				ReadIndex(indexURL);
			} else if (!location.equals("")){
				indexURL = this.getClass().getResource("/"+location.trim()+"/"+indexFile);
				if (indexURL==null) {
	    			System.err.println("[error] LocalService: can't read "+indexFile+"in location: " + location.trim());
	    			System.exit(1);
				}
				ReadIndex(indexURL);
			}
		}   	
    }
    
    private void ReadIndex(URL indexURL) {
        if (debugging) {
        	System.err.println("[debug] LocalService: picture directory \""+indexURL.toString()+"\"");
        }
        try {
        	BufferedReader in = new BufferedReader(new InputStreamReader(indexURL.openStream()));
        	String line;
        	while ((line = in.readLine()) != null) {
        		String[] fields = line.trim().split(":");
         		if (fields.length>0 && !fields[0].startsWith("#") && fields[0].length()>0) {
               		String filename = fields[0].trim();
            		String subject = (fields.length>1) ? fields[1].trim() : filename;
            		String urlString = indexURL.toString().replaceFirst(indexFile,filename);
                    URL pictureURL = null;
    				try {
    					pictureURL = new URL(urlString);
    				} catch (MalformedURLException ex) {
    	    			System.err.println("[error] LocalService: bad image url: " + urlString);
    	    			System.exit(1);
    				}
    				// add an index to the picture
     		        int i=1;
    		        while (urlBySubject.get(subject+":"+i) != null) { ++i; }
    		        urlBySubject.put(subject+":"+i,pictureURL);
       		        if (debugging) {
    		        	System.err.println("[debug] picture: \""+subject+":"+i+"\" = "+ pictureURL);
    		        }
          		}
         	}
        	in.close();
         } catch(IOException ex) {
			System.err.println("[error] LocalService: can't read index: " + indexURL);
			System.exit(1);
        }
    }

	/**
	 * Return the service name.
	 *
	 * @return the name of the service ("Local Service").
	 *
	 */
    public String serviceName() {
    	return "Local Service";
    }
    
    /**
    * Return a picture object from a local directory. If there
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

    	URL pictureURL = urlBySubject.get(subject+":"+index);
    	if (pictureURL==null) {
    		return new Picture("LocalService has no picture #"+index+" of \""+subject+"\"");
    	}
     	return new Picture(new Image(pictureURL.toString()),subject,serviceName(),index);
    }
}
