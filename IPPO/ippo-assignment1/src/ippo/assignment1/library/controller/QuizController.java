// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.controller;

import java.lang.String;

import ippo.assignment1.library.Picture;
import ippo.assignment1.library.service.LocalService;
import ippo.assignment1.library.service.Service;
import ippo.assignment1.library.service.ServiceFromProperties;
import ippo.assignment1.library.utils.Properties;
import ippo.assignment1.library.view.View;
import ippo.assignment1.library.view.ViewFromProperties;
import ippo.assignment1.library.utils.HashMap;

import java.util.Random;

/**
 * A controller for the PictureViewer application which displays a picture
 * of a random Munro and asks the user to guess what it is.
 * <p>The following properties are supported:
 * <ul>
 * <li><code>controller.subjects</code> - a comma-separated list of subjects.
 * <li><code>controller.debug</code> - <code>true</code> or <code>false</code> to enable/disable debugging.</li>
 * </ul>
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
public class QuizController implements Controller {

	private View view;
	private Service service;
	private Boolean debugging = false;
	private final HashMap<Integer,String> subjectBySelection = new HashMap<Integer,String>();
	
	private int newSelectionID;			// the selection id for the "New" button
	private int randomSelectionID;		// the selection id for the current random selection
	private int randomIndex = 0;		// the random index of the current selection
	private String currentSubject;		// the subject of the currrent selection
	
	private Picture tick;
	private Picture cross;
	
	/**
	 * Create a quiz controller.
	 */
	public QuizController() {
	    debugging = Properties.getBool("controller.debug");
	}
	
	/**
	 * Start the controller.
	 * Read the list of available selections from the <code>QuizController.subjects</code> property.
	 */
	public void start() {

		view = new ViewFromProperties(this);
		service = new ServiceFromProperties();

        if (debugging) {
        	System.err.println("[debug] QuizController: starting");
        }
		String[] subjectList = Properties.get("controller.subjects").split(",");
		for (String subject : subjectList) {
			subject = subject.trim();
			int selectionID = view.addSelection(subject);
	        if (debugging) {
	        	System.err.println("[debug] QuizController: adding subject "+selectionID+" = \""+subject+"\"");
	        }
			subjectBySelection.put(selectionID,subject);
		}
		
		// load the right & wrong icons
		Service localService = new LocalService("images");
		tick = localService.getPicture("BigTick", 1);
		cross = localService.getPicture("BigCross", 1);
		
		// add a button for a new selection
        newSelectionID = view.addSelection("New");
        // start with a new selection
        select(newSelectionID);
        
		view.start();
	}

	/**
	 * Handle the specified selection from the interface.
	 *
	 * @param selectionID the id of the selected item
	 */
	public void select(int selectionID) {
				
		if (selectionID == newSelectionID) {
	
			// check the number of subjects
			int numSubjects = subjectBySelection.size();
			if (numSubjects<2) {
	    		System.err.println("[error] QuizService: must be at least two subjects (only found "
	    				+ numSubjects + ")");
	    		System.exit(1);
			}

			// choose a random one different from last time
			int oldIndex = randomIndex;
			while (randomIndex==oldIndex) {
				randomIndex = new Random().nextInt(numSubjects);
			}
			oldIndex = randomIndex;
			
			// find the corresponding selection id
			int index = 0;	
			for (Integer key : subjectBySelection.keySet()) {
				if (index++ >= randomIndex) {
					randomSelectionID = key;
					break;
				}
			}
			
			// show the random picture
			// don't display the answer!!!
			if (debugging) {
				System.err.println("[debug] QuizController: new random selection ["+randomSelectionID+"]");
			}
			currentSubject = subjectBySelection.get(randomSelectionID);
			view.showPicture(service.getPicture(currentSubject,1));
			view.setCaption("Guess the Munro ...");

		} else {

			if (selectionID == randomSelectionID) {
				if (debugging) {
					System.err.println("[debug] QuizController: correct selection ["+selectionID+"]");
				}
				view.showPicture(tick);
				view.setCaption("Right! It was \""+currentSubject+"\"");
			} else {
				if (debugging) {
					System.err.println("[debug] QuizController: wrong selection ["+selectionID+"]");
				}
				view.showPicture(cross);
				view.setCaption("Wrong! It was \""+currentSubject+"\"");
			}
		}
 	}
}
