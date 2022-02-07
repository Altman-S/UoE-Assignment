// IPPO Assignment 1, Version 20.3, 21/12/2020
package ippo.assignment1.proxy;

import ippo.assignment1.library.Picture;
import ippo.assignment1.library.service.LocalService;
import ippo.assignment1.library.service.Service;
import ippo.assignment1.library.service.ServiceFromProperties;
import ippo.assignment1.library.utils.HashMap;
import ippo.assignment1.library.utils.Properties;

/**
 * A skeleton cache proxy service for the PictureViewer application.
 * The skeleton does no implement any caching!
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version 20.3, 21/12/2020
 */
public class CacheProxy implements Service {

	private Service baseService = null;
	private HashMap<String, Picture> pictureCache = new HashMap<String, Picture>();

	/**
	 * Return a textual name for the service.
	 *
	 * @return the name of the base service
	 */
	public String serviceName() {
		return baseService.serviceName();
	}
	
	/**
	 * Create a proxy service based on the service specified in the
	 * <code>proxy.cache.service</code> resource.
	 */
	public CacheProxy() {
		baseService = new ServiceFromProperties("proxy.cache.service");
	}
	
	/**
	 * Create a proxy service based on the specified service.
	 *
	 * @param baseService the base service
	 */
	public CacheProxy(Service baseService) {
		this.baseService = baseService;
	}

	/**
	 * Return a picture from the base service. 
	 *
     * @param subject the free-text subject string
     * @param index the index of the matching picture to return
     * @return the requested picture
     */
	public Picture getPicture(String subject, int index) {
		if (pictureCache.containsKey(subject+":"+index)) {
			// return the copy from the cache
			return pictureCache.get(subject+":"+index);
		} else {
			// download it from the base service and store it in the cache before returning it
			Picture newPicture = baseService.getPicture(subject, index);
			pictureCache.put(subject+":"+index, newPicture);
			return newPicture;
		}
    }
}
