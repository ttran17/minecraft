package com.github.ttran17.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class ClassSignature {
	
	private final Map<String,String[]> signatures;
	
	private final int minMatches;
	
	public ClassSignature(Map<String,String[]> signatures) {
		this.signatures = signatures;
		this.minMatches = signatures.size();
	}
	
	public ClassSignature(Map<String,String[]> signatures, int minMatches) {
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
			for (Map.Entry<String,String[]> entrySet : signatures.entrySet()) {
				String key = entrySet.getKey();
				String[] values = entrySet.getValue();				
				if (line.contains(key)) {
					int count = 0;
					for (String value : values) {
						if (line.contains(value)) {
							System.out.println(line);
							count++;
						}
					}
					if (count == values.length) {
						matches++;
					}
				}
			}
			line = reader.readLine();
		}		
		return matches;
	}
}
