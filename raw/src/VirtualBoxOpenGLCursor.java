import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/** 
 * Goes in the default package so that it can reference minecraft obfuscated classes (which are in default package).
 * 
 * @author ttran
 */
public class VirtualBoxOpenGLCursor {
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Checks for an OpenGL error. If there is one, prints the error ID and error string.
	 * <p>
	 * Gets rid of annoying OpenGL Error 1280 message in virtualbox mode.
	 */
	public static void checkGLError(String par1Str)
	{
		int i = GL11.glGetError();

		if (i != 0 &&  i != 1280 && i != 1282)
		{
			String s1 = GLU.gluErrorString(i);
			LOGGER.error("########## GL ERROR ##########");
			LOGGER.error("@ " + par1Str);
			LOGGER.error(i + ": " + s1);
		}
	}

	public static void drawCursor(int width, int height, int displayWidth, int displayHeight) {
		int x = Mouse.getEventX() * width / displayWidth;
		int y = height - Mouse.getEventY() * height / displayHeight - 1;

		int L = 7;
		int T = 1;
		double H = 0.0D;

        civ tessellator = ckx.a().c(); // 1.8.x (?) tessellator instance
		cjm.l(); // GL11.glEnable(GL11.GL_BLEND);
		cjm.x(); // GL11.glDisable(GL11.GL_TEXTURE_2D);
		cjm.b(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		cjm.c(0.0f, 0.0f, 1.0f, 1.0f);
		tessellator.b(); // startDrawingQuads
		// Draw the horizontal (counter-clockwise starting from 4th quadrant -- y is flipped!)
		tessellator.b((double)x+L, (double)y-T, H); // addVertex
		tessellator.b((double)x-L, (double)y-T, H);
		tessellator.b((double)x-L, (double)y+T, H);
		tessellator.b((double)x+L, (double)y+T, H);
		// Draw the vertical (counter-clockwise starting from 4th quadrant -- y is flipped!)
		tessellator.b((double)x+T, (double)y-L, H);
		tessellator.b((double)x-T, (double)y-L, H);
		tessellator.b((double)x-T, (double)y+L, H);
		tessellator.b((double)x+T, (double)y+L, H);
		ckx.a().b(); // draw
		cjm.w(); // GL11.glEnable(GL11.GL_TEXTURE_2D);
		cjm.k(); // GL11.glDisable(GL11.GL_BLEND);        
	}
}
