package com.github.ttran17.servermods;

import java.util.logging.Level;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.IClassTransformer;
import static org.objectweb.asm.Opcodes.*;

public class DedicatedPlayerListTransformer implements IClassTransformer {

	public final String className = "hn";
	
	@Override
	public byte[] transform(String name, byte[] bytes) {
		if (className.equals(name)) {
			FMLRelaunchLog.log(Level.INFO,"Modifying " + name + " which is DedicatedPlayerList");
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
		public final String desc = "(Lho;)V";
		
		public DedicatedPlayerListVisitor(int api, ClassVisitor cv) {
			super(api, cv);
		}
		
		@Override
	    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			if (this.name.equals(name) && this.desc.equals(desc)) {
				FMLRelaunchLog.log(Level.FINE,"Visiting constructor ...");
				mv = new ConstructorVisitor(Opcodes.ASM4, mv);
			}  			
			return mv;
		}		
		
		@Override
		public void visitEnd() {
			FMLRelaunchLog.log(Level.FINE,"Visiting load god/ops list commands ...");
			visitLoadGodList();
			visitLoadSuperOpsList();
			visitLoadGodCommands();
			visitLoadSuperOpCommands();
			super.visitEnd();
		}
		
		public void visitLoadGodList() {
			MethodVisitor mv = super.visitMethod(ACC_PRIVATE, "loadGodList", "(Lho;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
			mv.visitLabel(l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn("gods.txt");
			mv.visitMethodInsn(INVOKEVIRTUAL, "ho", "e", "(Ljava/lang/String;)Ljava/io/File;");
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "java/util/HashSet");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashSet", "<init>", "()V");
			mv.visitFieldInsn(PUTFIELD, "hn", "gods", "Ljava/util/Set;");
			mv.visitTypeInsn(NEW, "java/io/BufferedReader");
			mv.visitInsn(DUP);
			mv.visitTypeInsn(NEW, "java/io/FileReader");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/FileReader", "<init>", "(Ljava/io/File;)V");
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/BufferedReader", "<init>", "(Ljava/io/Reader;)V");
			mv.visitVarInsn(ASTORE, 3);
			mv.visitLdcInsn("");
			mv.visitVarInsn(ASTORE, 4);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitFrame(Opcodes.F_APPEND,3, new Object[] {"java/io/File", "java/io/BufferedReader", "java/lang/String"}, 0, null);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedReader", "readLine", "()Ljava/lang/String;");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 4);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNULL, l4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "hn", "gods", "Ljava/util/Set;");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "trim", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "toLowerCase", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "add", "(Ljava/lang/Object;)Z");
			mv.visitInsn(POP);
			mv.visitJumpInsn(GOTO, l3);
			mv.visitLabel(l4);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "hn", "i", "()Ljava/util/Set;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "hn", "gods", "Ljava/util/Set;");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "addAll", "(Ljava/util/Collection;)Z");
			mv.visitInsn(POP);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedReader", "close", "()V");
			mv.visitLabel(l1);
			Label l5 = new Label();
			mv.visitJumpInsn(GOTO, l5);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_FULL, 2, new Object[] {"hn", "ho"}, 1, new Object[] {"java/lang/Exception"});
			mv.visitVarInsn(ASTORE, 2);
			mv.visitFieldInsn(GETSTATIC, "hn", "a", "Ljava/util/logging/Logger;");
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V");
			mv.visitLdcInsn("Failed to load gods list: ");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/logging/Logger", "warning", "(Ljava/lang/String;)V");
			mv.visitLabel(l5);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(5, 5);
			mv.visitEnd();
		}
		
		public void visitLoadSuperOpsList() {
			MethodVisitor mv = super.visitMethod(ACC_PRIVATE, "loadSuperOpsList", "(Lho;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
			mv.visitLabel(l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn("super-ops.txt");
			mv.visitMethodInsn(INVOKEVIRTUAL, "ho", "e", "(Ljava/lang/String;)Ljava/io/File;");
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "java/util/HashSet");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashSet", "<init>", "()V");
			mv.visitFieldInsn(PUTFIELD, "hn", "superOps", "Ljava/util/Set;");
			mv.visitTypeInsn(NEW, "java/io/BufferedReader");
			mv.visitInsn(DUP);
			mv.visitTypeInsn(NEW, "java/io/FileReader");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/FileReader", "<init>", "(Ljava/io/File;)V");
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/BufferedReader", "<init>", "(Ljava/io/Reader;)V");
			mv.visitVarInsn(ASTORE, 3);
			mv.visitLdcInsn("");
			mv.visitVarInsn(ASTORE, 4);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitFrame(Opcodes.F_APPEND,3, new Object[] {"java/io/File", "java/io/BufferedReader", "java/lang/String"}, 0, null);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedReader", "readLine", "()Ljava/lang/String;");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 4);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNULL, l4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "hn", "superOps", "Ljava/util/Set;");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "trim", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "toLowerCase", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "add", "(Ljava/lang/Object;)Z");
			mv.visitInsn(POP);
			mv.visitJumpInsn(GOTO, l3);
			mv.visitLabel(l4);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "hn", "i", "()Ljava/util/Set;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "hn", "superOps", "Ljava/util/Set;");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "addAll", "(Ljava/util/Collection;)Z");
			mv.visitInsn(POP);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedReader", "close", "()V");
			mv.visitLabel(l1);
			Label l5 = new Label();
			mv.visitJumpInsn(GOTO, l5);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_FULL, 2, new Object[] {"hn", "ho"}, 1, new Object[] {"java/lang/Exception"});
			mv.visitVarInsn(ASTORE, 2);
			mv.visitFieldInsn(GETSTATIC, "hn", "a", "Ljava/util/logging/Logger;");
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V");
			mv.visitLdcInsn("Failed to load super operators list: ");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/logging/Logger", "warning", "(Ljava/lang/String;)V");
			mv.visitLabel(l5);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(5, 5);
			mv.visitEnd();
		}
		
		public void visitLoadGodCommands() {
			MethodVisitor mv = super.visitMethod(ACC_PRIVATE, "loadGodCommands", "(Lho;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "hn", "gods", "Ljava/util/Set;");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "size", "()I");
			mv.visitJumpInsn(IFNE, l0);
			mv.visitFieldInsn(GETSTATIC, "hn", "a", "Ljava/util/logging/Logger;");
			mv.visitLdcInsn("No gods specified. Therefore, no commands reserved for gods.");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/logging/Logger", "warning", "(Ljava/lang/String;)V");
			mv.visitInsn(RETURN);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn("god-commands.txt");
			mv.visitMethodInsn(INVOKEVIRTUAL, "ho", "e", "(Ljava/lang/String;)Ljava/io/File;");
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "java/util/HashSet");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashSet", "<init>", "()V");
			mv.visitFieldInsn(PUTFIELD, "hn", "godCommands", "Ljava/util/Set;");
			mv.visitTypeInsn(NEW, "java/io/BufferedReader");
			mv.visitInsn(DUP);
			mv.visitTypeInsn(NEW, "java/io/FileReader");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/FileReader", "<init>", "(Ljava/io/File;)V");
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/BufferedReader", "<init>", "(Ljava/io/Reader;)V");
			mv.visitVarInsn(ASTORE, 3);
			mv.visitLdcInsn("");
			mv.visitVarInsn(ASTORE, 4);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitFrame(Opcodes.F_APPEND,3, new Object[] {"java/io/File", "java/io/BufferedReader", "java/lang/String"}, 0, null);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedReader", "readLine", "()Ljava/lang/String;");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 4);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNULL, l4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "hn", "godCommands", "Ljava/util/Set;");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "trim", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "toLowerCase", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "add", "(Ljava/lang/Object;)Z");
			mv.visitInsn(POP);
			mv.visitJumpInsn(GOTO, l3);
			mv.visitLabel(l4);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedReader", "close", "()V");
			mv.visitLabel(l1);
			Label l5 = new Label();
			mv.visitJumpInsn(GOTO, l5);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_FULL, 2, new Object[] {"hn", "ho"}, 1, new Object[] {"java/lang/Exception"});
			mv.visitVarInsn(ASTORE, 2);
			mv.visitFieldInsn(GETSTATIC, "hn", "a", "Ljava/util/logging/Logger;");
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V");
			mv.visitLdcInsn("Failed to load god commands list: ");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/logging/Logger", "warning", "(Ljava/lang/String;)V");
			mv.visitLabel(l5);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(5, 5);
			mv.visitEnd();			
		}
		
		public void visitLoadSuperOpCommands() {
			MethodVisitor mv = visitMethod(ACC_PRIVATE, "loadSuperOpCommands", "(Lho;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "hn", "superOps", "Ljava/util/Set;");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "size", "()I");
			mv.visitJumpInsn(IFNE, l0);
			mv.visitFieldInsn(GETSTATIC, "hn", "a", "Ljava/util/logging/Logger;");
			mv.visitLdcInsn("No super-ops specified. Therefore, no commands reserved for super-ops.");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/logging/Logger", "warning", "(Ljava/lang/String;)V");
			mv.visitInsn(RETURN);
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitLdcInsn("super-op-commands.txt");
			mv.visitMethodInsn(INVOKEVIRTUAL, "ho", "e", "(Ljava/lang/String;)Ljava/io/File;");
			mv.visitVarInsn(ASTORE, 2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "java/util/HashSet");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashSet", "<init>", "()V");
			mv.visitFieldInsn(PUTFIELD, "hn", "superOpCommands", "Ljava/util/Set;");
			mv.visitTypeInsn(NEW, "java/io/BufferedReader");
			mv.visitInsn(DUP);
			mv.visitTypeInsn(NEW, "java/io/FileReader");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/FileReader", "<init>", "(Ljava/io/File;)V");
			mv.visitMethodInsn(INVOKESPECIAL, "java/io/BufferedReader", "<init>", "(Ljava/io/Reader;)V");
			mv.visitVarInsn(ASTORE, 3);
			mv.visitLdcInsn("");
			mv.visitVarInsn(ASTORE, 4);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitFrame(Opcodes.F_APPEND,3, new Object[] {"java/io/File", "java/io/BufferedReader", "java/lang/String"}, 0, null);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedReader", "readLine", "()Ljava/lang/String;");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 4);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNULL, l4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "hn", "superOpCommands", "Ljava/util/Set;");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "trim", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "toLowerCase", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "add", "(Ljava/lang/Object;)Z");
			mv.visitInsn(POP);
			mv.visitJumpInsn(GOTO, l3);
			mv.visitLabel(l4);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedReader", "close", "()V");
			mv.visitLabel(l1);
			Label l5 = new Label();
			mv.visitJumpInsn(GOTO, l5);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_FULL, 2, new Object[] {"hn", "ho"}, 1, new Object[] {"java/lang/Exception"});
			mv.visitVarInsn(ASTORE, 2);
			mv.visitFieldInsn(GETSTATIC, "hn", "a", "Ljava/util/logging/Logger;");
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V");
			mv.visitLdcInsn("Failed to load super-op commands list: ");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/logging/Logger", "warning", "(Ljava/lang/String;)V");
			mv.visitLabel(l5);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(5, 5);
			mv.visitEnd();
		}
	}

	public class ConstructorVisitor extends MethodVisitor {

		public ConstructorVisitor(int api, MethodVisitor mv) {
			super(api, mv);
		}
		
		@Override
		/*
        this.loadGodList(par1DedicatedServer);
        this.loadSuperOpsList(par1DedicatedServer);
        this.loadGodCommands(par1DedicatedServer);
        this.loadSuperOpCommands(par1DedicatedServer);
		 */
	    public void visitInsn(int opcode) {
	        if (opcode == RETURN) {
	        	mv.visitVarInsn(ALOAD, 0);
	        	mv.visitVarInsn(ALOAD, 1);
	        	mv.visitMethodInsn(INVOKESPECIAL, "hn", "loadGodList", "(Lho;)V");
	        	mv.visitVarInsn(ALOAD, 0);
	        	mv.visitVarInsn(ALOAD, 1);
	        	mv.visitMethodInsn(INVOKESPECIAL, "hn", "loadSuperOpsList", "(Lho;)V");
	        	mv.visitVarInsn(ALOAD, 0);
	        	mv.visitVarInsn(ALOAD, 1);
	        	mv.visitMethodInsn(INVOKESPECIAL, "hn", "loadGodCommands", "(Lho;)V");
	        	mv.visitVarInsn(ALOAD, 0);
	        	mv.visitVarInsn(ALOAD, 1);
	        	mv.visitMethodInsn(INVOKESPECIAL, "hn", "loadSuperOpCommands", "(Lho;)V");
	        } 	        
	        mv.visitInsn(opcode);	       
	    }
	}
	
}
