package com.github.ttran17.basemods.customlogging;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.github.ttran17.basemods.IClassTransformer;
import com.github.ttran17.basemods.finerops.EntityPlayerMPTransformer;

public class NetHandlerPlayServerTransformer implements IClassTransformer {

	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String NetHandlerPlayServer_classname = "ng"; // net.minecraft.network.NetHandlerPlayServer
	
	/**
	 * Need to look for the method that has "chat.cannotSend"
	 */
	public static final String C01PacketChatMessage_classname = "ir"; // net.minecraft.network.play.client.C01PacketChatMessage
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (NetHandlerPlayServer_classname.equals(name)) {
			LOGGER.info("Modifying " + name + " which is " + transformedName);
			ClassReader cr = new ClassReader(bytes);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			NetHandlerPlayServerVisitor visitor = new NetHandlerPlayServerVisitor(Opcodes.ASM4, cw);
			cr.accept(visitor, 0);
			bytes = cw.toByteArray();
		}
		return bytes;
	}

	public class NetHandlerPlayServerVisitor extends ClassVisitor {

		public final String name = "a"; // func_147354_a -- mcp903;
		public final String desc = "(L" + C01PacketChatMessage_classname + ";)V";

		public NetHandlerPlayServerVisitor(int api, ClassVisitor cv) {
			super(api, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			if (this.name.equals(name) && this.desc.equals(desc)) {
				LOGGER.info("Visiting HandleC01PacketChatMessage ...");
				mv = new HandleC01PacketChatMessage(Opcodes.ASM4, mv);
			}                          
			return mv;
		}                
	}

	public class HandleC01PacketChatMessage extends MethodVisitor {
		/**
		 * To find the obfuscated classes checkClassInJar with class = NetHandlerPlayServer_classname obfuscated name
		 */
		
		public final String owner = "oh"; //net.minecraft.server.management.ServerConfigurationManager
		public final String name = "a"; // func_148544_a -- mcp903
		public final String desc = "(L"+ CommandMessageTransformer.IChatComponent_classname + ";Z)V";
		
		/**
		 * Look at the top of NetHandlerPlayServer_classname for playerEntity (should be field corresponding to entityPlayerMP)
		 */
		
		public final String NetHandlerPlayServer_playerEntity = "b";

		public HandleC01PacketChatMessage(int api, MethodVisitor mv) {
			super(api, mv);
		}

		@Override
		/**
		 * CustomLogging.logChat(IChatComponent from, String message);
		 */
	    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
	        mv.visitMethodInsn(opcode, owner, name, desc);
	        if (opcode == INVOKEVIRTUAL && this.owner.equals(owner) && this.name.equals(name) && this.desc.equals(desc)) {
	        	mv.visitVarInsn(ALOAD, 0);
	        	mv.visitFieldInsn(GETFIELD, NetHandlerPlayServer_classname, NetHandlerPlayServer_playerEntity, "L"+ EntityPlayerMPTransformer.EntityPlayerMP_classname + ";");
	        	mv.visitMethodInsn(INVOKEVIRTUAL, EntityPlayerMPTransformer.EntityPlayerMP_classname, CommandMessageTransformer.ICommandSender_getChatComponent, "()L" + CommandMessageTransformer.IChatComponent_classname + ";");
	        	mv.visitVarInsn(ALOAD, 2);
	        	mv.visitMethodInsn(INVOKESTATIC, "CustomLogging", "logChat", "(L" + CommandMessageTransformer.IChatComponent_classname + ";Ljava/lang/String;)V");
	        }
	    }
	}
}
