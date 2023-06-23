package io.miso.menu;

public abstract class MenuActionImpl implements MenuAction {
    private String text;
    private int id;

    public MenuActionImpl(final String text, final int id) {
        this.text = text;
        this.id = id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public MenuActionImpl setText(final String text) {
        this.text = text;
        return this;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public MenuActionImpl setId(final int id) {
        this.id = id;
        return this;
    }

    @Override
    public void exec(final String[] args) {
        // DO SOMETHING
    }

    @Override
    public void print(final boolean newLine) {
        System.out.print(String.format("%d: %s%s", id, text, (newLine ? "\n" : "")));
    }
}
