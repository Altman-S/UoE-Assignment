// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * An view for the PictureViewer application which uses JavaFx
 * and allows the user to make selections using a menu.
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
public class MenuView extends JavaFxView {
	
	@FXML
	private Menu subjectMenu;
	
	/**
	 * Add an item to the interface which the user can select.
	 *
	 * @param label a label for the item, to be displayed in the interface
	 * @return an identifier for the selection. This identifier will be passed to the controller
	 * as the argument of the <code>select()</code> method when the user selects this item in the interface.
	 * Note that some controllers may return consecutive integers for the selection identifiers, but this 
	 * is not required.
	 */
	@Override
	public int addSelection(String label) {
		MenuItem item = new MenuItem(label);
	    int selectionID = item.hashCode();
		item.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	if (controller != null) {
		    		controller.select(selectionID);
		    	}
		    }
		});
        subjectMenu.getItems().addAll(item);
        return selectionID;
	}
}
