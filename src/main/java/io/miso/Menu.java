package io.miso;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final List<MenuAction> menuActionList = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public Menu() {
        init();
    }

    private void init() {
        menuActionList.add(new StartGameAction("Start new game", 0));
        menuActionList.add(new ExitGameAction("Exit game!", 1));
    }

    public void start() {
        System.out.println("<- Menu ->");

        for (final MenuAction ma : menuActionList) {
            ma.print(true);
        }

        System.out.print("#> ");

        switch (scanner.nextInt()) {
            case 0: {
                menuActionList.get(0).exec(new String[0]);
                break;
            }
            case 1: {
                menuActionList.get(1).exec(new String[0]);
                break;
            }
            default: {
                menuActionList.get(1).exec(new String[0]);
                break;
            }
        }
    }
}
