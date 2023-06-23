package io.miso;

public class HumanPlayer implements Player {
    private final Colour colour;

    public HumanPlayer(Colour colour) {
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
