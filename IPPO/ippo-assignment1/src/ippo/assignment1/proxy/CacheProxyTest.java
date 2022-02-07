// IPPO Assignment 1, Version 20.3, 21/12/2020
package ippo.assignment1.proxy;

import static org.junit.Assert.*;
import java.awt.image.BufferedImage;

import org.junit.Test;


import ippo.assignment1.library.Picture;
import ippo.assignment1.library.service.Service;

/**
 * Tests for the cache proxy for the PictureViewer application.
 * 
 * @author YOUR NAME HERE
 * @version YOUR VERSION HERE
 */
public class CacheProxyTest implements Service {
	
	/**
	 * Test that requests for the same subject and index return the same image.
	 */
	@Test
	public void equalityTest() {

		Service proxy = new CacheProxy(this);
		Picture firstPicture = proxy.getPicture("equalityTest",2);
		Picture secondPicture = proxy.getPicture("equalityTest",2);
		assertTrue(
				"different picture returned for same subject (and index)",
				firstPicture == secondPicture);
	}

	/**
	 * Return a picture from the simulated service.
	 * This service simply returns an empty picture every time that it called.
	 * Note that a <em>different</em> object is returned each time, even if the
	 * subject and index are the same.
	 *
	 * @param subject the requested subject
	 * @param index the index of the picture within all pictures for the requested topic
	 *
	 * @return the picture
	 */
	@Override
	public Picture getPicture(String subject, int index) {
		return new Picture((BufferedImage)null, subject ,serviceName(), index);
	}
	
	/**
	 * Return a textual name to identify the simulated service.
	 *
	 * @return the name of the service
	 */
	@Override
	public String serviceName() {
		return "CacheProxyTest";
	}
}
