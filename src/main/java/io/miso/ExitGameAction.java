package io.miso;

public class ExitGameAction extends MenuActionImpl {
    private String[] args;
    
    public ExitGameAction(String text, int id) {
        super(text, id);
    }
    
    @Override
    public void exec(final String[] args) {
        this.args = args;
        System.exit(0);
    }
}
