package org.nowireless.console;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class CommandProcessorTest {

	public static Logger LOG = LogManager.getLogger();
	
	public static void assertParse(String line, List<String> expected) {
		CommandProcessor commandProcessor = new CommandProcessor();
		List<String> parsed = commandProcessor.parse(line);
		
		LOG.info("Given: [{}]", line);
		LOG.info("Expected: {}", expected);
		LOG.info("Parsed:   {}", parsed);
		
		assertEquals(expected.size(), parsed.size());
		
		for(int i = 0; i < expected.size(); i++) {
			assertTrue(expected.get(i).equals(parsed.get(i)));
		}
	}
	
	@Test
	public void test() {
		assertParse("", ImmutableList.<String>of());
		assertParse(" ", ImmutableList.<String>of());
		assertParse("   ", ImmutableList.<String>of());
		assertParse("a", ImmutableList.<String>of("a"));
		assertParse("a a", ImmutableList.<String>of("a", "a"));
		assertParse("a  a", ImmutableList.<String>of("a", "a"));
		assertParse("a  ", ImmutableList.<String>of("a"));
		assertParse("a a  ", ImmutableList.<String>of("a", "a"));
		assertParse("a  a  ", ImmutableList.<String>of("a", "a"));
	}

}
