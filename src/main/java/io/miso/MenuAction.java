package io.miso;

public interface MenuAction {
    public String getText();

    public MenuActionImpl setText(final String text);

    public int getId();

    public MenuActionImpl setId(final int id);

    public void exec(final String[] args);

    public void print(final boolean newLine);
}
