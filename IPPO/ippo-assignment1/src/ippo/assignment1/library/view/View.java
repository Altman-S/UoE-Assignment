// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.view;

import ippo.assignment1.library.Picture;
import ippo.assignment1.library.controller.Controller;

/**
 * A view for the PictureViewer application.
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
public interface View {

	/**
	 * Start the view. The interface waits for user interaction and calls
	 * the controller's <code>select()</code> method when the user selects
	 * an item.
	 */
    void start();
	
	/**
	 * Set the controller.
	 *
	 * @param controller the controller to be notified when the user selects an item
	 */
    void setController(Controller controller);

	/**
	 * Add an item to the interface which the user can select.
	 *
	 * @param label a label for the item, to be displayed in the interface
	 * @return an identifier for the selection. This identifier will be passed to the controller
	 * as the argument of the <code>select()</code> method when the user selects this item in the interface.
	 * Note that some controllers may return consecutive integers for the selection identifiers, but this 
	 * is not required.
	 */
    int addSelection(String label);
	
	/**
	 * Display the specified picture in the interface.
	 *
	 * @param picture the picture to display
	 */
    void showPicture(Picture picture);

	/**
	 * Display the specified text in the interface.
	 *
	 * @param caption the text to display
	 */
    void setCaption(String caption);
	
	/**
	 * Display the specified error message in the interface.
	 *
	 * @param message the error message to display
	 */
    void showErrorMessage(String message);
	
	/**
	 * Clear the error message.
	 */
    void clearErrorMessage();
}
