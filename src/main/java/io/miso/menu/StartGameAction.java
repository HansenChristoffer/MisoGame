package io.miso.menu;

import io.miso.core.GameLogic;

import java.util.Scanner;

public class StartGameAction extends MenuActionImpl {
    private final Scanner scanner = new Scanner(System.in);

    private String[] args;

    public StartGameAction(final String text, final int id) {
        super(text, id);
    }

    @Override
    public void exec(final String[] args) {
        this.args = args;
        System.out.println("Starting game...");

        System.out.println("Play against computer? [Y/N]");
        final String input = scanner.next();

        final GameLogic gameLogic = new GameLogic(input.equalsIgnoreCase("Y"));
        gameLogic.cycle();
    }
}
