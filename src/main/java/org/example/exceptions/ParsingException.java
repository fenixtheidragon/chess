package org.example.exceptions;

public class ParsingException extends Exception {
    public ParsingException() {
        super("Parsing failed because two coordinates must be written like this: \"e2-e4\"");
    }
}
