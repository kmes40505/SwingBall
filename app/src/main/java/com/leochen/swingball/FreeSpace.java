package com.leochen.swingball;

import com.leochen.swingball.units.*;
public class FreeSpace implements actionGenerator {
	private static double gravity;
	private class FreeSpaceAction implements MoveAction {
		public void process(Spirit object) {
			double oldAngle = object.getAngle();
			double oldSpeed = object.getSpeed();
			double xSpeed = oldSpeed * Math.sin(oldAngle);
			double ySpeed = oldSpeed * Math.cos(oldAngle) + gravity;
			object.setx(object.getx() + (int)(Math.ceil(xSpeed) + 0.5));
			object.sety(object.gety() + (int)ySpeed);
			double newSpeed = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);
			object.setSpeed(newSpeed);
			double newAngle = Math.asin(xSpeed / newSpeed);
			newAngle = ySpeed > 0? newAngle : Math.PI - newAngle;
			newAngle = (Math.PI * 2 + newAngle) % (Math.PI * 2);
			object.setAngle(newAngle);
		}
	}

	public static double getGravity() {
		return gravity;
	}

	public FreeSpace(double gravity) {
		this.gravity = gravity;
	}

	public void giveAction(Spirit obj) {
		obj.setActionType(Spirit.ActionType.inAir);
		obj.setActionShifting(true);
		obj.setAction(new FreeSpaceAction());
	}


}