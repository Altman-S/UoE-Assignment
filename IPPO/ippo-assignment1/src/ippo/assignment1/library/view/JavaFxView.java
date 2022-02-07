// IPPO Assignment 1, Version §VERSION, §PUBDATE
package ippo.assignment1.library.view;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ippo.assignment1.library.Picture;
import ippo.assignment1.library.PictureViewer;
import ippo.assignment1.library.controller.Controller;
import ippo.assignment1.library.utils.Properties;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

/**
 * An abstract view for the PictureViewer application which provides
 * common methods for JavaFx views.
 * 
 * @author  Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version §VERSION, §PUBDATE
 */
abstract public class JavaFxView implements View {

	protected Controller controller = null;

	@FXML
	protected ImageView imageView;

	@FXML
	protected ImageView iconView;

	@FXML
	protected Label captionLabel;

	@FXML
	protected Label errorLabel;

	JavaFxView() {
		super();
		boolean debugging = Properties.getBool("view.debug");
		if (debugging) {
			System.err.println("[debug] JavaFxView: creating view object: "+this.getClass().getName());
		}
	}

	/**
	 * Create a JavaFx view from an fxml file.
	 *
	 * @param viewClassName the name of the view class
	 * @return the view
	 */
	public static View factory(String viewClassName) {

		String packagePath = (viewClassName.contains(".")) ? ""
			: "ippo.assignment1.view:ippo.assignment1.library.view";

	    for (String thePackage : packagePath.split(":")) {

	    	String qualifiedClassName = (viewClassName.contains(".")) ? viewClassName
	    		: thePackage + "." + viewClassName;

			String viewFxml = Properties.get("view") + ".fxml";
			URL fxmlURL = null;
			boolean debugging = Properties.getBool("view.debug");
		
			if (debugging) {
				System.err.println("[debug] JavaFxView: potential view class: "+qualifiedClassName);
			}

			try {
				Class<?> viewClass = Class.forName(qualifiedClassName);
				if (debugging) {
					System.err.println("[debug] JavaFxView: view class located: "+qualifiedClassName);
					System.err.println("[debug] JavaFxView: attempting to load fxml: "+viewFxml);
				}
				// load the fxml from the fxml subdirectory of the resource folder
				// https://stackoverflow.com/questions/21128652/location-is-required-in-javafx-with-gradle
				// we used to load it from the same directory as the java class:
				// fxmlURL = viewClass.getResource(viewFxml);
				fxmlURL = ClassLoader.getSystemResource("fxml/"+viewFxml);
				// the order in which you set up the fxmlloader is crucial!
				// in particular, you must pass the URL to the constructor (not the load())
				// otherwise, you will get (sometimes?) a null controller returned
				// see: https://stackoverflow.com/questions/22910611/javafx-controller-is-always-null
				FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
				AnchorPane page = fxmlLoader.load();
				if (debugging) {
					System.err.println("[debug] JavaFxView: fxml loaded: "+viewFxml);
				}
				View view = fxmlLoader.getController();
				if (view == null) {
			        System.err.println("[err] JavaFxView: null controller: "+qualifiedClassName);
        			System.exit(1);
				}
				if (debugging) {
					System.err.println("[debug] JavaFxView: controller valid");
					System.err.println("[debug] JavaFxView: attempting to create scene");
				}
				Scene scene = new Scene(page);
				if (debugging) {
					System.err.println("[debug] JavaFxView: scene created");
					System.err.println("[debug] JavaFxView: attempting to load css: css/pictureviewer.css");
				}
				// load the css from the css subdirectory of the resource folder
				scene.getStylesheets().add("css/pictureviewer.css");
				if (debugging) {
					System.err.println("[debug] JavaFxView: css loaded");
					System.err.println("[debug] JavaFxView: attempting to set scene");
				}
				PictureViewer.mainStage.setScene(scene);
				PictureViewer.mainStage.setResizable(false);
				if (debugging) {
					System.err.println("[debug] JavaFxView: scene set");
					System.err.println("[debug] JavaFxView: returning controller");
				}
				return view;

			} catch (IOException ex) {
				System.err.println("[error] JavaFxView: can't load fxml file \""
						+ fxmlURL.toString() + "\"\n" + ex);
				System.exit(1);

			} catch (ClassNotFoundException ex) {
				if (debugging) {
					System.err.println("[debug] JavaFxView: no class: "+qualifiedClassName);
				}
			}
		}
	    
        System.err.println("[err] JavaFxView: no view class found: "+viewClassName);
        System.exit(1);
        return null;
	}

	/**
	 * Set the controller.
	 *
	 * @param controller the controller to be notified when the user selects an item
	 */
	public void setController(Controller controller) { 
 		this.controller = controller;
 	}
 	
	/**
	 * Start the view. Create a default caption and display the main JavaFx stage.
	 * The interface then waits for user interaction and calls
	 * the controller's <code>select()</code> method when the user selects
	 * an item.
	 */
	public void start() {
 		String caption = "PictureViewer §VERSION";
 		if (controller != null) {
 			caption = caption + " (using " + controller.getClass().getSimpleName() + ")";
 		}
 		setCaption(caption);
 		PictureViewer.mainStage.show();
 	}

	/**
	 * Display the specified picture in the interface.
	 *
	 * @param picture the picture to display
	 */
	public void showPicture(Picture picture) {
		if (picture.isValid()) {
			clearErrorMessage();
			imageView.setImage(picture.image());
			setCaption(picture.description());
		} else {
			showErrorMessage(picture.errorMessage());
			imageView.setImage(null);
			setCaption("");	
		}
	}
	
	/**
	 * Display the specified text in the interface.
	 *
	 * @param caption the text to display
	 */
	public void setCaption(String caption) {
		this.captionLabel.setText(caption);
	}
	
	/**
	 * Display the specified error message in the interface.
	 *
	 * @param message the error message to display
	 */
	public void showErrorMessage(String message) {
		errorLabel.setText(message);
		showErrorIcon(true);
	}
	
	/**
	 * Clear the error message.
	 */
	public void clearErrorMessage() {
		errorLabel.setText("");
		showErrorIcon(false);
	}

	/**
	 * Enable/display the error icon.
	 *
	 * @param state true/false to enable/disable the icon
	 */
	protected void showErrorIcon(Boolean state) {
		if (state && iconView.getImage()==null) {
			URL errorIconURL = this.getClass().getResource("/images/Error.png");			
			iconView.setImage(new Image(errorIconURL.toString()));	
		}
		iconView.setVisible(state);
	}
 }
