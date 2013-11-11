package com.github.ttran17.basemods.finerops;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.github.ttran17.basemods.IClassTransformer;

import static org.objectweb.asm.Opcodes.*;

public class DedicatedPlayerListTransformer implements IClassTransformer {

	private static final Logger LOGGER = LogManager.getLogger();

	public static final String DedicatedPlayerList_classname = "li"; // net.minecraft.server.dedicated.DedicatedPlayerList
	public static final String DedicatedServer_classname = "lj";

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (DedicatedPlayerList_classname.equals(name)) {
			LOGGER.info("Modifying " + name + " which is " + transformedName);
			ClassReader cr = new ClassReader(bytes);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			DedicatedPlayerListVisitor visitor = new DedicatedPlayerListVisitor(Opcodes.ASM4, cw);
			cr.accept(visitor, 0);
			bytes = cw.toByteArray();
		}
		return bytes;
	}

	public class DedicatedPlayerListVisitor extends ClassVisitor {

		public final String name = "<init>"; // constructor;
		public final String desc = "(L"+DedicatedServer_classname +";)V";

		public DedicatedPlayerListVisitor(int api, ClassVisitor cv) {
			super(api, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			if (this.name.equals(name) && this.desc.equals(desc)) {
				LOGGER.info("Visiting constructor ...");
				mv = new ConstructorVisitor(Opcodes.ASM4, mv);
			}                          
			return mv;
		}                
	}

	public class ConstructorVisitor extends MethodVisitor {

		public ConstructorVisitor(int api, MethodVisitor mv) {
			super(api, mv);
		}

		@Override
		/**
		 * FinerOps.dedicatedPlayerList = this;
		 * FinerOps.load(dedicatedServer);
		 */
		public void visitInsn(int opcode) {
			if (opcode == RETURN) {
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(PUTSTATIC, "FinerOps", "dedicatedPlayerList", "L" + DedicatedPlayerList_classname + ";");
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKESTATIC, "FinerOps", "load", "(L" + DedicatedServer_classname + ";)V");
			}                 
			mv.visitInsn(opcode);               
		}
	}

}