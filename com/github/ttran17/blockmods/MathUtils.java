package com.github.ttran17.blockmods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MathUtils {
	
	public enum YAW {
		South(0),
		West(1),
		North(2),
		East(3);
		
		public final int index;
		
		private YAW(int index) {
			this.index = index;
		}
	}
	
	/**
	 * Canonical coords means the way the object looks if the user
	 * stands south of the object and looks north at it.
	 * 
	 * @param canonical The orientation to rotate from (by definition: south).
	 * @param face The orientation to rotate to.
	 * @return
	 */
	public static double[][] rotateBlock(double[][] canonical, YAW face) {
		double[][] rotated = new double[canonical.length][6];
		
		// translate to origin
		double tx = -0.5;
		double tz = -0.5;
		for (int i = 0; i < canonical.length; i++) {
			canonical[i][0] = canonical[i][0] + tx;
			canonical[i][2] = canonical[i][2] + tz;			
			canonical[i][3] = canonical[i][3] + tx;
			canonical[i][5] = canonical[i][5] + tz;
		}
			
		/*
		 *  a b
		 *  c d
		 * 
		 *  WEST    NORTH    EAST
		 *  0 -1    -1  0     0 1
		 *  1  0     0 -1    -1 0
		 */
		
		// South 
		int a = 1;
		int b = 0;
		int c = 0;
		int d = 1;
		
		if (face == YAW.West) {
			a = 0;
			b = -1;
			c = 1;
			d = 0;
		} else if (face == YAW.North) {
			a = -1;
			b = 0;
			c = 0;
			d = -1;
		} else if (face == YAW.East){
			a = 0;
			b = 1;
			c = -1;
			d = 0;
		}
				
		for (int i = 0; i < canonical.length; i++) {
			// rotate about origin
			double x0 = a*canonical[i][0] + b*canonical[i][2];
			rotated[i][1] =   canonical[i][1];
			double z0 = c*canonical[i][0] + d*canonical[i][2];
			
			double x1 = a*canonical[i][3] + b*canonical[i][5];
			rotated[i][4] =   canonical[i][4];
			double z1 = c*canonical[i][3] + d*canonical[i][5];
			
			// Minecraft has very specific form for render bounds
			if (x0 > x1) {
				double temp = x1;
				x1 = x0;
				x0 = temp;
			}
			if (z0 > z1) {
				double temp = z1;
				z1 = z0;
				z0 = temp;
			}			
			rotated[i][0] = x0;
			rotated[i][2] = z0;			
			rotated[i][3] = x1;
			rotated[i][5] = z1;
		}
		
		// translate back
		tx = 0.5;
		tz = 0.5;
		for (int i = 0; i < canonical.length; i++) {
			canonical[i][0] = canonical[i][0] + tx;
			canonical[i][2] = canonical[i][2] + tz;			
			canonical[i][3] = canonical[i][3] + tx;
			canonical[i][5] = canonical[i][5] + tz;
			
			rotated[i][0] = rotated[i][0] + tx;
			rotated[i][2] = rotated[i][2] + tz;			
			rotated[i][3] = rotated[i][3] + tx;
			rotated[i][5] = rotated[i][5] + tz;
		}

		return rotated;
	}
	
	public static void toBounds(double[][] model, double[] bound) {
		int N = model.length;
		
		for (int j = 0; j < 3; j++) {
			for (int n = 0; n < N; n++) {
				if (model[n][j] < bound[j]) {
					bound[j] = model[n][j];
				}
				if (model[n][j+3] > bound[j+3]) {
					bound[j+3] = model[n][j+3];
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static double[][][] getModelByYaw(double[][] canonical) {
		double[][][] yaw = new double[4][][];
		
		yaw[YAW.South.index] = MathUtils.rotateBlock(canonical, YAW.South);
		yaw[YAW.West.index] = MathUtils.rotateBlock(canonical, YAW.West);
		yaw[YAW.North.index] = MathUtils.rotateBlock(canonical, YAW.North);
		yaw[YAW.East.index] = MathUtils.rotateBlock(canonical, YAW.East);
		
		return yaw;
	}
	
	@SideOnly(Side.CLIENT)
	public static double[][] getBoundsBasedOnYaw(double[][][] yaw) {
		double[][] bounds = new double[4][6];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				bounds[i][j] = Double.POSITIVE_INFINITY;
				bounds[i][j+3] = Double.NEGATIVE_INFINITY;
			}
		}

		for (YAW y : YAW.values()) {
			double[][] model = yaw[y.index];
			MathUtils.toBounds(model, bounds[y.index]);
		}
		
		return bounds;
	}
}
