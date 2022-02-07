// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.utils;

/**
 * A delegate for the properties.
 * Property values are normally retrieved from the property files.
 * If a delegate is set for the <code>Properties</code> object, the delegate will be called
 * whenever the value of a property is requested (<code>getStringProperty</code>),
 * or whenever an instance of a class is requested (<code>getObjectOfClass</code>).
 * If these methods return non-null, then the returned value will be used in preference
 * to any value from the property files.
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
public interface PropertyDelegate {

	/**
	 * Return a value for the named property.
     * Return <code>null</code> to use the value from the property file.
	 *
	 * @param propertyName the name of the property
	 * @return the value of the property, or <code>null</code> to use the value from the property files.
	 */
	String getStringProperty(String propertyName);
	
	/**
	 * Return an instance of the specified class.
     * Return <code>null</code> to use a new instance.
	 *
	 * @param className the name of the class
	 * @return an instance of the specified class, or <code>null</code> to create a new instance.
	 */
	Object getObjectOfClass(String className);
}
