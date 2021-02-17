package com.foxminded.SQL.menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Menu {
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
            this.print();
            item = this.getUserInput();
            item.invoke();
        }
        while (!item.isExitItem());
    }

    private int getExitIndex() {
        return this.itemList.size() + 1;
    }

    private MenuItem getUserInput() {
        MenuItem item = null;
        String input = null;

        try {
            input = this.in.readLine();
            int option = Integer.parseInt(input);

            if (option < 1 || option > this.getExitIndex()) {
                throw new NumberFormatException();
            }
            if (option == this.getExitIndex()) {
                item = exitItem;

                if (this.isRootMenu) {
                    this.in.close();
                }
            } else {
                item = itemList.get(option - 1);
            }
        } catch (NumberFormatException ex) {

            System.out.println("\nError: '" + input + "' is not a valid menu option!");
            item = new MenuItem(null);

            ConsoleUtils.pauseExecution();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            return item;
        }
    }

    private void print() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");

        if (!this.title.equals("")) {
			sb.append(this.title).append("\n");
		}

        for (int i = 0; i < this.itemList.size(); i++) {
			sb.append("\n").append(i + 1).append("... ")
					.append(this.itemList.get(i).getLabel());
		}

        sb.append("\n").append(getExitIndex())
                .append("... ")
                .append(exitItem.getLabel())
                .append("\n> ");

        System.out.print(sb.toString());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        return "menu=[" + this.title + "]  items=" + this.itemList.toString();
    }
}
