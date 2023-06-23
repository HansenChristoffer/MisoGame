package io.miso.entities;

public class HumanPlayer implements Player {
    private final Colour colour;

    public HumanPlayer(final Colour colour) {
        this.colour = colour;
    }

    @Override
    public Colour getColour() {
        return colour;
    }

    @Override
    public String toString() {
        return "" + getColour();
    }
}
