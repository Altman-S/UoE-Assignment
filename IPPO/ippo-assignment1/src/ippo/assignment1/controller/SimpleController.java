// IPPO Assignment 1, Version 20.3, 21/12/2020
package ippo.assignment1.controller;

import ippo.assignment1.library.controller.Controller;
import ippo.assignment1.library.Picture;
import ippo.assignment1.library.service.Service;
import ippo.assignment1.library.service.ServiceFromProperties;
import ippo.assignment1.library.view.View;
import ippo.assignment1.library.view.ViewFromProperties;

/**
 * A simple controller for the PictureViewer application.
 * 
 * @author Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version 20.3, 21/12/2020
 */
public class SimpleController implements Controller {

	private View view;
	private Service service;

	private int selection1;
	private int selection2;
	private int selection3;

	/**
	 * Start the controller.
	 */
	public void start() {

		// create the view and the service objects
		view = new ViewFromProperties(this);
		service = new ServiceFromProperties();

		// create three selections in the interface
		selection1 = view.addSelection("Stob Binnein");
		selection2 = view.addSelection("Gairich");
		selection3 = view.addSelection("Ben Lomond");

		// start the interface
		view.start();
	}

	/**
	 * Handle the specified selection from the interface.
	 *
	 * @param selectionID the id of the selected item
	 */
	public void select(int selectionID) {
		
		// a picture corresponding to the selectionID
		// by default, this is an empty picture
		// (this is used if the selectionID does not match)
		Picture picture = new Picture();

		// create a picture corresponding to the selectionID
		if (selectionID==selection1) {
			picture = service.getPicture("Stob Binnein",1);
		}
		else if (selectionID==selection2) {
			picture = service.getPicture("Gairich",1);
		}
		else if (selectionID==selection3) {
			picture = service.getPicture("Ben Lomond",1);
		}
		
		// show the picture in the interface
		view.showPicture(picture);
	}
}
