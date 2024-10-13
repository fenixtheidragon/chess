package org.example.game;

import org.example.exceptions.IllegalCoordinateException;
import org.example.exceptions.ParsingException;

public class Parser {
    public static int[] parse(
            String move
    ) throws IllegalCoordinateException, ParsingException {

        String[] coordinates = move.split("-");
        if (coordinates.length == 2) {
            char letterChar = coordinates[0].charAt(0);
            char digitChar = coordinates[0].charAt(1);
            int firstLetterCoordinate = letterCharToInt(letterChar);
            int firstDigitCoordinate = digitCharToInt(digitChar);

            letterChar = coordinates[1].charAt(0);
            digitChar = coordinates[1].charAt(1);
            int secondLetterCoordinate = letterCharToInt(letterChar);
            int secondDigitCoordinate = digitCharToInt(digitChar);

            return new int[]{
                    firstLetterCoordinate, firstDigitCoordinate,
                    secondLetterCoordinate, secondDigitCoordinate
            };
        }
        throw new ParsingException();
    }

    private static int letterCharToInt(char character) throws IllegalCoordinateException {
        return switch (character) {
            case 'a' -> 0;
            case 'b' -> 1;
            case 'c' -> 2;
            case 'd' -> 3;
            case 'e' -> 4;
            case 'f' -> 5;
            case 'g' -> 6;
            case 'h' -> 7;
            default -> throw new IllegalCoordinateException("letter");
        };
    }

    private static int digitCharToInt(char character) throws IllegalCoordinateException {
        return switch (character) {
            case '1' -> 0;
            case '2' -> 1;
            case '3' -> 2;
            case '4' -> 3;
            case '5' -> 4;
            case '6' -> 5;
            case '7' -> 6;
            case '8' -> 7;
            default -> throw new IllegalCoordinateException("digit");
        };
    }
}
