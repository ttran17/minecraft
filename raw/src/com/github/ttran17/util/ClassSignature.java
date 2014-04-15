package com.github.ttran17.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClassSignature {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final List<Signature> signatures;
	
	private final int minMatches;
	
	public ClassSignature(List<Signature> signatures) {
		this.signatures = signatures;
		this.minMatches = signatures.size();
	}
	
	public ClassSignature(List<Signature> signatures, int minMatches) {
		this.signatures = signatures;
		this.minMatches = minMatches;
	}
	
	public int getMinMatches() {
		return minMatches;
	}

	public int check(BufferedReader reader) throws IOException {
		int matches = 0;
		String line = reader.readLine();
		while (line != null) {
			for (Signature signature : signatures) {
				if (line.contains(signature.key)) {
					int count = 0;
					for (String value : signature.values) {
						if (line.contains(value)) {
							// LOGGER.info(line);
							count++;
						}
					}
					if (count == signature.values.length) {
						LOGGER.info(line);
						matches++;
					}
				}
			}
			line = reader.readLine();
		}		
		return matches;
	}
	
	public static class Signature {
		private final String key;
		private final String[] values;
		
		public Signature(String key, String[] values) {
			this.key = key;
			this.values = values;
		}
	}
}
