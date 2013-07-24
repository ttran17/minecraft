package com.github.ttran17.vboxmods;

import static org.objectweb.asm.Opcodes.*;

import java.util.logging.Level;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class MinecraftTransformer implements IClassTransformer {

	public final String className = "net.minecraft.client.Minecraft";
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (className.equals(name)) {
			FMLRelaunchLog.log(Level.INFO,"Modifying " + className);
			ClassReader cr = new ClassReader(bytes);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			MinecraftVisitor ma = new MinecraftVisitor(Opcodes.ASM4, cw);
			cr.accept(ma, 0);
			bytes = cw.toByteArray();
		}
		return bytes;
	}
	
	public class MinecraftVisitor extends ClassVisitor {
		public final String name = "a"; //"startGame";
		public final String desc = "()V";	
		
		public final String name2 = "d"; //"checkGLError";
		public final String desc2 = "(Ljava/lang/String;)V";
		
		public MinecraftVisitor(int api, ClassWriter cv) {
			super(api, cv);
		}
		
		@Override
	    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			if (this.name.equals(name) && this.desc.equals(desc)) {
				FMLRelaunchLog.log(Level.FINE,"Visiting startGame ...");
				mv = new StartGameVisitor(Opcodes.ASM4, mv);
			} else if (this.name2.equals(name) && this.desc2.equals(desc)) {
				FMLRelaunchLog.log(Level.FINE,"Visiting checkGLError ...");
	    		newMethod(mv);
	    		return null;
	    	} 
			return mv;
		}
		
		@Override
	    // public boolean virtualbox = false;
		public void visitEnd() {
			FMLRelaunchLog.log(Level.FINE,"Visiting virtualBox field ...");
			FieldVisitor fv = cv.visitField(ACC_PUBLIC, "virtualbox", "Z", null, null);
			fv.visitEnd();
			cv.visitEnd();
		}
		
		/* 
			int i = GL11.glGetError();
	        
	        if (i != 0 && (!virtualbox || (virtualbox && i != 1280)))
	        {
	            String s1 = GLU.gluErrorString(i);
	            this.getLogAgent().logSevere("########## GL ERROR ##########");
	            this.getLogAgent().logSevere("@ " + par1Str);
	            this.getLogAgent().logSevere(i + ": " + s1);
	        }
        */
		public void newMethod(MethodVisitor mv) {
			mv.visitCode();
			mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glGetError", "()I");
			mv.visitVarInsn(ISTORE, 2);
			mv.visitVarInsn(ILOAD, 2);
			Label l0 = new Label();
			mv.visitJumpInsn(IFEQ, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "virtualbox", "Z");
			Label l1 = new Label();
			mv.visitJumpInsn(IFEQ, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "virtualbox", "Z");
			mv.visitJumpInsn(IFEQ, l0);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitIntInsn(SIPUSH, 1280);
			mv.visitJumpInsn(IF_ICMPEQ, l0);
			mv.visitLabel(l1);
			mv.visitFrame(Opcodes.F_APPEND,1, new Object[] {Opcodes.INTEGER}, 0, null);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/util/glu/GLU", "gluErrorString", "(I)Ljava/lang/String;");
			mv.visitVarInsn(ASTORE, 3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "al", "()Lku;");
			mv.visitLdcInsn("########## GL ERROR ##########");
			mv.visitMethodInsn(INVOKEINTERFACE, "ku", "c", "(Ljava/lang/String;)V");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "al", "()Lku;");
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V");
			mv.visitLdcInsn("@ ");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEINTERFACE, "ku", "c", "(Ljava/lang/String;)V");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "al", "()Lku;");
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V");
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
			mv.visitLdcInsn(": ");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEINTERFACE, "ku", "c", "(Ljava/lang/String;)V");
			mv.visitLabel(l0);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitMaxs(3, 4);
			mv.visitEnd();
		}
	}
	
	public class StartGameVisitor extends MethodVisitor {
		public final String owner = "bkn"; //"net/minecraft/client/renderer/OpenGlHelper";
		public final String name = "a"; //"initializeTextures";
		public final String desc = "()V";

		public StartGameVisitor(int api, MethodVisitor mv) {
			super(api, mv);
		}
		
		@Override
		/* Tries to detect virtualbox via opengl vendor/renderer */
		/*        
	        if (GL11.glGetString(GL11.GL_VENDOR).trim().equalsIgnoreCase("humper") || GL11.glGetString(GL11.GL_RENDERER).trim().equalsIgnoreCase("chromium")) {  
	            this.getLogAgent().logInfo("OpenGL Vendor: " + GL11.glGetString(GL11.GL_VENDOR).trim());
            	this.getLogAgent().logInfo("OpenGL Renderer: " + GL11.glGetString(GL11.GL_RENDERER).trim());
	        	this.getLogAgent().logInfo("Judging from OpenGl vendor or renderer: setting virtualbox = true!");
	        	this.virtualbox = true;
	        } 
        }*/
	    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
			if (opcode == INVOKESTATIC && this.owner.equals(owner) && this.name.equals(name) && this.desc.equals(desc)) {
				mv.visitIntInsn(SIPUSH, 7936);
				mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glGetString", "(I)Ljava/lang/String;");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "trim", "()Ljava/lang/String;");
				mv.visitLdcInsn("humper");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equalsIgnoreCase", "(Ljava/lang/String;)Z");
				Label l16_a = new Label();
				mv.visitJumpInsn(IFNE, l16_a);
				mv.visitIntInsn(SIPUSH, 7937);
				mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glGetString", "(I)Ljava/lang/String;");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "trim", "()Ljava/lang/String;");
				mv.visitLdcInsn("chromium");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equalsIgnoreCase", "(Ljava/lang/String;)Z");
				Label l17_a = new Label();
				mv.visitJumpInsn(IFEQ, l17_a);
				mv.visitLabel(l16_a);
				mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "al", "()Lku;");
				mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
				mv.visitInsn(DUP);
				mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V");
				mv.visitLdcInsn("OpenGL Vendor: ");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
				mv.visitIntInsn(SIPUSH, 7936);
				mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glGetString", "(I)Ljava/lang/String;");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "trim", "()Ljava/lang/String;");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
				mv.visitMethodInsn(INVOKEINTERFACE, "ku", "a", "(Ljava/lang/String;)V");
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "al", "()Lku;");
				mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
				mv.visitInsn(DUP);
				mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V");
				mv.visitLdcInsn("OpenGL Renderer: ");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
				mv.visitIntInsn(SIPUSH, 7937);
				mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glGetString", "(I)Ljava/lang/String;");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "trim", "()Ljava/lang/String;");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
				mv.visitMethodInsn(INVOKEINTERFACE, "ku", "a", "(Ljava/lang/String;)V");
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "al", "()Lku;");
				mv.visitLdcInsn("Judging from OpenGl vendor and/or renderer: setting virtualbox = true!");
				mv.visitMethodInsn(INVOKEINTERFACE, "ku", "a", "(Ljava/lang/String;)V");
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ICONST_1);
				mv.visitFieldInsn(PUTFIELD, "net/minecraft/client/Minecraft", "virtualbox", "Z");
				mv.visitLabel(l17_a);
				mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			}
			mv.visitMethodInsn(opcode, owner, name, desc);
		}
	}	
}
