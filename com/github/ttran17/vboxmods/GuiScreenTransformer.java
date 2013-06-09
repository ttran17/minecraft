package com.github.ttran17.vboxmods;

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

public class GuiScreenTransformer implements IClassTransformer {
	
	public final String className = "axr";

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (className.equals(name)) {
			FMLRelaunchLog.log(Level.INFO,"Modifying " + name + " which is " + transformedName);
			ClassReader cr = new ClassReader(bytes);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			GuiScreenVisitor gsa = new GuiScreenVisitor(Opcodes.ASM4, cw);
			cr.accept(gsa, 0);
			bytes = cw.toByteArray();
		}
		return bytes;
	}

	public class GuiScreenVisitor extends ClassVisitor {
		
		public final String name = "a"; //"drawScreen";
		public final String desc = "(IIF)V";

		public GuiScreenVisitor(int api, ClassWriter cv) {
			super(api, cv);
		}
		
		@Override
	    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			if (this.name.equals(name) && this.desc.equals(desc)) {
				FMLRelaunchLog.log(Level.FINE,"Visiting drawScreen ...");
				mv = new DrawScreenVisitor(Opcodes.ASM4, mv);
			}  			
			return mv;
		}

		@Override
		public void visitEnd() {
			FMLRelaunchLog.log(Level.FINE,"Visiting drawCursor ...");
			drawCursor();
			super.visitEnd();
		}

		/*	    
		    Draws a cursor on the screen when playing VirtualBox = true

	    	public void drawCursor() {
	        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
	        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

	        int L = 7;
	        int T = 1;
	        double H = 0.0D;

	        Tessellator tessellator = Tessellator.instance;
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        GL11.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
	        tessellator.startDrawingQuads();
	        // Draw the horizontal (clockwise starting from 4th quadrant)
	        tessellator.addVertex((double)x+L, (double)y-T, H);
	        tessellator.addVertex((double)x-L, (double)y-T, H);
	        tessellator.addVertex((double)x-L, (double)y+T, H);
	        tessellator.addVertex((double)x+L, (double)y+T, H);
	        // Draw the vertical (clockwise starting from 4th quadrant)
	        tessellator.addVertex((double)x+T, (double)y-L, H);
	        tessellator.addVertex((double)x-T, (double)y-L, H);
	        tessellator.addVertex((double)x-T, (double)y+L, H);
	        tessellator.addVertex((double)x+T, (double)y+L, H);
	        tessellator.draw();
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glDisable(GL11.GL_BLEND);	
		 */
		public void drawCursor() {
			MethodVisitor mv = super.visitMethod(ACC_PUBLIC, "drawCursor", "()V", null, null);
			mv.visitCode();
			mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/input/Mouse", "getEventX", "()I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "axr", "h", "I");
			mv.visitInsn(IMUL);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "axr", "g", "Lnet/minecraft/client/Minecraft;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "c", "I");
			mv.visitInsn(IDIV);
			mv.visitVarInsn(ISTORE, 1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "axr", "i", "I");
			mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/input/Mouse", "getEventY", "()I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "axr", "i", "I");
			mv.visitInsn(IMUL);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "axr", "g", "Lnet/minecraft/client/Minecraft;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "d", "I");
			mv.visitInsn(IDIV);
			mv.visitInsn(ISUB);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(ISUB);
			mv.visitVarInsn(ISTORE, 2);
			mv.visitIntInsn(BIPUSH, 7);
			mv.visitVarInsn(ISTORE, 3);
			mv.visitInsn(ICONST_1);
			mv.visitVarInsn(ISTORE, 4);
			mv.visitInsn(DCONST_0);
			mv.visitVarInsn(DSTORE, 5);
			mv.visitFieldInsn(GETSTATIC, "bgd", "a", "Lbgd;");
			mv.visitVarInsn(ASTORE, 7);
			mv.visitIntInsn(SIPUSH, 3042);
			mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glEnable", "(I)V");
			mv.visitIntInsn(SIPUSH, 3553);
			mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glDisable", "(I)V");
			mv.visitIntInsn(SIPUSH, 770);
			mv.visitIntInsn(SIPUSH, 771);
			mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glBlendFunc", "(II)V");
			mv.visitInsn(FCONST_0);
			mv.visitInsn(FCONST_0);
			mv.visitInsn(FCONST_1);
			mv.visitInsn(FCONST_1);
			mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glColor4f", "(FFFF)V");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitMethodInsn(INVOKEVIRTUAL, "bgd", "b", "()V");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(I2D);
			mv.visitInsn(DADD);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitInsn(I2D);
			mv.visitInsn(DSUB);
			mv.visitVarInsn(DLOAD, 5);
			mv.visitMethodInsn(INVOKEVIRTUAL, "bgd", "a", "(DDD)V");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(I2D);
			mv.visitInsn(DSUB);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitInsn(I2D);
			mv.visitInsn(DSUB);
			mv.visitVarInsn(DLOAD, 5);
			mv.visitMethodInsn(INVOKEVIRTUAL, "bgd", "a", "(DDD)V");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(I2D);
			mv.visitInsn(DSUB);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitInsn(I2D);
			mv.visitInsn(DADD);
			mv.visitVarInsn(DLOAD, 5);
			mv.visitMethodInsn(INVOKEVIRTUAL, "bgd", "a", "(DDD)V");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(I2D);
			mv.visitInsn(DADD);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitInsn(I2D);
			mv.visitInsn(DADD);
			mv.visitVarInsn(DLOAD, 5);
			mv.visitMethodInsn(INVOKEVIRTUAL, "bgd", "a", "(DDD)V");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitInsn(I2D);
			mv.visitInsn(DADD);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(I2D);
			mv.visitInsn(DSUB);
			mv.visitVarInsn(DLOAD, 5);
			mv.visitMethodInsn(INVOKEVIRTUAL, "bgd", "a", "(DDD)V");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitInsn(I2D);
			mv.visitInsn(DSUB);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(I2D);
			mv.visitInsn(DSUB);
			mv.visitVarInsn(DLOAD, 5);
			mv.visitMethodInsn(INVOKEVIRTUAL, "bgd", "a", "(DDD)V");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitInsn(I2D);
			mv.visitInsn(DSUB);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(I2D);
			mv.visitInsn(DADD);
			mv.visitVarInsn(DLOAD, 5);
			mv.visitMethodInsn(INVOKEVIRTUAL, "bgd", "a", "(DDD)V");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitInsn(I2D);
			mv.visitInsn(DADD);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(I2D);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(I2D);
			mv.visitInsn(DADD);
			mv.visitVarInsn(DLOAD, 5);
			mv.visitMethodInsn(INVOKEVIRTUAL, "bgd", "a", "(DDD)V");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitMethodInsn(INVOKEVIRTUAL, "bgd", "a", "()I");
			mv.visitInsn(POP);
			mv.visitIntInsn(SIPUSH, 3553);
			mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glEnable", "(I)V");
			mv.visitIntInsn(SIPUSH, 3042);
			mv.visitMethodInsn(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glDisable", "(I)V");
			mv.visitInsn(RETURN);
			mv.visitMaxs(7, 8);
			mv.visitEnd();
		}
	}		

	public class DrawScreenVisitor extends MethodVisitor {

		public DrawScreenVisitor(int api, MethodVisitor mv) {
			super(api, mv);
		}
		
		@Override
		/*
		 * if (this.mc.virtualbox) {
		 * 		drawCursor();
		 * }
		 */
	    public void visitInsn(int opcode) {
	        if (opcode == RETURN) {
	        	mv.visitVarInsn(ALOAD, 0);
	        	mv.visitFieldInsn(GETFIELD, "axr", "g", "Lnet/minecraft/client/Minecraft;");
	        	mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "virtualbox", "Z");
	        	Label l2_a = new Label();
	        	mv.visitJumpInsn(IFEQ, l2_a);
	        	mv.visitVarInsn(ALOAD, 0);
	        	mv.visitMethodInsn(INVOKEVIRTUAL, "axr", "drawCursor", "()V");
	        	mv.visitLabel(l2_a);
	        	mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
	        } 	        
	        mv.visitInsn(opcode);	       
	    }
	}
}