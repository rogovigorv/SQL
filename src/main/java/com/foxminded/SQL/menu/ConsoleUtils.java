package com.foxminded.SQL.menu;

import java.io.Console;

public class ConsoleUtils {
	private static final Console console = System.console();

	public static void pauseExecution() {
		System.out.print("Press Enter to Continue... ");
		console.readLine();
	}
}
