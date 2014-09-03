

public class Test {

	private boolean R = false;
	
	public void check(String s) {
		if (!this.R) {
			return;
		};
		
		VirtualBoxOpenGLCursor.checkGLError(s);
	}
}
