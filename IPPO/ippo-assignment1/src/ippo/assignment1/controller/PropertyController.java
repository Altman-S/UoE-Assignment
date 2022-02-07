// IPPO Assignment 1, Version 20。3, 08/10/2021
package ippo.assignment1.controller;

import ippo.assignment1.library.controller.Controller;
import ippo.assignment1.library.Picture;
import ippo.assignment1.library.service.Service;
import ippo.assignment1.library.service.ServiceFromProperties;
import ippo.assignment1.library.utils.Properties;
import ippo.assignment1.library.view.View;
import ippo.assignment1.library.view.ViewFromProperties;
import ippo.assignment1.library.utils.HashMap;


/**
 * A simple controller for the PictureViewer application.
 *
 * @author Paul Anderson &lt;dcspaul@ed.ac.uk&gt;
 * @version 20.3, 21/12/2020
 */
public class PropertyController implements Controller {

    private View view;
    private Service service;

    // store the correspondence between the selection identifier and the name
    private final HashMap<Integer,String> selectionNames = new HashMap<Integer,String>();

    /**
     * Start the controller.
     */
    public void start() {

        // create the view, the service and properties objects
        view = new ViewFromProperties(this);
        service = new ServiceFromProperties();
        Properties properties = new Properties();

        // get values in controller.subjects
        String propertyValue = properties.get("controller.subjects");
        String[] tokens = propertyValue.split(",");

        // create many selections from "controller.subjects" in the interface
        for (String s: tokens) {
            s = s.trim();
            addSubject(s);
        }

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
        picture = service.getPicture(selectionNames.get(selectionID), 1);

        // show the picture in the interface
        view.showPicture(picture);
    }

    /**
     * adds a selection to the interface
     * adds a corresponding entry to the HashMap
     *
     * @param namePicture the name of picture in Munro
     */
    public void addSubject(String namePicture) {

        // create one selection in the interface
        int selectionAdded = view.addSelection(namePicture);

        // create correspondence between the selection identifier and the name
        selectionNames.put(selectionAdded, namePicture);
    }

}

