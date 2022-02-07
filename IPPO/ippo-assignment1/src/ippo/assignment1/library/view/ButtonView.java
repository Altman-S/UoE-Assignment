// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;

/**
 * An view for the PictureViewer application which uses JavaFx
 * and allows the user to make selections using buttons.
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
public class ButtonView extends JavaFxView {
	
	@FXML
	private HBox buttonBox;
	
	private int nextSelectionID = 0;
	
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
	    Button button = new Button(label);
	    HBox.setMargin(button, new Insets(10.0,0.0,10.0,5.0));
	    buttonBox.getChildren().add(button);
	    int selectionID = nextSelectionID;
		button.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	if (controller != null) {
		    		controller.select(selectionID);
		    	}
		    }
		});
		++nextSelectionID;
		return selectionID;
	}
}
