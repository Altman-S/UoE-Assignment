// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.controller;

/**
 * A controller for the PictureViewer application.
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
public interface Controller {

	/**
	 * Start the controller.
	 */
    void start();

	/**
	 * Handle the specified selection from the interface.
	 *
	 * @param selectionID the id of the selected item
	 */
    void select(int selectionID);
}
