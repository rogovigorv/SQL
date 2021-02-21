package com.foxminded.SQL.menu;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private static final Console console = System.console();
    private static final String LINE_BREAK = "\n";
    private static final String ELLIPSIS = "... ";
    private static final String EMPTY_STRING = "";
    private static final String CURSOR = "> ";

    private final BufferedReader in;
    private final List<MenuItem> itemList;
    private final MenuItem exitItem;
    private String title;
    private final boolean isRootMenu;

    public Menu() {
        this.itemList = new ArrayList<>();

        this.in = new BufferedReader(new InputStreamReader(System.in));

        this.isRootMenu = true;
        this.setTitle("Menu");
        this.exitItem = new MenuItem("Press '7' to exit");
        this.exitItem.setExitItem();
    }

    public void addItem(MenuItem item) {
        this.itemList.add(item);
    }

    public void execute() {
        MenuItem item;
        do {
            print();
            item = getUserInput();
            item.invoke();
        }
        while (!item.isExitItem());
    }

    private int getExitIndex() {
        return itemList.size() + 1;
    }

    private MenuItem getUserInput() {
        MenuItem item = null;
        String input = null;

        try {
            input = in.readLine();
            int option = Integer.parseInt(input);

            if (option < 1 || option > getExitIndex()) {
                throw new NumberFormatException();
            }
            if (option == getExitIndex()) {
                item = exitItem;

                if (isRootMenu) {
                    in.close();
                }
            } else {
                item = itemList.get(option - 1);
            }
        } catch (NumberFormatException ex) {

            System.out.println(LINE_BREAK + "Error: '" + input + "' is not a valid menu option!");
            item = new MenuItem(null);

            pauseExecution();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            return item;
        }
    }

    private void print() {
        StringBuilder sb = new StringBuilder();

        sb.append(LINE_BREAK);

        if (!title.equals(EMPTY_STRING)) {
			sb.append(title)
                    .append(LINE_BREAK);
		}

        for (int i = 0; i < itemList.size(); i++) {
			sb.append(LINE_BREAK)
                    .append(i + 1)
                    .append(ELLIPSIS)
					.append(itemList.get(i).getLabel());
		}

        sb.append(LINE_BREAK).append(getExitIndex())
                .append(ELLIPSIS)
                .append(exitItem.getLabel())
                .append(LINE_BREAK)
                .append(CURSOR);

        System.out.print(sb.toString());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        return "menu=[" + title + "]  items=" + itemList.toString();
    }

    private void pauseExecution() {
        System.out.print("Press Enter to Continue" + ELLIPSIS);
        console.readLine();
    }
}
