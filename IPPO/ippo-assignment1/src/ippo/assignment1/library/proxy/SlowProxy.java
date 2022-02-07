// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.proxy;

import java.util.Random;

import ippo.assignment1.library.Picture;
import ippo.assignment1.library.service.Service;
import ippo.assignment1.library.service.ServiceFromProperties;
import ippo.assignment1.library.utils.Properties;

/**
 * A proxy service for the PictureViewer application which provides a slow version
 * of the base service (this is useful for testing).
 * <p>The following properties are supported:
 * <ul>
 * <li><code>proxy.slow.service</code> - the class to use for the base service if not specified in the constructor.</li>
 * <li><code>proxy.slow.min</code> - the minimum delay in seconds.</li>
 * <li><code>proxy.slow.max</code> - the maximum delay in seconds.</li>
 * <li><code>proxy.debug</code> - <code>true</code> or <code>false</code> to enable/disable debugging.</li>
 * </ul>
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
public class SlowProxy implements Service {

	private Service baseService = null;
	private int minDelay = 0;
	private int maxDelay = 0;
	private Boolean debugging = false;
		
	/**
	 * Return a textual name for the service.
	 *
	 * @return the name of the service
	 */
	public String serviceName() {
		return baseService.serviceName();
	}

	/**
	 * Create a slow proxy service based on the specified service.
	 * <code>SlowProxy.base_service</code> resource.
	 *
	 * @param baseService the base service
	 */
	public SlowProxy(Service baseService) {
		this.baseService = baseService;
		init();
	}

	/**
	 * Create a slow proxy service based on the service specified in the
	 */
	public SlowProxy() {
		baseService = new ServiceFromProperties("proxy.slow.service");
		init();
	}

	private void init() {
	    this.debugging = Properties.getBool("proxy.debug");
	    this.minDelay = Properties.getInt("proxy.slow.min");
	    this.maxDelay = Properties.getInt("proxy.slow.max");
	}
	
	/**
	 * Return a picture from the base service, with a random delay between <code>min_delay</code>
	 * and <code>max_sdelay</code> (seconds).
	 *
    * @param subject the free-text subject string
    * @param index the index of the matching picture to return
    * @return the requested picture
    */
    public Picture getPicture(String subject, int index) {
    	Random rand = new Random();
        int delay = rand.nextInt((maxDelay - minDelay) + 1) + minDelay;
        if (debugging) {
        	System.err.println("[debug] SlowProxy: fetching picture from "
        		+ baseService.serviceName() + " with delay " + delay + "secs");
        }
        try {
        	Thread.sleep(1000*delay);
        } catch(InterruptedException ex) {
        	Thread.currentThread().interrupt();
        }
        return baseService.getPicture(subject, index);
    }
}
