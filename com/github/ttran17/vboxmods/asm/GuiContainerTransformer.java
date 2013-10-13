package com.github.ttran17.vboxmods.asm;

import java.util.logging.Level;

import net.minecraft.launchwrapper.IClassTransformer;

import org.lwjgl.opengl.GL11;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import static org.objectweb.asm.Opcodes.*;

public class GuiContainerTransformer implements IClassTransformer {
	
	public static final String className = "awy"; //net.minecraft.client.gui.inventory.GuiContainer
	public static final String owner = GuiScreenTransformer.className;
	public static final String name = GuiScreenTransformer.name;
	public static final String desc = GuiScreenTransformer.desc;

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (className.equals(name)) {
			FMLRelaunchLog.log(Level.INFO,"Modifying " + name + " which is " + transformedName);
			ClassReader cr = new ClassReader(bytes);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			GuiContainerVisitor gcv = new GuiContainerVisitor(Opcodes.ASM4, cw);
			cr.accept(gcv, 0);
			bytes = cw.toByteArray();
		}
		return bytes;
	}

	public class GuiContainerVisitor extends ClassVisitor {

		public GuiContainerVisitor(int api, ClassWriter cv) {
			super(api, cv);
		}
		
		@Override
	    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			if (GuiContainerTransformer.name.equals(name) && GuiContainerTransformer.desc.equals(desc)) {
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
		
	    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
	    	if (opcode == INVOKESPECIAL && GuiContainerTransformer.owner.equals(owner) && GuiContainerTransformer.name.equals(name) && GuiContainerTransformer.desc.equals(desc)) {
	    		/* Must pop params off the stack! */
	    		// mv.visitVarInsn(ALOAD, 0);
	    		// mv.visitVarInsn(ILOAD, 1);
	    		// mv.visitVarInsn(ILOAD, 2);
	    		// mv.visitVarInsn(FLOAD, 3);
	    		mv.visitVarInsn(FSTORE, 3);
	    		mv.visitVarInsn(ISTORE, 2);
	    		mv.visitVarInsn(ISTORE, 1);
	    		mv.visitVarInsn(ASTORE, 0);
	    		return;
			}
			mv.visitMethodInsn(opcode, owner, name, desc);
		}
		
		@Override
		/*
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.zLevel = 300.0F;
        super.drawScreen(par1, par2, par3);
        this.zLevel = 0.0F;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
		 */
	    public void visitInsn(int opcode) {
	        if (opcode == RETURN) {
	        	mv.visitIntInsn(SIPUSH, 2929);
	        	mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glDisable", "(I)V");
	        	mv.visitIntInsn(SIPUSH, 2896);
	        	mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glDisable", "(I)V");
	        	mv.visitVarInsn(ALOAD, 0);
	        	mv.visitLdcInsn(new Float("300.0"));
	        	mv.visitFieldInsn(PUTFIELD, "awy", "n", "F");
	        	mv.visitVarInsn(ALOAD, 0);
	        	mv.visitVarInsn(ILOAD, 1);
	        	mv.visitVarInsn(ILOAD, 2);
	        	mv.visitVarInsn(FLOAD, 3);
	        	mv.visitMethodInsn(INVOKESPECIAL, "awe", "a", "(IIF)V");
	        	mv.visitVarInsn(ALOAD, 0);
	        	mv.visitInsn(FCONST_0);
	        	mv.visitFieldInsn(PUTFIELD, "awy", "n", "F");
	        	mv.visitIntInsn(SIPUSH, 2896);
	        	mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glEnable", "(I)V");
	        	mv.visitIntInsn(SIPUSH, 2929);
	        	mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glEnable", "(I)V");
	        } 	        
	        mv.visitInsn(opcode);	       
	    }
	}
}
