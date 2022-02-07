// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.service;

import ippo.assignment1.library.Picture;

/**
 * A service for the PictureViewer application.
 * A services provides pictures for a given subject.
 * The service may be a real service, or a <i>Proxy</i> service 
 * which modifies some other underlying service (for example
 * providing a cache layer).
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
public interface Service {

	/**
	 * Return a textual name to identify the service.
	 *
	 * @return the name of the service
	 */
    String serviceName();
	
	/**
	 * Return a picture from the service.
	 *
	 * @param subject the requested subject
	 * @param index the index of the picture within all pictures for the requested subject
	 *
	 * @return the picture
	 */
    Picture getPicture(String subject, int index);
}
