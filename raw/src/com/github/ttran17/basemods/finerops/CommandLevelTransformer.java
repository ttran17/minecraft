package com.github.ttran17.basemods.finerops;

import static org.objectweb.asm.Opcodes.ICONST_5;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.IRETURN;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.github.ttran17.basemods.IClassTransformer;

public class CommandLevelTransformer implements IClassTransformer {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	protected final int level;
	
	public CommandLevelTransformer(int level) {
		this.level = level;
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		LOGGER.info("Modifying " + name + " which is the class for command " + transformedName);
		ClassReader cr = new ClassReader(bytes);
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		CommandVisitor visitor = new CommandVisitor(Opcodes.ASM4, cw);
		cr.accept(visitor, 0);
		bytes = cw.toByteArray();
		
		return bytes;
	}

	private class CommandVisitor extends ClassVisitor {
		public final String name = "a"; // returns the command level;
		public final String desc = "()I";

		public CommandVisitor(int api, ClassVisitor cv) {
			super(api, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			if (this.name.equals(name) && this.desc.equals(desc)) {
				LOGGER.info("Visiting command level ...");
				newMethod(mv);
				return null;
			}                          
			return mv;
		}                           
	}
	
	public void newMethod(MethodVisitor mv) {
		mv.visitCode();
		if (level >= 6) {
			mv.visitIntInsn(BIPUSH, level);
		} else if (level == 5) {
			mv.visitInsn(ICONST_5);
		} else {
			throw new IllegalArgumentException("Can only support levels >= 5");
		}
		mv.visitInsn(IRETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}  
}
