// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library;

import ippo.assignment1.library.controller.Controller;
import ippo.assignment1.library.controller.ControllerFromProperties;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main program for a PictureViewer application.
 * <p>The following properties are supported:
 * <ul>
 * <li><code>controller</code> - the class to use as the controller.
 * </ul>
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
public class PictureViewer extends Application {
	
    /**
     * The main JavaFx stage.
     */	
	public static Stage mainStage;

    /**
     * Start the viewer on the specified stage.
     *
     * @param stage the JavaFx stage for the application. 
     */
	public void start(Stage stage) {
		
		mainStage = stage;

		Controller controller = new ControllerFromProperties();
		controller.start();			
	}
		
	 /**
     * Start the application
     *
     * @param args the command line arguments. 
     */
    public static void main(String[] args) {
     	launch(args);
     	System.exit(0);
    }
}
