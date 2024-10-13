package org.example;

public enum Color {
    WHITE,
    BLACK;

    public static Color getAnother(Color color) {
        if (color.equals(WHITE)) {
            return BLACK;
        }
        return WHITE;
    }
}
