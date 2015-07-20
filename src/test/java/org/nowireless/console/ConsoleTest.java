package org.nowireless.console;

public class ConsoleTest {
	
	public static void main(String[] args) {
		Console console = new Console();
		console.getProcessor().registerCommand("clear", new CommandProcessor.ClearScreenCommand());
		console.getProcessor().registerCommand("exit", new CommandProcessor.ExitCommand());
		
		console.start();
	} 
}
