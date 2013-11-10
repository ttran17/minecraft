package com.github.ttran17.vboxmods.asm;

import java.util.logging.Level;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import static org.objectweb.asm.Opcodes.*;

public class GuiScreenTransformer implements IClassTransformer {
	
	public static final String className = "awe"; //net.minecraft.client.gui.GuiScreen
	public static final String name = "a"; //"drawScreen";
	public static final String desc = "(IIF)V"; // drawScreen params

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (className.equals(name)) {
			FMLRelaunchLog.log(Level.INFO,"Modifying " + name + " which is " + transformedName);
			ClassReader cr = new ClassReader(bytes);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			GuiScreenVisitor gsv = new GuiScreenVisitor(Opcodes.ASM4, cw);
			cr.accept(gsv, 0);
			bytes = cw.toByteArray();
		}
		return bytes;
	}

	public class GuiScreenVisitor extends ClassVisitor {

		public GuiScreenVisitor(int api, ClassWriter cv) {
			super(api, cv);
		}
		
		@Override
	    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			if (GuiScreenTransformer.name.equals(name) && GuiScreenTransformer.desc.equals(desc)) {
				FMLRelaunchLog.log(Level.FINE,"Visiting drawScreen ...");
				mv = new DrawScreenVisitor(Opcodes.ASM4, mv);
			}  			
			return mv;
		}
	}

	public class DrawScreenVisitor extends MethodVisitor {

		public DrawScreenVisitor(int api, MethodVisitor mv) {
			super(api, mv);
		}
		
		@Override
		/*
        if (com.github.ttran17.vboxmods.VirtualBoxOpenGLCursor.virtualbox) {
        	com.github.ttran17.vboxmods.VirtualBoxOpenGLCursor.drawCursor(this.width, this.height, this.mc.displayWidth, this.mc.displayHeight);
        }
		 */
	    public void visitInsn(int opcode) {
	        if (opcode == RETURN) {
	        	mv.visitFieldInsn(GETSTATIC, "com/github/ttran17/vboxmods/VirtualBoxOpenGLCursor", "virtualbox", "Z");
	        	Label l2 = new Label();
	        	mv.visitJumpInsn(IFEQ, l2);
	        	mv.visitVarInsn(ALOAD, 0);
	        	mv.visitFieldInsn(GETFIELD, "awe", "g", "I");
	        	mv.visitVarInsn(ALOAD, 0);
	        	mv.visitFieldInsn(GETFIELD, "awe", "h", "I");
	        	mv.visitVarInsn(ALOAD, 0);
	        	mv.visitFieldInsn(GETFIELD, "awe", "f", "Latv;");
	        	mv.visitFieldInsn(GETFIELD, "atv", "d", "I");
	        	mv.visitVarInsn(ALOAD, 0);
	        	mv.visitFieldInsn(GETFIELD, "awe", "f", "Latv;");
	        	mv.visitFieldInsn(GETFIELD, "atv", "e", "I");
	        	mv.visitMethodInsn(INVOKESTATIC, "com/github/ttran17/vboxmods/VirtualBoxOpenGLCursor", "drawCursor", "(IIII)V");
	        	mv.visitLabel(l2);
	        	mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
	        } 	        
	        mv.visitInsn(opcode);	       
	    }
	}
}