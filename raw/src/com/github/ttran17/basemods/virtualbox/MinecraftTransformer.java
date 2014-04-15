package com.github.ttran17.basemods.virtualbox;

import static org.objectweb.asm.Opcodes.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.github.ttran17.basemods.IClassTransformer;

public class MinecraftTransformer implements IClassTransformer {

	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String checkGLError_name = "b"; //"checkGLError";
	public static final String checkGLError_desc = "(Ljava/lang/String;)V";

	public final String Minecraft_classname; // Minecraft obfuscated class name
	
	public MinecraftTransformer(String className) {
		this.Minecraft_classname = className;
	}
	
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		LOGGER.info("Modifying " + name + " which is " + transformedName);
		ClassReader cr = new ClassReader(bytes);
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		MinecraftVisitor ma = new MinecraftVisitor(Opcodes.ASM4, cw);
		cr.accept(ma, 0);
		bytes = cw.toByteArray();

		return bytes;
	}

	public class MinecraftVisitor extends ClassVisitor {

		public MinecraftVisitor(int api, ClassWriter cv) {
			super(api, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			if (checkGLError_name.equals(name) && checkGLError_desc.equals(desc)) {
				LOGGER.info("Visiting checkGLError ...");
				newMethod(mv);
				return null;
			} 
			return mv;
		}

		/**
		 * VirtualBoxOpenGLCursor.checkGLError(String par1)
		 */
		public void newMethod(MethodVisitor mv) {
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "VirtualBoxOpenGLCursor", "checkGLError", "(Ljava/lang/String;)V");
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 2);
			mv.visitEnd();
		}
	}
}
