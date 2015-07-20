package org.nowireless.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;

import jline.console.ConsoleReader;

public class CommandProcessor {
	
	public interface Command {
		public void exec(List<String> args, ConsoleReader reader) throws Exception;
		public String getDescription();
	}
	
	public static class ClearScreenCommand implements Command {
		public void exec(List<String> args, ConsoleReader reader) throws Exception { reader.clearScreen(); }
		public String getDescription() { return "Clears the console screen"; }
	}
	
	public static class ExitCommand implements Command {
		public void exec(List<String> args, ConsoleReader reader) throws Exception { System.exit(0); }
		public String getDescription() { return "Exit the JVM"; }
	}
	
	public class HelpCommand implements Command {

		public void exec(List<String> args, ConsoleReader reader) throws Exception {
			log.info("Showing help");
			for(Entry<String, Command> entry : commands.entrySet()) {
				log.info("{} - {}", entry.getKey(), entry.getValue().getDescription());
			}
		}

		public String getDescription() {
			return "Shows this help page";
		}
		
	}
	
	private final Map<String, Command> commands = new HashMap<String, CommandProcessor.Command>();
	private final Logger log = LogManager.getLogger();
	
	public CommandProcessor() {
		registerCommand("help", new HelpCommand());
	}
	
	public void process(String line, ConsoleReader reader) {
		try {
			List<String> givenCommand = this.parse(line);
			log.trace("Command {}", givenCommand);
			if(givenCommand.isEmpty()) {
				return;
			}
			
			Command command = commands.get(givenCommand.get(0));
			if(command == null) {
				log.warn("Command not found");
				return;
			}
		
			givenCommand.remove(0);
			
			log.trace("Args to command: {}", givenCommand);
			
			log.trace("Exec command");
			command.exec(ImmutableList.copyOf(givenCommand), reader);
			
		} catch (Exception e) {
			log.error("Could not process command");
			e.printStackTrace();
		}
		
	}
	
	public List<String> parse(String line) {
		List<String> args = new ArrayList<String>(Arrays.asList(line.split(" ")));
		for(int i = 0; i < args.size(); i++) {
			String arg = args.get(i);
			if(arg.contains(" ") || arg.isEmpty()) {
				args.remove(i);
			}
		}
		
		return args;
	}
	
	public void registerCommand(String cmdName, Command cmd) {
		Validate.notNull(cmd);
		Validate.notEmpty(cmdName);
		if(cmdName.contains(" ")) {
			throw new RuntimeException("Commands can not have spaces in their name");
		}
		
		log.trace("Registering command {}, {}", cmdName, cmd.getClass());
		commands.put(cmdName, cmd);
		
	}
}
