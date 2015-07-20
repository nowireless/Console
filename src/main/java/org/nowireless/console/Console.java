package org.nowireless.console;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jline.console.ConsoleReader;

public class Console {
	
	private final ConsoleReader reader;
	private final Logger log = LogManager.getLogger();
	
	private final ConsoleThread thread;
	private final CommandProcessor processor = new CommandProcessor();
	
	public CommandProcessor getProcessor() {
		return this.processor;
	}
	
	public Console() {
		try {
			this.reader = new ConsoleReader(System.in, System.out);
		} catch (IOException e) {
			throw new RuntimeException("Could not create console reader", e);
		}
		reader.setPrompt(">");
		
		this.thread = new ConsoleThread();
		
	}
	
	public void start() {
		log.info("Starting Console");
		thread.start();
	}
	
	private class ConsoleThread extends Thread {
		
		public ConsoleThread() {
			super("Console Thread");
		}
		
		@Override
		public void run() {
			String line;
			try {
				while(((line = reader.readLine()) != null)) {
					processor.process(line, reader);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
