// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.proxy;

import static org.junit.Assert.*;
import org.junit.Test;

import ippo.assignment1.library.Picture;
import ippo.assignment1.library.service.Service;
import ippo.assignment1.library.utils.Properties;
import ippo.assignment1.library.utils.PropertyDelegate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javafx.scene.image.Image;

/**
 * Tests for the cache proxy of the PictureViewer application.
 * ** DO NOT DISTRIBUTE **
 * 
 * @author  Michael Glienecke
 * @version §VERSION, §PUBDATE
 */
public class BuggyProxyTestTemplate implements Service, PropertyDelegate {

	public String classUnderTest() { return "BuggyProxy"; }

	// this has to be changed
	private String uuid = "s2154448";

	/**
	 * Test that the cache returns a picture with the expected subject.
	 */
	@Test
	public void subject() {

		Service proxy = newProxyForThis(uuid, "A");
		for ( int i=0; i<5; ++i ) {
			Picture picture = null;
			try { picture = proxy.getPicture("subjectTest",1); }
			catch (Throwable e) { fail( "[<GETEX>] getPicture threw exception" ); }
			assertTrue(
					"[<SUBJECT>] unexpected subject returned on attempt "+i+" ("+picture.subject()+")",
					picture.subject().equals("subjectTest") );
		}	
	}
	
	/**
	 * Test that the cache returns a picture with the expected index.
	 */
	@Test
	public void index() {

		Service proxy = newProxyForThis(uuid, "A");

		for ( int i=0; i<5; ++i ) {
			Picture picture = null;
			try { picture = proxy.getPicture("indexTest",1955 + i); }
			catch (Throwable e) { fail( "[<GETEX>] getPicture threw exception" ); }
			assertTrue(
					"[<INDEX>] unexpected index returned on attempt "+i+" ("+picture.index()+")",
					picture.index().equals(1955 + i) );
		}
	}

	/**
	 * Test that the cache returns a picture with the expected source.
	 */
	@Test
	public void source() {

		Service proxy = newProxyForThis(uuid, "A");

		for ( int i=0; i<5; ++i ) {
			Picture picture = null;
			try { picture = proxy.getPicture("sourceTest",1); }
			catch (Throwable e) { fail( "[<GETEX>] getPicture threw exception" ); }
			assertTrue(
					"[<SOURCE>] unexpected source returned on attempt "+i+" ("+picture.source()+")",
					picture.source().equals(serviceName()) );
		}
	}

	/**
	 * Test that the cache returns a picture with the expected image.
	 */
	@Test
	public void image() {

		Service proxy = newProxyForThis(uuid, "A");

		for ( int i=0; i<5; ++i ) {
			Picture picture = null;
			try { picture = proxy.getPicture("imageTest",1); }
			catch (Throwable e) { fail( "[<GETEX>] getPicture threw exception" ); }
			assertTrue(
					"[<IMAGE>] unexpected image returned on attempt "+i,
					 picture.image()==null );
		}
	}
	
	/**
	 * Test that requests for the same subject and index return the same picture.
	 */
	@Test
	public void equality() {

		Service proxy = newProxyForThis(uuid, "A");
		for ( int i=0; i<5; ++i ) {
			Picture firstPicture = null;
			Picture subsequentPicture = null;
			try { firstPicture=proxy.getPicture("equalityTest",2);
				subsequentPicture = proxy.getPicture("equalityTest",2); }
			catch (Throwable e) { fail( "[<GETEX>] getPicture threw exception" ); }
			assertTrue(
					"[<EQ>] different picture returned for same subject (and index) on attempt "+i,
					firstPicture == subsequentPicture );
		}
	}

	/**
	 * Test that requests for different subjects with the same index
	 * return different pictures.
	 */
	@Test
	public void different() {

		Service proxy = newProxyForThis(uuid, "A");
		Picture firstPicture = null;
		try { firstPicture = proxy.getPicture("subjectTest",5); }
		catch (Throwable e) { fail( "[<GETEX>] getPicture threw exception" ); }
		for ( int i=0; i<5; ++i ) {
			Picture subsequentPicture = null;
			try { subsequentPicture = proxy.getPicture("subjectTest"+(i+1),5); }
			catch (Throwable e) { fail( "[<GETEX>] getPicture threw exception" ); }
			assertTrue(
					"[<DIFF>] same picture returned for different subjects",
					!firstPicture.equals(subsequentPicture) );
		}
	}
	
	/**
	 * Test that requests for the same subject with a different index
	 * return different pictures.
	 */
	@Test
	public void differentIndex() {

		Service proxy = newProxyForThis(uuid, "A");
		Picture firstPicture = null;
		try {  firstPicture = proxy.getPicture("indexTest",3); }
		catch (Throwable e) { fail( "[<GETEX>] getPicture threw exception" ); }
		for ( int i=0; i<5; ++i ) {
			Picture subsequentPicture = null;
			try { subsequentPicture = proxy.getPicture("indexTest",4+i); }
			catch (Throwable e) { fail( "[<GETEX>] getPicture threw exception" ); }
			assertTrue(
					"[<DIFFIND>] same picture returned for different index values",
					!firstPicture.equals(subsequentPicture) );
		}
	}

	/**
	 * Test that 100 different requests return the same pictures when they
	 * are repeated with the same subjects and indices.
	 */
	@Test
	public void stress() {

		Service proxy = newProxyForThis(uuid, "A");
		final int numSubjects = 100;
		Picture picture;
		int indexMatches = 0;
		int subjectMatches = 0;
		
		try {
			for (int i = 0; i < numSubjects; i++) {
				picture = proxy.getPicture("stressTest" + i, i);
			}
			for (int i = 0; i < numSubjects; i++) {
				picture = proxy.getPicture("stressTest" + i, i);
				if (picture.subject().equals("stressTest" + i)) { ++subjectMatches; }
				if (picture.index().equals(i)) { ++indexMatches; }
			}
		} catch (Throwable e) { fail( "[<GETEX>] getPicture threw exception" ); }
		assertTrue(
				"[<STRESS>] stress test failed ("+subjectMatches+"/"+numSubjects+" subjects matched)",
				subjectMatches==numSubjects );
		assertTrue(
				"[<STRESS>] stress test failed ("+indexMatches+"/"+numSubjects+" indices matched)",
				indexMatches==numSubjects );
	}	
	
	/**
	 * Return a new proxy of the specified class which uses "this" as the base service
	 *
	 * @return the proxy service
	 */
	@SuppressWarnings("unchecked")
	private Service newProxyForThis(String uuid, String bugToIssue) {
		Constructor<Service> proxyConstructor;
		Class<Service> proxyClass = null;
		Service proxy = null;
		// we act as a property delegate so that we can inject test values for
		// certain resources (such as debugging)
		Properties.setDelegate(this);
		try {
			String qualifiedTestClass = "ippo.assignment1.library.proxy."+classUnderTest();
			proxyClass = (Class<Service>)Class.forName(qualifiedTestClass);

			// try looking for a proxy which exposes this constructor
			proxyConstructor = proxyClass.getConstructor(Service.class, String.class, String.class);
			proxy = proxyConstructor.newInstance(this, uuid, bugToIssue);
		} catch (InstantiationException | IllegalAccessException |
				IllegalArgumentException | ClassNotFoundException ex) {
			assertTrue(
				"[<CREATE>] BuggyProxyTest: Failed to create proxy for : "
				+ proxyClass.getName() + "\n- " + ex,
				false );
		} catch (NoSuchMethodException ex) {
			assertTrue(
				"[<CONS>] BuggyProxyTest: No constructor(service) for : "
				+ proxyClass.getName(),
				false );
	    } catch (SecurityException ex) {
			assertTrue(
				"[<EXIT>] BuggyProxyTest: constructor exited : "
				+ proxyClass.getName(),
				false );
		} catch (InvocationTargetException ex) {
			// unwrap the invoked exception
			// https://examples.javacodegeeks.com/java-basics/exceptions/java-lang-reflect-invocationtargetexception-how-to-handle-invocation-target-exception/
			Throwable cause = ex.getCause();
			fail( (cause.getMessage()==null)
				? "[<EXCEPTION>] BuggyProxyTest: invocation target exception (unknown cause)"
				: cause.getMessage() );
       	}
		return proxy;
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
		return new Picture((Image)null, subject, serviceName(), index);
	}
	
	/**
	 * Return a textual name to identify the simulated service.
	 *
	 * @return the name of the service ("CacheProxyTest")
	 */
	@Override
	public String serviceName() {
		return "BuggyProxyTest";
	}
	
	/**
	 * Return string value of property.
	 *
	 * @param propertyName the property name
	 *
	 * @return property value.
	 */
	// when we are doing the autotesting, this traps attempts to read
	// resources other than the standard debugging ones
	@Override
	public String getStringProperty(String propertyName) {
		// disable debugging
		if (propertyName.endsWith(".debug")) { return "false"; }
		// anything else is unexpected
		// really. if we get a request here for "subjects", for example
		// it probably means the cache and controller are somehow confused
		fail("[<PROPREQ>] requested property \""+propertyName+"\"");
		return null;
	}

	/**
	 * Create object of specified class.
	 *
	 * @param className the name of the class
	 *
	 * @return null so that the default object is created by the caller
	 */
	@Override
	public Object getObjectOfClass(String className) { return null; }
}
