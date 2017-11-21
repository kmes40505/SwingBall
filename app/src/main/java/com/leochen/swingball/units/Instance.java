package com.leochen.swingball.units;

public abstract class Instance {
	private int x;
	private int y;
	//angle is in Radian
	private double angle;
	private int xSize;
	private int ySize;


	public int getDrawX() {
		return x;
	}

	public int getDrawY() {
		return y;
	}

	public int getxSize() {
		return xSize;
	}

	public int getySize() {
		return ySize;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}


	public int getx() {
		return x + xSize / 2;
	}

	public void setx(int x) {
		this.x = x - xSize / 2;
	}

	public int gety() {
		return y + ySize / 2;
	}

	public void sety(int y) {
		this.y = y - ySize / 2;
	}

	public Instance(int x, int y, int xSize, int ySize) {
		this.xSize = xSize;
		this.ySize = ySize;
		setx(x);
		sety(y);
		this.angle = 0.0;
	}

	public abstract void draw(float[] mMVPMatrix);
}