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

public class EntityPlayerMPTransformer implements IClassTransformer {

	public final String className = "iq";
	
	@Override
	public byte[] transform(String name, byte[] bytes) {
		if (className.equals(name)) {
			FMLRelaunchLog.log(Level.INFO,"Modifying " + name + " which is EntityPlayerMP");
			ClassReader cr = new ClassReader(bytes);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			EntityPlayerMPVisitor visitor = new EntityPlayerMPVisitor(Opcodes.ASM4, cw);
			cr.accept(visitor, 0);
			bytes = cw.toByteArray();
		}
		return bytes;
	}
	
	public class EntityPlayerMPVisitor extends ClassVisitor {

		public final String name = "a"; // canCommandSenderUseCommand;
		public final String desc = "(ILjava/lang/String;)Z"; // int par1, String par2Str
		
		public EntityPlayerMPVisitor(int api, ClassVisitor cv) {
			super(api, cv);
		}
		
		@Override
	    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			if (this.name.equals(name) && this.desc.equals(desc)) {
				FMLRelaunchLog.log(Level.FINE,"Visiting UseCommand visitor ...");
				mv = new UseCommandVisitor(Opcodes.ASM4, mv);
			}  			
			return mv;
		}		
		
		@Override
		public void visitEnd() {
			FMLRelaunchLog.log(Level.FINE,"Visiting load god/ops list commands ...");

			super.visitEnd();
		}
		
		public void toWarning() {
			MethodVisitor mv = super.visitMethod(ACC_PRIVATE, "toWarning", "(Ljava/lang/String;Ljava/util/Set;)Ljava/lang/String;", null, null);
			mv.visitCode();
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V");
			mv.visitVarInsn(ASTORE, 3);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitLdcInsn("\u00a7c");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitInsn(POP);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitLdcInsn("Sorry. On this server the command \"");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitInsn(POP);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitInsn(POP);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitLdcInsn("\" is restricted to ");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitInsn(POP);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "iterator", "()Ljava/util/Iterator;");
			mv.visitVarInsn(ASTORE, 4);
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_APPEND,2, new Object[] {"java/lang/StringBuilder", "java/util/Iterator"}, 0, null);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z");
			Label l1 = new Label();
			mv.visitJumpInsn(IFEQ, l1);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;");
			mv.visitTypeInsn(CHECKCAST, "java/lang/String");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitInsn(POP);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z");
			Label l2 = new Label();
			mv.visitJumpInsn(IFEQ, l2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitLdcInsn(", ");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitInsn(POP);
			mv.visitJumpInsn(GOTO, l0);
			mv.visitLabel(l2);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitLdcInsn(".");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitInsn(POP);
			mv.visitJumpInsn(GOTO, l0);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
			mv.visitInsn(ARETURN);
			mv.visitMaxs(2, 5);
			mv.visitEnd();
		}
	}
	
	public class UseCommandVisitor extends MethodVisitor {

		public UseCommandVisitor(int api, MethodVisitor mv) {
			super(api, mv);
		}
		
		@Override
		/*
    	if (this.mcServer.getConfigurationManager().isGodCommand(par2Str) && 
    			!this.mcServer.getConfigurationManager().isGod(this.username)) {
    		String mesg = toWarning(par2Str, this.mcServer.getConfigurationManager().gods);
    		this.sendChatToPlayer(mesg);
    		return false;
    	}
    	if (this.mcServer.getConfigurationManager().isSuperOpCommand(par2Str) && 
    			!this.mcServer.getConfigurationManager().isSuperOp(this.username)) {
    		String mesg = toWarning(par2Str, this.mcServer.getConfigurationManager().superOps);
    		this.sendChatToPlayer(mesg);
    		return false;
    	} 
		 */
	    public void visitCode() {
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "iq", "b", "Lnet/minecraft/server/MinecraftServer;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/server/MinecraftServer", "ad", "()Lgm;");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "gm", "isGodCommand", "(Ljava/lang/String;)Z");
			Label l0_a = new Label();
			mv.visitJumpInsn(IFEQ, l0_a);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "iq", "b", "Lnet/minecraft/server/MinecraftServer;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/server/MinecraftServer", "ad", "()Lgm;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "iq", "bR", "Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "gm", "isGod", "(Ljava/lang/String;)Z");
			mv.visitJumpInsn(IFNE, l0_a);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "iq", "b", "Lnet/minecraft/server/MinecraftServer;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/server/MinecraftServer", "ad", "()Lgm;");
			mv.visitFieldInsn(GETFIELD, "gm", "gods", "Ljava/util/Set;");
			mv.visitMethodInsn(INVOKESPECIAL, "iq", "toWarning", "(Ljava/lang/String;Ljava/util/Set;)Ljava/lang/String;");
			mv.visitVarInsn(ASTORE, 3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "iq", "a", "(Ljava/lang/String;)V");
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l0_a);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "iq", "b", "Lnet/minecraft/server/MinecraftServer;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/server/MinecraftServer", "ad", "()Lgm;");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "gm", "isSuperOpCommand", "(Ljava/lang/String;)Z");
			Label l1_a = new Label();
			mv.visitJumpInsn(IFEQ, l1_a);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "iq", "b", "Lnet/minecraft/server/MinecraftServer;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/server/MinecraftServer", "ad", "()Lgm;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "iq", "bR", "Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "gm", "isSuperOp", "(Ljava/lang/String;)Z");
			mv.visitJumpInsn(IFNE, l1_a);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "iq", "b", "Lnet/minecraft/server/MinecraftServer;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/server/MinecraftServer", "ad", "()Lgm;");
			mv.visitFieldInsn(GETFIELD, "gm", "superOps", "Ljava/util/Set;");
			mv.visitMethodInsn(INVOKESPECIAL, "iq", "toWarning", "(Ljava/lang/String;Ljava/util/Set;)Ljava/lang/String;");
			mv.visitVarInsn(ASTORE, 3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "iq", "a", "(Ljava/lang/String;)V");
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1_a);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);	       
	    }
	}
}
