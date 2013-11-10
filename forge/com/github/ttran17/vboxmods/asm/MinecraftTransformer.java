package com.github.ttran17.vboxmods.asm;

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

	public final String className = "atv"; // net.minecraft.client.Minecraft
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (className.equals(name)) {
			FMLRelaunchLog.log(Level.INFO,"Modifying " + name + " which is " + transformedName);
			ClassReader cr = new ClassReader(bytes);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			MinecraftVisitor ma = new MinecraftVisitor(Opcodes.ASM4, cw);
			cr.accept(ma, 0);
			bytes = cw.toByteArray();
		}
		return bytes;
	}
	
	public class MinecraftVisitor extends ClassVisitor {
		public final String name = "O"; //"startGame";
		public final String desc = "()V";	
		
		public final String name2 = "c"; //"checkGLError";
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
		
		/* Gets rid of annoying OpenGL Error 1280 message  */
		/* 
			com.github.ttran17.vboxmods.VirtualBoxOpenGLCursor.checkGLError(String par1)
        */
		public void newMethod(MethodVisitor mv) {
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "com/github/ttran17/vboxmods/VirtualBoxOpenGLCursor", "checkGLError", "(Ljava/lang/String;)V");
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 2);
			mv.visitEnd();
		}
	}
	
	public class StartGameVisitor extends MethodVisitor {
		public final String owner = "bma"; //"net/minecraft/client/renderer/OpenGlHelper";
		public final String name = "a"; //"initializeTextures";
		public final String desc = "()V";

		public StartGameVisitor(int api, MethodVisitor mv) {
			super(api, mv);
		}
		
		@Override
		/* Tries to detect virtualbox via opengl vendor/renderer */
		/*        
	        com.github.ttran17.vboxmods.VirtualBoxOpenGLCursor.checkVirtualBox();
        }*/
	    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
			if (opcode == INVOKESTATIC && this.owner.equals(owner) && this.name.equals(name) && this.desc.equals(desc)) {
				mv.visitMethodInsn(INVOKESTATIC, "com/github/ttran17/vboxmods/VirtualBoxOpenGLCursor", "checkVirtualBox", "()V");
			}
			mv.visitMethodInsn(opcode, owner, name, desc);
		}
	}
}
