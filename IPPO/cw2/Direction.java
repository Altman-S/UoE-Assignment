package ippo.assignment2;

import java.util.HashMap;

/**
 * Class Direction: the direction which can show pictures in one Location
 * The default direction is North, South, East, West and Down, so you could
 * see 5 different pictures in one Location.
 * And if you want more directions(30°, 60°), just extend it
 *
 * @author Pai Peng
 * @version 2021.11.19
 */
public class Direction {

    // the value of HashMap is a sign to show the position of item
    // 0 means that there is no item
    private HashMap<String, Integer> directions = new HashMap<>();
    private static final String[] directionsDefault = {
            "North", "South", "East", "West"
    };

    /**
     * Create a default Direction
     */
    public Direction() {
        for (int i = 0; i < directionsDefault.length; i++) {
            directions.put(directionsDefault[i], 0);
        }
    }

    /**
     * Add a new direction to Location's Direction
     *
     * @param direction the new direction of the Direction
     */
    public void addDirection(String direction) {
        directions.put(direction, 0);
    }


}
