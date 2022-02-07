// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.utils;
import java.nio.charset.StandardCharsets;
import net.goui.util.MTRandom;

/**
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */

public class BugChooser {

    /* ******* WARNING ******
       The data here should really be read from the JSON in the Admin/Bugs file
       We just haven't got around to this yet
       If you change anything here, change the JSON as well.

       You do need to be careful though - you don't want the json file to be shipped
       to the students! so you probably need to generate this at compile time.
    */

	private static final int[][] bugTable = {
			{ 1 },						// A: no bugs
			{ 8 },						// B: no cache implemented (null proxy)
			{ 2 },						// C: return first picture from cache every time
			{ 3, 5, 9, 10 },			// D: bugs not involving the index
			{ 4, 11, 13, 14 },			// E: bugs involving the index
			{ 12 },						// F: exception
			{ 15 },						// G: non-termination
			// { 6, 7 },				// H: these are not used in the assignment
	};

	private final byte[] seedBytes;
	
	public BugChooser(String uid) {
		
		// check the uid is sane-ish
		if (uid==null || uid.length()<4) {
			System.err.println("[error] BugChooser: missing or short uid");
			System.exit(1);
		}

		// use the uid to seed the random generator
		seedBytes = (uid).getBytes(StandardCharsets.UTF_8);
	}

	public int errorForCategory(String error) {

		MTRandom rgen;
		
		// check the category is single character
		if (error.length()!=1) {
			System.err.println("[error] BugChooser: invalid category = "+error);
			System.exit(1);
		}

		// convert the category
		int categoryIndex = (int)(error.toUpperCase().charAt(0)) - (int)('A');
		if (categoryIndex<0 || categoryIndex>=bugTable.length) {
			System.err.println("[error] BugChooser: category out of range = "+error);
			System.exit(1);
		}
		
		// mix the category into the seed by overwriting the first seed byte
		// create the generator
		seedBytes[0] = (byte)categoryIndex;
		rgen = new MTRandom(seedBytes);
		 
		// choose error code from possible range
		int[] codes = bugTable[categoryIndex];
		int index = rgen.nextInt(codes.length);
		return codes[index];
	}
	
	public static int numCategories() {
		return bugTable.length;
	}
}
