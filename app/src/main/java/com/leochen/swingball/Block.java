package com.leochen.swingball;

import com.leochen.swingball.units.*;

public class Block extends Instance implements actionGenerator, Collision {
	private class BlockColliFunc implements CollisionFunc {
		public boolean chk(Spirit obj) {
			int leftBound = getx() - (getxSize() + obj.getxSize()) / 2;
			int rightBound = getx() + (getxSize() + obj.getxSize()) / 2;
			int upperBound = gety() - (getySize() + obj.getySize()) / 2;
			int lowerBound = gety() + (getySize() + obj.getySize()) / 2;
			if (obj.getx() >= leftBound  &&
				obj.getx() <= rightBound &&
				obj.gety() <= lowerBound &&
				obj.gety() >= upperBound)
				return true;
			return false;
		}
	}

	public Block(int x, int y, int xSize, int ySize) {
		super(x, y, xSize, ySize);
	}

	public CollisionFunc getColliFunc() {
		return new BlockColliFunc();
	}

	public void giveAction(Spirit obj) {
		obj.setActionType(Spirit.ActionType.none);
		obj.setSpeed(0);
		obj.setAngle(0);
		obj.setColli(null);
		obj.setAction(null);
	}

	public void draw(float[] mMVPMatrix) {
		DrawObj.rectangle(this, mMVPMatrix);
	}
}