package com.github.ttran17.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;

public class MinecraftLibBuilder {

	public static void main(String[] args) throws IOException, InvalidSyntaxException {
		System.out.println("Json entries ...");

		String version = "1.7.2";
		File file = new File("/home/ttran/.minecraft/versions/"+version,version+".json");

		BufferedReader reader = new BufferedReader(new FileReader(file));
		JdomParser jdom = new JdomParser();
		JsonRootNode node = jdom.parse(reader);

		List<JsonNode> elements = node.getArrayNode("libraries");
		List<String> names = new ArrayList<>();
		for (JsonNode e : elements) {
			System.out.println("    " + e.getStringValue("name"));
			names.add(e.getStringValue("name"));
		}
		toLibEntry(names);

		reader.close();
	}

	private static void toLibEntry(List<String> names) {
		System.out.println();

		Iterator<String> iter = names.iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			if (name.contains("-nightly-")) {
				System.out.println("    Skipping " + name);
				iter.remove();
			}
		}
		System.out.println();
		System.out.println("classpath entries:");

		for (String name : names) {
			String path = transform(name);
			System.out.println("    <classpathentry kind=\"lib\" path=\"libraries/" + path + "\"/>");
		}
	}

	private static String transform(String name) {
		StringBuilder path = new StringBuilder();
		String[] parts = name.trim().split(":");
		String base = parts[0].replace(".","/");
		path.append(base);
		for (int i = 1; i < parts.length; i++) {
			path.append("/");
			path.append(parts[i]);
		}
		path.append("/");
		for (int i = 1; i < parts.length; i++) {
			path.append(parts[i]);
			if (i < parts.length - 1) {
				path.append("-");
			}
		}
		if (name.contains("-platform")) {
			path.append("-natives-linux");
		}
		path.append(".jar");

		return path.toString();
	}
}
