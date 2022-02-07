// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.service;

import ippo.assignment1.library.Picture;
import ippo.assignment1.library.utils.Properties;

/**
 * A service for the PictureViewer application which uses the implementation
 * class specified in the property file.
 * <p>The following properties are supported:
 * <ul>
 * <li><code>service</code> - the (unqualified) classname of the implementation.</li>
 * <li><code>service.debug</code> - <code>true</code> or <code>false</code> to enable/disable debugging.</li>
 * </ul>
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
public class ServiceFromProperties implements Service {

	private Service delegate = null; 
	private Boolean debugging = false;
	
	/**
	 * Create an instance of a service which retrieves pictures from the service
	 * specified in the <code>service</code> property in the property file.
	 */
	public ServiceFromProperties() {
		createService("service");
	}
	
	/**
	 * Create an instance of a service which retrieves pictures from the service
	 * specified in named property in the property file.
	 * 
	 * @param propertyName the name of the property specifying the service class.
	 */
	public ServiceFromProperties(String propertyName) {
		createService(propertyName);
	}
	
	/**
	 * Create an instance of a service which uses the implementation class
	 * specified in the <code>Service</code> property in the property file.
	 */
	private void createService(String propertyName) {
		debugging = Properties.getBool("service.debug");
		delegate = (Service) Properties.getObject( propertyName,
			"ippo.assignment1.service:ippo.assignment1.proxy:" +
			"ippo.assignment1.library.service:ippo.assignment1.library.proxy" );
        if (debugging) {
        	System.err.println("[debug] Service: created " + delegate.getClass().getName());
        }
	}

	/**
	 * Return a picture from the service.
	 *
	 * @param subject the requested subject
	 * @param index the index of the picture within all pictures for the requested topic
	 *
	 * @return the picture
	 */
	public Picture getPicture(String subject, int index) {
	    if (debugging) {
	     	System.err.println("[debug] Service: get picture \""+subject+"\" ("+index+")");
	    }
		return delegate.getPicture(subject, index);
	}

	/**
	 * Return a textual name for the service.
	 *
	 * @return the name of the service
	 */
	public String serviceName() {
		String serviceName = delegate.serviceName();
	    if (debugging) {
	     	System.err.println("[debug] Service: name = \""+serviceName+"\"");
	    }
		return serviceName;
	}
}
