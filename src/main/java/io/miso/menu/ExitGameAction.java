package io.miso.menu;

public class ExitGameAction extends MenuActionImpl {
    private String[] args;

    public ExitGameAction(final String text, final int id) {
        super(text, id);
    }

    @Override
    public void exec(final String[] args) {
        this.args = args;
        System.exit(0);
    }
}
