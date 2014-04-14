package com.github.ttran17.basemods.finerops;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CommandLevelInsertionTransformer extends CommandLevelTransformer {
	
	private static final Logger LOGGER = LogManager.getLogger();

	public CommandLevelInsertionTransformer(int level) {
		super(level);
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		LOGGER.info("Modifying " + name + " which is the class for command " + transformedName);
		ClassReader cr = new ClassReader(bytes);
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		LevelInsertor visitor = new LevelInsertor(Opcodes.ASM4, cw);
		cr.accept(visitor, 0);
		bytes = cw.toByteArray();
		
		return bytes;
	}

	private class LevelInsertor extends ClassVisitor {
		public LevelInsertor(int api, ClassVisitor cv) {
			super(api, cv);
		}

		@Override
		public void visitEnd() {
			LOGGER.info("Inserting command level ...");
			MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, "a", "()I", null, null);
			newMethod(mv);
			cv.visitEnd();
		}              
	}
}
