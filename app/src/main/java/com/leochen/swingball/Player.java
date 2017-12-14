package com.leochen.swingball;

import com.leochen.swingball.units.*;

public class Player extends Spirit {
	private double diameter;
	private float[] bodyColour;
	private float[] centreColour;
	public double getDiameter() {
		return diameter;
	}

	private void init(double diameter) {
		this.diameter = diameter;
		bodyColour = new float[]{1.0f, 0.709803922f, 0.2f, 0.5f};
		centreColour = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
	}

	public Player(int x, int y, double diameter) {
		super(x, y, (int)diameter, (int)diameter);
		init(diameter);
	}

	public void draw(float[] mMVPMatrix) {
		DrawObj.circle(this, mMVPMatrix, bodyColour);
		DrawObj.circle(getx(), gety(), (int)diameter / 10, mMVPMatrix, centreColour);
	}
}
