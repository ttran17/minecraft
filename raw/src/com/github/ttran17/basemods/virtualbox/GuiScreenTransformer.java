package com.github.ttran17.basemods.virtualbox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.github.ttran17.basemods.IClassTransformer;

import static org.objectweb.asm.Opcodes.*;

public class GuiScreenTransformer implements IClassTransformer {

	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String Minecraft_ref = "k"; // Reference to Minecraft in GuiScreen
	public static final String Minecraft_displayWidth = "d";
	public static final String Minecraft_displayHeight = "e";

	public static final String drawScreen_name = "a"; 
	public static final String drawScreen_desc = "(IIF)V";

	public static final String GuiScreen_width = "l";
	public static final String GuiScreen_height = "m";
	
	public final String GuiScreen_classname; // // GuiScreen obfuscated class name
	public final String Minecraft_classname;
	
	public GuiScreenTransformer(String className, String minecraftClassName) {
		this.GuiScreen_classname = className;
		this.Minecraft_classname = minecraftClassName;
	}

	public byte[] transform(String name, String transformedName, byte[] bytes) {
		LOGGER.info("Modifying " + name + " which is " + transformedName);
		ClassReader cr = new ClassReader(bytes);
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		GuiScreenVisitor gsv = new GuiScreenVisitor(Opcodes.ASM4, cw);
		cr.accept(gsv, 0);
		bytes = cw.toByteArray();

		return bytes;
	}

	public class GuiScreenVisitor extends ClassVisitor {

		public GuiScreenVisitor(int api, ClassWriter cv) {
			super(api, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			if (GuiScreenTransformer.drawScreen_name.equals(name) && GuiScreenTransformer.drawScreen_desc.equals(desc)) {
				LOGGER.info("Visiting drawScreen ...");
				mv = new DrawScreenVisitor(Opcodes.ASM4, mv);
			}                          
			return mv;
		}
	}

	public class DrawScreenVisitor extends MethodVisitor {

		public DrawScreenVisitor(int api, MethodVisitor mv) {
			super(api, mv);
		}

		@Override
		/**
		 * VirtualBoxOpenGLCursor.drawCursor(this.width, this.height, this.mc.displayWidth, this.mc.displayHeight);
		 */
		public void visitInsn(int opcode) {
			if (opcode == RETURN) {
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, GuiScreen_classname, GuiScreen_width, "I");
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, GuiScreen_classname, GuiScreen_height, "I");
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, GuiScreen_classname, Minecraft_ref, "L"+Minecraft_classname+";");
				mv.visitFieldInsn(GETFIELD, Minecraft_classname, Minecraft_displayWidth, "I");
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, GuiScreen_classname, Minecraft_ref, "L"+Minecraft_classname+";");
				mv.visitFieldInsn(GETFIELD, Minecraft_classname, Minecraft_displayHeight, "I");
				mv.visitMethodInsn(INVOKESTATIC, "VirtualBoxOpenGLCursor", "drawCursor", "(IIII)V");
			}                 
			mv.visitInsn(opcode);               
		}
	}
}