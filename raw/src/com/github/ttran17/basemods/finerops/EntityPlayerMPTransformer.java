package com.github.ttran17.basemods.finerops;

import com.github.ttran17.basemods.IClassTransformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;

public class EntityPlayerMPTransformer implements IClassTransformer {

	private static final Logger LOGGER = LogManager.getLogger();

	public static final String EntityPlayerMP_classname = "mp"; // net.minecraft.entity.player.EntityPlayerMP
	
	public static final String getUserName = "b_";

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (EntityPlayerMP_classname.equals(name)) {
			LOGGER.info("Modifying " + name + " which is " + transformedName);
			ClassReader cr = new ClassReader(bytes);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
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
				LOGGER.info("Visiting canCommandSenderUseCommand ...");
				newMethod(mv);
				return null;
			}                          
			return mv;
		}                

		/**
    	 * public boolean canCommandSenderUseCommand(int par1, String par2Str) {
         *     return FinerOps.canCommandSenderUseCommand(par1, par2Str, getUserName, this)); 
         *     
         *   // Original from Mojang:
         *     {
         *  	   if ("seed".equals(par2Str) && !this.mcServer.isDedicatedServer()) {
         *             return true;
         *         }
         *         if ("tell".equals(par2Str) || "help".equals(par2Str) || "me".equals(par2Str) return true; 
         *         if (this.mcServer.getConfigurationManager().isPlayerOpped(this.username)) {
         *             return this.mcServer.func_110455_j() >= par1;
         *         }
         *     }    
         *   
         *  // The old one liner from Mojang:
         *     return "seed".equals(par2Str) && !this.mcServer.isDedicatedServer() ? true : (!"tell".equals(par2Str) && !"help".equals(par2Str) && !"me".equals(par2Str) ? (this.mcServer.getConfigurationManager().isPlayerOpped(this.username) ? this.mcServer.func_110455_j() >= par1 : false) : true);
    	 *
    	 * }
		 */
		public void newMethod(MethodVisitor mv) {
			mv.visitCode();
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, EntityPlayerMP_classname, getUserName, "()Ljava/lang/String;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "FinerOps", "canCommandSenderUseCommand", "(ILjava/lang/String;Ljava/lang/String;L"+EntityPlayerMP_classname +";)Z");
			mv.visitInsn(IRETURN);
			mv.visitMaxs(4, 3);
			mv.visitEnd();
		}
	}
}
