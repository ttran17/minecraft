package com.github.ttran17.blockmods.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MathUtils {
	
	public enum COMPASS {
		NORTH(0),
		EAST(1),
		SOUTH(2),
		WEST(3);
		
		public final int index;
		
		private COMPASS(int index) {
			this.index = index;
		}
	}
	
	public enum FACE {
		BOTTOM(0),
		TOP(1),
		NORTH(2),
		SOUTH(3),
		WEST(4),
		EAST(5);
		
		public final int index;
		
		private FACE(int index) {
			this.index = index;
		}
	}
	
	/**
	 * Canonical coords means the way the object looks if the user
	 * stands south of the object and looks north at it.
	 * 
	 * @param canonical The orientation to rotate from (by definition: COMPASS == NORTH).
	 * @param direction The orientation to rotate to.
	 * @return
	 */
	public static double[][] rotateBlock(double[][] canonical, COMPASS direction) {
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
		 *  EAST    SOUTH    WEST
		 *  0 -1    -1  0     0 1
		 *  1  0     0 -1    -1 0
		 */
		
		// NORTH 
		int a = 1;
		int b = 0;
		int c = 0;
		int d = 1;
		
		if (direction == COMPASS.EAST) {
			a = 0;
			b = -1;
			c = 1;
			d = 0;
		} else if (direction == COMPASS.SOUTH) {
			a = -1;
			b = 0;
			c = 0;
			d = -1;
		} else if (direction == COMPASS.WEST){
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
	
	public static double[][][] getModelByCompassDirection(double[][] canonical) {
		double[][][] model = new double[4][][];
		
		model[COMPASS.NORTH.index] = MathUtils.rotateBlock(canonical, COMPASS.NORTH);
		model[COMPASS.EAST.index] = MathUtils.rotateBlock(canonical, COMPASS.EAST);
		model[COMPASS.SOUTH.index] = MathUtils.rotateBlock(canonical, COMPASS.SOUTH);
		model[COMPASS.WEST.index] = MathUtils.rotateBlock(canonical, COMPASS.WEST);
		
		return model;
	}
	
	public static double[][] getBoundsByCompassDirection(double[][][] modelByCompass) {
		double[][] bounds = new double[4][6];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				bounds[i][j] = Double.POSITIVE_INFINITY;
				bounds[i][j+3] = Double.NEGATIVE_INFINITY;
			}
		}

		for (COMPASS y : COMPASS.values()) {
			double[][] model = modelByCompass[y.index];
			MathUtils.toBounds(model, bounds[y.index]);
		}
		
		return bounds;
	}
}
