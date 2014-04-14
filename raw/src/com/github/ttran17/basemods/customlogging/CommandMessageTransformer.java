package com.github.ttran17.basemods.customlogging;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.RETURN;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.github.ttran17.basemods.IClassTransformer;

public class CommandMessageTransformer implements IClassTransformer {
	
	private static final Logger LOGGER = LogManager.getLogger();

	public static final String CommandMessage_classname = "az"; // net.minecraft.command.server.CommandMessage
	
	/**
	 * Find EntityPlayerMP which extends a class which implements this interface
	 */
	public static final String ICommandSender_classname = "ac"; // net.minecraft.command.ICommandSender
	
	public static final String ICommandSender_getUserName = "b_";
	
	public static final String ICommandSender_getChatComponent = "c_";
	
	public static final String IChatComponent_classname = "fj"; // net.minecraft.util.IChatComponent
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (CommandMessage_classname.equals(name)) {
			LOGGER.info("Modifying " + name + " which is " + transformedName);
			ClassReader cr = new ClassReader(bytes);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			CommandMessageVisitor visitor = new CommandMessageVisitor(Opcodes.ASM4, cw);
			cr.accept(visitor, 0);
			bytes = cw.toByteArray();
		}
		return bytes;
	}

	public class CommandMessageVisitor extends ClassVisitor {

		public final String name = "b"; // processCommand;
		public final String desc = "(L"+ICommandSender_classname +";[Ljava/lang/String;)V";

		public CommandMessageVisitor(int api, ClassVisitor cv) {
			super(api, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			if (this.name.equals(name) && this.desc.equals(desc)) {
				LOGGER.info("Visiting processCommand ...");
				mv = new ProcessCommandVisitor(Opcodes.ASM4, mv);
			}                          
			return mv;
		}                
	}

	public class ProcessCommandVisitor extends MethodVisitor {

		public ProcessCommandVisitor(int api, MethodVisitor mv) {
			super(api, mv);
		}

		@Override
		/**
		 * CustomLogging.logWhisper(IChatComponent from, IChatComponent to, IChatComponent message);
		 */
		public void visitInsn(int opcode) {
			if (opcode == RETURN) {
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKEINTERFACE, ICommandSender_classname, ICommandSender_getChatComponent, "()L" + IChatComponent_classname + ";");
				mv.visitVarInsn(ALOAD, 3);
				mv.visitMethodInsn(INVOKEVIRTUAL, NetHandlerPlayServerTransformer.EntityPlayerMP_classname, ICommandSender_getChatComponent, "()L" + IChatComponent_classname + ";");
				mv.visitVarInsn(ALOAD, 4);
				mv.visitMethodInsn(INVOKESTATIC, "CustomLogging", "logWhisper", "(L" + IChatComponent_classname + ";L" + IChatComponent_classname + ";L" + IChatComponent_classname + ";)V");
			}                 
			mv.visitInsn(opcode);               
		}
	}
}
