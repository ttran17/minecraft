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
	
	public static final String Minecraft_classname = "azd"; // net.minecraft.client.Minecraft
	
	public static final String checkGLError_name = "d"; //"checkGLError";
	public static final String checkGLError_desc = "(Ljava/lang/String;)V";
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (Minecraft_classname.equals(name)) {
			FMLRelaunchLog.info("Modifying %s which is %s", name, transformedName);
			ClassReader cr = new ClassReader(bytes);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			MinecraftVisitor ma = new MinecraftVisitor(Opcodes.ASM4, cw);
			cr.accept(ma, 0);
			bytes = cw.toByteArray();
		}
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
				FMLRelaunchLog.fine("Visiting checkGLError ...");
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
	
}
