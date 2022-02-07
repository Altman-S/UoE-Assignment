// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.utils;

import java.util.Map;
import java.util.Set;

/**
 * A subclass of <code>Java.Util.HashMap</code> which provides some extra information for testing.
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */

public class HashMap<K, V> extends java.util.HashMap<K, V> {
	
  	private static final long serialVersionUID = 1L;
  	
	/**
	 * The last created HashMap instance.
	 */
 	public static Object lastMapCreated = null;
	/**
	 * The last requested key.
	 */
  	public Object lastKeyRequested = null;
	/**
	 * The last inserted key.
	 */
 	public Object lastKeyEntered = null;
	/**
	 * The last inserted value.
	 */
 	public Object lastValueEntered = null;
  	/**
	 * The number of "get" requests made
	 */
  	public int numOfGets = 0;
  	/**
  	 * flags for called methods
  	 */
  	public boolean entrySetCalled = false;
  	public boolean keySetCalled = false;
  	
  	public HashMap() {
  		super();
  		lastMapCreated = this;
  		keySetCalled = false;
  		entrySetCalled = false;
  		// System.err.println("NEW");
  	}
  	
  	public void Reset() {
		lastKeyRequested = null;
		lastKeyEntered = null;
		lastValueEntered = null;
		numOfGets = 0;
  		// System.err.println("RESET");
  	}
  	
	@Override
	public V get(Object key) {
		lastKeyRequested = key;
		++numOfGets;
		return super.get(key);
	}
	
	@Override
    public V put(K key, V value) {
		V v = super.put(key,value);
		lastKeyEntered = key;
		lastValueEntered = v;
		return v;
	}

	@Override
	public Set<Entry<K,V>> entrySet() {
		entrySetCalled = true;
		return super.entrySet();
	}
	
	@Override
	public Set<K> keySet() {
		keySetCalled = true;
		return super.keySet();
	}

}
