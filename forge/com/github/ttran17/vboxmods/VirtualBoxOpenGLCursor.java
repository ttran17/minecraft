package com.github.ttran17.vboxmods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class VirtualBoxOpenGLCursor {
	
	private static final Logger LOGGER = LogManager.getLogger();

	public static boolean virtualbox = true;
	
    /**
     * Checks for an OpenGL error. If there is one, prints the error ID and error string.
     * <p>
     * Gets rid of annoying OpenGL Error 1280 message in virtualbox mode.
     */
    public static void checkGLError(String par1Str)
    {
		int i = GL11.glGetError();
        
        if (i != 0 && i != 1280)
        {
            String s1 = GLU.gluErrorString(i);
            LOGGER.warn("########## GL ERROR ##########");
            LOGGER.warn("@ " + par1Str);
            LOGGER.warn(i + ": " + s1);
        }
    }
	
	public static void drawCursor(int width, int height, int displayWidth, int displayHeight) {
        int x = Mouse.getEventX() * width / displayWidth;
        int y = height - Mouse.getEventY() * height / displayHeight - 1;

        int L = 7;
        int T = 1;
        double H = 0.0D;

        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
        tessellator.startDrawingQuads();
        // Draw the horizontal (counter-clockwise starting from 4th quadrant -- y is flipped!)
        tessellator.addVertex((double)x+L, (double)y-T, H);
        tessellator.addVertex((double)x-L, (double)y-T, H);
        tessellator.addVertex((double)x-L, (double)y+T, H);
        tessellator.addVertex((double)x+L, (double)y+T, H);
        // Draw the vertical (counter-clockwise starting from 4th quadrant -- y is flipped!)
        tessellator.addVertex((double)x+T, (double)y-L, H);
        tessellator.addVertex((double)x-T, (double)y-L, H);
        tessellator.addVertex((double)x-T, (double)y+L, H);
        tessellator.addVertex((double)x+T, (double)y+L, H);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);	
	}
	
}
