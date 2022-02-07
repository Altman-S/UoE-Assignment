// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.proxy;

import ippo.assignment1.library.Picture;
import ippo.assignment1.library.service.Service;
import ippo.assignment1.library.utils.Properties;

import java.util.ArrayList;
import java.util.Random;

/**
 * A proxy service for the PictureViewer application which obtains pictures from a random base service.
 * <p>The following properties are supported:
 * <ul>
 * <li><code>proxy.random.services</code> - a comma-separated list of (unqualified)
 * service names for the base services to be used when this is not specified in the constructor.</li>
 * <li><code>proxy.debug</code> - <code>true</code> or <code>false</code> to enable/disable debugging.</li>
 * </ul>
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
public class RandomProxy implements Service {

	private final ArrayList<Service> baseServices = new ArrayList<Service>();
	private final Random rand = new Random();
	private Boolean debugging = false;

	/**
	 * Return a textual name for the service.
	 *
	 * @return the name of the service ("Random Service")
	 */
	public String serviceName() {
		return "Random Service";
	}

	/**
	 * Create a random proxy service based on the services specified in the
	 * <code>proxy.random.services</code> resource.
	 */
	public RandomProxy() {
		init(Properties.get("proxy.random.services"));
	}
	
	/**
	 * Create a random proxy service based on a specified list of service classes.
	 *
	 * @param baseServices a (comma-separated) list of base service classes
	 */
	public RandomProxy(String baseServices) {
		init(baseServices);
	}

	private void init(String serviceList) {
	    this.debugging = Properties.getBool("proxy.debug");
		for (String serviceName : serviceList.split(",")) {
			serviceName = serviceName.trim();
	        if (debugging) {
	        	System.err.println("[debug] RandomProxy: adding '" + serviceName + "'");
	         }
			Service baseService = (Service)Properties.getObjectOfClass(serviceName,
				"ippo.assignment1.service:ippo.assignment1.library.service");
			baseServices.add(baseService);
		}	    
	}
	
	/**
	 * Return a picture from a random service.
	 *
	 * @param subject the requested subject
	 * @param index the index of the image within all images for the requested subject
	 *
	 * @return the picture
	 */
	public Picture getPicture(String subject, int index) {
		if (baseServices.size()==0) {
    		return new Picture("random service has no base services specified");
		}
		Service baseService = baseServices.get(rand.nextInt(baseServices.size()));
        if (debugging) {
        	System.err.println("[debug] RandomProxy: using '" + baseService.serviceName() + "'");
         }
		return baseService.getPicture(subject,index);
    }
}
