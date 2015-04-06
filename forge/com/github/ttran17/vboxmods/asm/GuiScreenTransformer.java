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
	
	public static final String GuiScreen_classname = "bdw"; // net.minecraft.client.gui.GuiScreen
	
	public static final String Minecraft_ref = "k"; // Reference to Minecraft in GuiScreen
	public static final String Minecraft_displayWidth = "d";
	public static final String Minecraft_displayHeight = "e";

	public static final String drawScreen_name = "a"; 
	public static final String drawScreen_desc = "(IIF)V";

	public static final String GuiScreen_width = "l";
	public static final String GuiScreen_height = "m";

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (GuiScreen_classname.equals(name)) {
			FMLRelaunchLog.info("Modifying %s which is %s", name, transformedName);
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
			if (GuiScreenTransformer.drawScreen_name.equals(name) && GuiScreenTransformer.drawScreen_desc.equals(desc)) {
				FMLRelaunchLog.fine("Visiting drawScreen ...");
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
        	com.github.ttran17.vboxmods.VirtualBoxOpenGLCursor.drawCursor(this.width, this.height, this.mc.displayWidth, this.mc.displayHeight);
		 */
	    public void visitInsn(int opcode) {
	        if (opcode == RETURN) {
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, GuiScreen_classname, GuiScreen_width, "I");
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, GuiScreen_classname, GuiScreen_height, "I");
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, GuiScreen_classname, Minecraft_ref, "L"+MinecraftTransformer.Minecraft_classname+";");
				mv.visitFieldInsn(GETFIELD, MinecraftTransformer.Minecraft_classname, Minecraft_displayWidth, "I");
				mv.visitVarInsn(ALOAD, 0);
				mv.visitFieldInsn(GETFIELD, GuiScreen_classname, Minecraft_ref, "L"+MinecraftTransformer.Minecraft_classname+";");
				mv.visitFieldInsn(GETFIELD, MinecraftTransformer.Minecraft_classname, Minecraft_displayHeight, "I");
	        	mv.visitMethodInsn(INVOKESTATIC, "com/github/ttran17/vboxmods/VirtualBoxOpenGLCursor", "drawCursor", "(IIII)V");
	        } 	        
	        mv.visitInsn(opcode);	       
	    }
	}
}