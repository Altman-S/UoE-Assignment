// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Static methods for retrieving values from property files and creating class
 * instances from property names. The properties are retrieved from several sources
 * (in order):
 * <ul>
 * <li><code>default.properties</code> (eg. in the library jar file).</li>
 * <li><code>app.properties</code> (eg. in a user jar file).</li>
 * <li><code>app.properties</code> in the <i>user.dir</i><code>/src</code> directory (eg. Eclipse source directory).</li>
 * <li><code>app.properties</code> in the <i>user.dir</i> directory (eg. BlueJ project directory).</li>
 * <li>in the colon-separated list of files given by the <code>IPPO_PROPERTYPATH</code> environment variable.</li>
 *
 * </ul>
 * <p>If the <code>delegate</code> is set, it will queried for resource values before
 * querying the above sources. It will also be given the opportunity to supply instances
 * of classes for given property names.
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
public class Properties {

	private static final String propertyFileEnvVar = "IPPO_PROPERTYPATH";
	private static final String defPropFileName = "properties/default.properties";
	private static final String propFileName = "properties/app.properties";
	private static Properties singleton;
	private static boolean debugging = false;
	private PropertyDelegate delegate = null;
	private java.util.Properties properties;
	
	/**
	 * Load the properties from the property file(s)
	 */
	private void load() {
		
		// default properties (the ones packaged in the library jar file)
		try {	
			properties = new java.util.Properties();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(defPropFileName);
			properties.load(inputStream);
		} catch (Exception ex) {
			System.err.println("[error] Properties: failed to read default properties \"" + defPropFileName + "\"");
			System.exit(1);
		}
		if (debugging) {
			System.err.println("[debug] Properties: loaded default properties \"" + defPropFileName + "\"");
		}
		
		// merge app properties (eg. in a jar file which uses the library) if we find them
		try {	
			java.util.Properties appProperties = new java.util.Properties();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			appProperties.load(inputStream);
			properties.putAll(appProperties);
			if (debugging) {
				System.err.println("[debug] Properties: loaded app properties \"" + propFileName + "\"");
			}
		} catch (Exception ex) {
			if (debugging) {
				System.err.println("[debug] Properties: no app properties \"" + propFileName + "\"");
			}			
		}
		
		// add user.dir properties
		addUserProperties(propFileName);

		// properties specified in the path given in the environment
        String propertyFilePath = System.getenv().get(propertyFileEnvVar);
		if (propertyFilePath == null) {
			if (debugging) {
				System.err.println("[debug] Properties: no environment properties: $" + propertyFileEnvVar );
			}
		} else {
			if (debugging) {
				System.err.println("[debug] Properties: from $" + propertyFileEnvVar + ": \"" + propertyFilePath + "\"");
			}
			for (String path : propertyFilePath.split(":")) {
				addProperties(Paths.get(path.trim()));
			}
		}
	}
	
	/**
	 * Return the singleton properties, creating them if necessary.
	 *
	 * @return the singleton properties
	 */
	private static Properties theProperties() {
		if (singleton == null) {
			if (debugging) { System.err.println("[debug] Properties: creating singleton"); }
			singleton = new Properties();
			singleton.load();
			debugging = Properties.getBool("properties.debug") || debugging;
		}
		if (debugging) { System.err.println("[debug] Properties: loaded"); }
		return singleton;
	}

	/**
	 * Add properties from the specified file, given the path.
	 * Ignore if the file does not exist.
	 *
	 * @param path the path of the property file
	 */
	public static void addProperties(Path path) {
		try {
			InputStream inputStream = Files.newInputStream(path);
			java.util.Properties projProperties  = new java.util.Properties();
			projProperties.load(inputStream);
			theProperties().properties.putAll(projProperties);
			if (debugging) {
				System.err.println("[debug] Properties: loaded user properties \"" + path + "\"");
			}			
		} catch	(FileNotFoundException|NoSuchFileException ex) {
			if (debugging) {
				System.err.println("[debug] Properties: no user properties \"" + path + "\"");
			}			
		} catch (Exception ex) {
			System.err.println("[error] Properties: failed to load properties \""+path+"\"\n"+ ex);
			System.exit(1);			
		}
	}

	/**
	 * Add properties from the specified file in the user project or src directory.
	 * Ignore if the file does not exist.
	 *
	 * @param fileName the name of the property file
	 */
	public static void addUserProperties(String fileName) {
		// properties specified in the "src" dir (eg. running from Eclipse)
		addProperties(Paths.get(System.getProperty("user.dir")).resolve("src").resolve(fileName));
		// properties specified in the "project" dir (eg. running from BlueJ)
		addProperties(Paths.get(System.getProperty("user.dir")).resolve(fileName));
	}
	
	/**
	 * Return the value of a boolean property. The property value should be one of
	 * <code>yes</code>,<code>no</code>,<code>true</code>, or <code>false</code>.
	 *
	 * @param propertyName the name of the property
	 * @return the value of the property
	 */
	public static Boolean getBool(String propertyName) {
		String propertyValue = getProperty(propertyName);
		if (propertyValue == null) { propertyValue=""; }
		Boolean boolValue = false;
		if (propertyValue.equals("no")) { boolValue = false; }
		else if (propertyValue.equals("false")) { boolValue = false; }
		else if (propertyValue.equals("yes")) { boolValue = true; }
		else if (propertyValue.equals("true")) { boolValue = true; }
		else if (propertyValue.equals("")) { 
			System.err.println("[warning] Properties: boolean expected " + propertyName + "='" + propertyValue + "' (assuming false)");
			boolValue = false;
		} else {
			System.err.println("[error] Properties: boolean expected " + propertyName + "='" + propertyValue + "'");
			System.exit(1);
		}
		if (debugging) {
			System.err.println("[debug] Properties: "+propertyName+" = "+(boolValue?"true":"false")+" (bool)");
		}
		return boolValue;
	}
	
	/**
	 * Return the value of an integer property.
	 *
	 * @param propertyName the name of the property
	 * @return the value of the property
	 */
	public static int getInt(String propertyName) {
		String propertyValue = getProperty(propertyName);
		if (propertyValue == null) { propertyValue=""; }
		int value = 0;
		try {
			value = Integer.parseInt(propertyValue);
		} catch (NumberFormatException e) {
			System.err.println("[error] Properties: integer expected '" + propertyName + "=" + propertyValue + "'");
			System.exit(1);			
		}
		if (debugging) {
			System.err.println("[debug] Properties: "+propertyName+" = "+value+" (int)");
		}
		return value;
	}
	
	/**
	 * Return the value of a string property.
	 *
	 * @param propertyName the name of the property
	 * @return the value of the property, or <code>null</code> if not found
	 */
	public static String get(String propertyName) {
		String propertyValue = getProperty(propertyName);
		String displayValue = (propertyValue == null) ? "<null>" : propertyValue;
		if (debugging) {
			System.err.println("[debug] Properties: "+propertyName+" = "+displayValue+" (string)");
		}
		return propertyValue;
	}
	
	/**
	 * Return property value from the delegate or the property files.
	 *
	 * @param propertyName the name of the property
	 * @return the value of the property
	 */
	private static String getProperty(String propertyName) {
		if (delegate() != null) {
			if (debugging) { System.err.println("[debug] Properties: calling delegate: "+propertyName ); }
			String propertyValue = delegate().getStringProperty(propertyName);
			if (propertyValue != null) { 
				if (debugging) { System.err.println("[debug] Properties: delegate returned "+propertyName+" = "+propertyValue ); }
				return propertyValue;
			}
			if (debugging) { System.err.println("[debug] Properties: delegate returned "+propertyName+" =[NULL]" ); }
		}

		String propertyValue = theProperties().properties.getProperty(propertyName);
		if (debugging) {
			if (propertyValue == null) {
				System.err.println("[debug] Properties: "+propertyName+" = [NULL]");
			} else {
				System.err.println("[debug] Properties: "+propertyName+" = "+propertyValue);
			}
		}
		return propertyValue;
	}

	/**
	 * Return an instance of a class whose name is specified in the given property.
	 * Fail if the class does not exist.
	 *
	 * @param propertyName the name of the property specifying the class
	 * @param packagePath a colon-separated list of packages in which to search for the class
	 * @return a new instance of the specified class
	 */
	public static Object getObject(String propertyName, String packagePath) {

		String className = getProperty(propertyName);
		return getObjectOfClass(className, packagePath);
	}

	/**
	 * Return an instance of a class with the given name.
	 * Fail if the class does not exist.
	 *
	 * @param className the name of the class
	 * @param packagePath a colon-separated list of packages in which to search for the class
	 * @return a new instance of the specified class
	 */
	public static Object getObjectOfClass(String className, String packagePath) {

	    for (String thePackage : packagePath.split(":")) {
	    	
	    	String qualifiedName = ((thePackage==".") ? className :
	    						thePackage+"."+className).trim();
	    	if (delegate() != null) {
				Object theObject = delegate().getObjectOfClass(qualifiedName);
				if (theObject != null) { return theObject; }
			}
			try {
				Class<?> theClass = Class.forName(qualifiedName);
				// we use this for creating new JavaFx views ...
		    	try {
					Method factory = theClass.getMethod("factory",String.class);
					if (debugging) {
						System.err.println("[debug] Properties: "+qualifiedName+" (object from factory)");
					}
					return factory.invoke(null,className);
				} catch (NoSuchMethodException ex) {
					if (debugging) {
						System.err.println("[debug] Properties: "+qualifiedName+" (object)");
					}
					return theClass.getDeclaredConstructor().newInstance();
				}
			} catch (ClassNotFoundException ex) {
				if (debugging) {
					System.err.println("[debug] Properties: "+qualifiedName+" (no class)");
				}				
			} catch (Exception ex) {
				// log the gory details
				// see: https://alvinalexander.com/scala/how-convert-stack-trace-exception-string-print-logger-logging-log4j-slf4j
				StringWriter sw = new StringWriter();
				ex.printStackTrace(new PrintWriter(sw));
				System.err.println("[error] Properties: failed to create class \""+qualifiedName+"\"\n"+ sw);
				System.exit(1);			
			}
			
	    }
	    
        System.err.println("[err] Properties: no class found: \""+className+"\"");
        System.exit(1);
        return null;
	}
	
	/**
	 * Return the delegate.
	 *
	 * @return the delegate
	 */
	static public PropertyDelegate delegate() {
		return theProperties().delegate;
	}
	
	/**
	 * Set the delegate.
	 *
	 * @param delegate the delegate
	 */
	static public void setDelegate(PropertyDelegate delegate) {
		theProperties().delegate = delegate;
	}
}
