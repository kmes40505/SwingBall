package com.leochen.swingball.units;
public abstract class Spirit extends Instance {
	protected MoveAction action = null;
	protected double speed;
	protected ActionType actionType;
	//used to check if a action shift is complete
	protected boolean actionShifting;
	protected CollisionFunc chkColliFunc;

	public void setColli(CollisionFunc input) {
		actionShifting = true;
		chkColliFunc = input;
	}

	public boolean isShifting() {
		if (chkColliFunc != null && actionShifting == true && chkColliFunc.chk(this))
			return true;

		chkColliFunc = null;
		actionShifting = false;
		return false;
	}

	public void setActionShifting(boolean input) {
		actionShifting = input;
	}

	public boolean getActionShifting() {
		return actionShifting;
	}

	public enum ActionType {
		none, inAir, inStation
	}

	public void setActionType(ActionType type) {
		actionType = type;
	}

	public ActionType getActionType() {
		return actionType;
	}


	public void setAction(MoveAction action) {
		this.action = action;
	}

	public void doAction() {
		if (action != null)
			action.process(this);
	}

	public void removeAction() {
		actionType = ActionType.none;
		action = null;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	private void init() {
		speed = 0;
		actionType = ActionType.none;
		actionShifting = false;
		chkColliFunc = null;
	}

	public Spirit(int x, int y, int xSize, int ySize) {
		super(x, y, xSize, ySize);
		init();
	}
}