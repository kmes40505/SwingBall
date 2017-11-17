package com.leochen.swingball;

import com.leochen.swingball.units.*;

public class Player extends Spirit {
	private double diameter;
	public double getDiameter() {
		return diameter;
	}

	private void init(double diameter) {
		this.diameter = diameter;
	}

	public Player(int x, int y, double diameter) {
		super(x, y, (int)diameter, (int)diameter);
		init(diameter);
	}

	public void draw(float[] mMVPMatrix) {
		DrawObj.circle(this, mMVPMatrix);
	}
}
