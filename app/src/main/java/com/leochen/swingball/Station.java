package com.leochen.swingball;

import com.leochen.swingball.units.*;

public class Station extends Instance implements actionGenerator, Collision {
	double diameter;
	private double stationSpeed;
	public double getStationSpeed() {
		return stationSpeed;
	}

	public void setStationSpeed(double stationSpeed) {
		this.stationSpeed = stationSpeed;
	}

	public Station(int x, int y, double diameter) {
		super(x, y, (int)diameter, (int)diameter);
		this.diameter = diameter;
		stationSpeed = EnvVar.stationSpeed();
	}

	public void giveAction(Spirit obj) {
		obj.setActionType(Spirit.ActionType.inStation);
		obj.setSpeed(stationSpeed);
		obj.setColli(getColliFunc());
		double offX = obj.getx() - getx();
		double offY = obj.gety() - gety();
		double offDiameter = Math.sqrt(offX * offX + offY * offY);
		double absNewAngle = Math.asin(offX / offDiameter);
		//this is from centre circle to point, not spirit moving angle
		double newAngle = offY > 0? absNewAngle : Math.PI - absNewAngle;
		final int circDir = (obj.getAngle() - newAngle + Math.PI * 2) % (Math.PI * 2) < Math.PI? 1 : -1;
		newAngle = newAngle + Math.PI / 2 * circDir;
		obj.setAngle(newAngle);

		class StationAction implements MoveAction {
			public void process(Spirit object) {
				double objSpeed = object.getSpeed();
				double offAngle = objSpeed / diameter * 2 * circDir;
				double angle = (Math.PI * 2 + object.getAngle() + offAngle) % (Math.PI * 2);
				object.setAngle(angle);
				object.setx(getx() + (int)(Math.sin(angle - Math.PI / 2 * circDir) * diameter / 2));
				object.sety(gety() + (int)(Math.cos(angle - Math.PI / 2 * circDir) * diameter / 2));
			}
		}

		obj.setAction(new StationAction());
	}

	public CollisionFunc getColliFunc() {
		class StationColliFunc implements CollisionFunc {
			public boolean chk(Spirit obj) {
				double offX = obj.getx() - getx();
				double offY = obj.gety() - gety();
				double distance = Math.sqrt(offX * offX + offY * offY);
				double radius = diameter / 2;
				//in obj in circle
				if (distance <= radius)
					return true;

				//obj too far from circle
				if (obj.getSpeed() + radius < distance)
					return false;

				double offAngle = Math.asin(offX / distance);
				offAngle = offY > 0? offAngle : Math.PI - offAngle;
				double diffAngle = Math.abs(obj.getAngle() + Math.PI / 2 - offAngle);
				//angle difference > max angle where distance, radius, needSpeed is a right triangle
				if (Math.asin(radius / distance) < diffAngle)
					return false;

				//distance > rad && dis > required speed(needSpeed)
				double cosVal = Math.cos(diffAngle);
				double bval = 2 * distance * cosVal;
				double needSpeed = bval + Math.sqrt(bval * bval - 4 * (distance * distance - radius * radius)) / 2;
				if (obj.getSpeed() < needSpeed)
					return false;
				return true;
			}
		}

		return new StationColliFunc();
	}

	public void draw(float[] mMVPMatrix) {
		DrawObj.circle(this, mMVPMatrix);
	}
}