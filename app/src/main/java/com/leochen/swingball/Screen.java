package com.leochen.swingball;

import com.leochen.swingball.units.*;

public class Screen {
	private static Instance focusObj;
	private static int xOffset;
	private static int yOffset;
	private static int xDislocation;
	private static int yDislocation;
	private static int xRecoverSpeed;
	private static int yRecoverSpeed;
	private static int xTotalMoved;
	private static int yTotalMoved;
	private static double focusSpeed;

	public static void focusOn(Instance input) {
		focusOn(input, 0, 0);
	}

	public static void focusOn(int xOffset, int yOffset) {
		focusOn(null, xOffset, yOffset);
	}

	public static void focusOn(Instance input, int _xOffset, int _yOffset) {
		if (input != null) {
			xDislocation = input.getx() - EnvVar.getMainWidth() / 2 - _xOffset;
			yDislocation = input.gety() - EnvVar.getMainHeight() / 2 - _yOffset;
		} else {
			xDislocation = -_xOffset;
			yDislocation = -_yOffset;
		}

		focusObj = input;
		xOffset = _xOffset;
		yOffset = _yOffset;

		updateRecoverSpeed();
	}

	public static void updateRecoverSpeed() {
		if (xDislocation == 0 && yDislocation == 0) {
			xRecoverSpeed = 0;
			yRecoverSpeed = 0;
			return;
		}

		double speedRatio = focusSpeed / Math.sqrt((double)(xDislocation * xDislocation + yDislocation * yDislocation));
		xRecoverSpeed = (int)(xDislocation * speedRatio);
		if (xRecoverSpeed == 0)
			xRecoverSpeed = 1;
		yRecoverSpeed = (int)(yDislocation * speedRatio);
		if (yRecoverSpeed == 0)
			yRecoverSpeed = 1;
	}

	public Screen(){}

	public static void init() {
		focusObj = null;
		xOffset = 0;
		yOffset = 0;
		xDislocation = 0;
		yDislocation = 0;
		xRecoverSpeed = 0;
		yRecoverSpeed = 0;
		focusSpeed = EnvVar.screenFocusSpeed();
	}

	//should be called everytime before screen is used in each frame
	public static void updateScreen() {
		xTotalMoved = focusObj != null? focusObj.getx() - EnvVar.getMainWidth() / 2 - xOffset  - xDislocation : 0;
		yTotalMoved = focusObj != null? focusObj.gety() - EnvVar.getMainHeight() / 2 - yOffset - yDislocation : 0;

		if (xDislocation != 0) {
			xDislocation -= xRecoverSpeed;
			if (xDislocation * xRecoverSpeed < 0) {
				xTotalMoved += xRecoverSpeed + xDislocation;
				xDislocation = 0;
			} else {
				xTotalMoved += xRecoverSpeed;
			}
		}

		if (yDislocation != 0) {
			yDislocation -= yRecoverSpeed;
			if (yDislocation * yRecoverSpeed < 0) {
				yTotalMoved += yRecoverSpeed + yDislocation;
				yDislocation = 0;
			} else {
				yTotalMoved += yRecoverSpeed;
			}
		}

		Board.setTotalXOff(Board.getTotalXOff() + xTotalMoved);
		Board.setTotalYOff(Board.getTotalYOff() - yTotalMoved);
	}

	public static void screenLocation(Instance obj) {
		obj.setx(obj.getx() - xTotalMoved);
		obj.sety(obj.gety() - yTotalMoved);
	}
}