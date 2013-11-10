package com.github.ttran17.blockmods.xray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import net.minecraft.block.Block;

public class SmartXrayBuilder {

	private static final String base = "/home/ttran/Projects/"; ;
	private static final String forge = "forge-9.10.0.804";
	private static final String mcp = "/mcp/src/minecraft/";
	private static final String readPath = base + forge + mcp + "net/minecraft/block";
	private static final String writePath = base + forge + mcp + "com/github/ttran17/blockmods/xray/staging";
	
	public static void main(String[] args) {
		File infile = new File(readPath, "Block.java");
		File outfile = new File(writePath, "SmartXrayBlock.java");
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outfile));
			BufferedReader reader = new BufferedReader(new FileReader(infile));
			String line = reader.readLine();
			while (line != null) {
				if (line.trim().startsWith("public static final") 
						|| line.trim().startsWith("protected static int")
						|| line.trim().startsWith("public static boolean")) {
					if (line.trim().endsWith(";")) {
						line = reader.readLine();
						continue;
					}
				}
				
				if (line.trim().startsWith("package")) {
					writer.write("package com.github.ttran17.blockmods.xray.staging;");
					writer.write("\n");
				} else if (line.trim().startsWith("import net.minecraft.block.material.Material")) {
					writer.write("import net.minecraft.block.Block;");
					writer.write("\n"); ;
					writer.write("import net.minecraft.block.StepSound;");
					writer.write("\n"); ;
					writer.write(line.trim());
					writer.write("\n");
				} else if (line.trim().startsWith("public class Block")) {
					writer.write("public class SmartXrayBlock extends Block");
					writer.write("\n");
					
					line = reader.readLine();
					writer.write(line.trim());
					writer.write("\n");
					
					writer.write("public final Block actual;");
					writer.write("\n\n");
					writer.write("public SmartXrayBlock(int blockID, Block actual)");
					writer.write("\n{\n");
					writer.write("super(blockID, actual.blockMaterial);\n");
					writer.write("this.actual = actual;");
					writer.write("\n}\n");					
				} else if (line.trim().startsWith("public Block(")) {
					Flag flag = new Flag();
					while (line != null) {
						stripMethodBody(line, flag);
						if (flag.count == 0 && flag.found) {
							break;
						}
						line = reader.readLine();
					}
				} else if (line.trim().startsWith("public") ||
						line.trim().startsWith("protected") || 
						line.trim().startsWith("private")) {
					if (line.endsWith(";")) {
						writer.write(line.trim());
						writer.write("\n");
						
						line = reader.readLine();
						continue;
					}
				} else if (line.trim().endsWith("{}")) {
					//List<String> methodInfo getMethodInfo(line);
					
				
				} else if (line.trim().startsWith("public") ||
						line.trim().startsWith("protected") || 
						line.trim().startsWith("private")) {
					if (line.endsWith(";")) {
						writer.write(line.trim());
						writer.write("\n");
						
						line = reader.readLine();
						continue;
					}
				} else {
					writer.write(line.trim());
					writer.write("\n");
				}

				line = reader.readLine();
			}
			
			reader.close();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void stripMethodBody(String line, Flag flag) throws IOException {		
		char[] charArray = line.trim().toCharArray();
		for (char ch : charArray) {
			if (ch == '{') {
				flag.count++;
				flag.found = true;
			} else if (ch == '}') {
				flag.count--;
			}
		}
	}
	
	private static class Flag {
		private int count = 0;
		private boolean found = false;
	}
}
