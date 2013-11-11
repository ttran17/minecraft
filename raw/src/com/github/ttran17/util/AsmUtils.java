package com.github.ttran17.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;

public class AsmUtils {

	public static final String base = "/home/ttran/Projects/decompile/raw/";
	
	public static final String version = "1.7.2";
	
	public static final String rawDir = base + version + "/";
	
	public static void main(String[] args) {
		find();
		
//		parseClass("a.class");
		
//		parseClass("azd.class"); // Looks like this is main minecraft class!
//								 // bcd.class = GuiScreen
//								 // blz.class = Tessellator
		
//		parseClass("mm.class"); // mm.class = EntityPlayerMP
							    // mx.class = NetServerHandler
								// ld.class = ServerConfigurationManager
								// li.class = DedicatedPlayerList
								// fi.class = ChatMessageComponent
								// ga.class = Packet3Chat
								// xl.class = AbstractClientPlayer
								// rh.class = EntityLivingBase
//        in mm.class:	
//		  public void b(fa paramfa)
//		  {
//		    this.a.a(new ga(paramfa));
//		  }
		
//		parseClass("xl.class");
	}
	
	public static void find() {
		Map<String,String[]> signatures = new TreeMap<>();
//		signatures.put("INVOKEVIRTUAL", new String[] {"java/nio/ByteBuffer", "asIntBuffer", "()Ljava/nio/IntBuffer;"});
//		signatures.put("INVOKEVIRTUAL", new String[] {"java/nio/ByteBuffer", "asFloatBuffer", "()Ljava/nio/FloatBuffer;"});
//		signatures.put("INVOKEVIRTUAL", new String[] {"java/nio/ByteBuffer", "asShortBuffer", "()Ljava/nio/ShortBuffer;"});
//		signatures.put("INVOKESTATIC", new String[] {"org/lwjgl/opengl/GLContext", "getCapabilities", "()Lorg/lwjgl/opengl/ContextCapabilities;"});
//		signatures.put("INVOKESTATIC", new String[] {"org/lwjgl/opengl/ARBVertexBufferObject", "glGenBuffersARB", "(Ljava/nio/IntBuffer;)V"});
//		signatures.put("GETFIELD", new String[] {"org/lwjgl/opengl/ContextCapabilities", "GL_ARB_vertex_buffer_object", "Z"});
		
//		// Signature for Gui Container ...
//		signatures.put("INVOKESPECIAL", new String[] {"bcd", "a", "IIF"});
//		signatures.put("visitLdcInsn", new String[] {"textures/gui/container/inventory.png"});
			
		// EntityPlayerMP
//		signatures.put("visitLdcInsn", new String[] {"\"tell\""});
//		signatures.put("visitLdcInsn", new String[] {"\"help\""});
//		signatures.put("visitLdcInsn", new String[] {"\"me\""});
		
//		signatures.put("visitLdcInsn", new String[] {"commands.generic.usage"});
		
//		signatures.put("visitLdcInsn", new String[] {"Failed to load"});
		
//		signatures.put("visitLdcInsn", new String[] {"commandBlock"});
		
		signatures.put("visitLdcInsn", new String[] {"steve.png"});
		signatures.put("visitLdcInsn", new String[] {"minecraft.net"});
		
		ClassSignature tessellator = new ClassSignature(signatures);
		
		File dir = new File(rawDir);
		for (String classfile : dir.list()) {
			if (!classfile.endsWith(".class")) {
				System.out.println("Skipping " + classfile);
				continue;
			}
			parseClass(classfile, tessellator);
		}
	}
	
	private static void parseClass(String classfile) {
        FileInputStream fis = null;
		try {		
			fis = new FileInputStream(rawDir + classfile);
			ClassReader cr = new ClassReader(fis);
	        cr.accept(new TraceClassVisitor(null, 
	        		new ASMifier(), 
	        		new PrintWriter(System.out)),
	        		ClassReader.SKIP_DEBUG);	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void parseClass(String classfile, ClassSignature signature) {
        FileInputStream fis = null;
		try {		
			fis = new FileInputStream(rawDir + classfile);
			PrintWriter pw = new PrintWriter(new FileWriter(new File("tmp")));			
			ClassReader cr = new ClassReader(fis);
	        cr.accept(new TraceClassVisitor(null, 
	        		new ASMifier(), 
	        		pw),
	        		ClassReader.SKIP_DEBUG);	        
	        pw.close();
	        
	        BufferedReader reader = new BufferedReader(new FileReader(new File("tmp")));
	       	int matches = signature.check(reader);
	       	if (matches >= signature.getMinMatches()) {
	       		System.out.println("Possible match: " + classfile);
	       	}
	        reader.close();	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
