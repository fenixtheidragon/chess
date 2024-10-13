package org.example.exceptions;

public class IllegalCoordinateException extends Exception {

    public IllegalCoordinateException(String coordinateName) {
        super("Coordinate " + coordinateName + " is illegal.");
    }

    public IllegalCoordinateException(String coordinateName, int coordinateValue) {
        super("Coordinate " + coordinateName + " with value " + coordinateValue + " is illegal.");
    }
}
