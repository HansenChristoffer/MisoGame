package io.miso.entities;

public enum Colour {
    NONE(-1),
    WHITE(0),
    BLACK(1);

    private final int id;

    Colour(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Colour getById(final int id) {
        for (final Colour c : values()) {
            if (c.getId() == id) {
                return c;
            }
        }

        return NONE;
    }
}
