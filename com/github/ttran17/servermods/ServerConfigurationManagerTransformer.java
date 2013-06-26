package com.github.ttran17.servermods;

import java.util.logging.Level;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.IClassTransformer;
import static org.objectweb.asm.Opcodes.*;

public class ServerConfigurationManagerTransformer implements IClassTransformer {
	
	public final String className = "gm";

	@Override
	public byte[] transform(String name, byte[] bytes) {
		if (className.equals(name)) {
			FMLRelaunchLog.log(Level.INFO,"Modifying " + name + " which is ServerConfiguratonManager");
			ClassReader cr = new ClassReader(bytes);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			ServerConfigurationManagerVisitor visitor = new ServerConfigurationManagerVisitor(Opcodes.ASM4, cw);
			cr.accept(visitor, 0);
			bytes = cw.toByteArray();
		}
		return bytes;
	}

	public class ServerConfigurationManagerVisitor extends ClassVisitor {

		public ServerConfigurationManagerVisitor(int api, ClassVisitor cv) {
			super(api, cv);
		}		

		@Override
		public void visitEnd() {
			FMLRelaunchLog.log(Level.FINE,"Visiting isGod / isOps commands ...");
			visitField("gods");
			visitField("superOps");
			visitField("godCommands");
			visitField("superOpCommands");
			
			visitMethod("isGod","gods");
			visitMethod("isSuperOp","superOps");
			visitMethod("isGodCommand","godCommands");
			visitMethod("isSuperOpCommand","superOpCommands");
			
			super.visitEnd();
		}
		
		private void visitField(String name) {
			FieldVisitor fv = super.visitField(ACC_PUBLIC, name, "Ljava/util/Set;", null, null);
			fv.visitEnd();
		}
		
		private void visitMethod(String methodName, String setName) {
			MethodVisitor mv = super.visitMethod(ACC_PUBLIC, methodName, "(Ljava/lang/String;)Z", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gm", setName, "Ljava/util/Set;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "trim", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "toLowerCase", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "contains", "(Ljava/lang/Object;)Z");
			mv.visitInsn(IRETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();			
		}
	}
}
