package com.leochen.swingball;

public class EnvVar {
	private static FreeSpace freeSpaceObj;
	private static int mainWidth;
	private static int mainHeight;

	private static double stationSpeedVal = 6.8; //speed: 6.6-8.4, size 50-100 height diff: 120, dis diff:50 (blocksize 200)
	private static double gravityVal = 0.06;
	private static double screenFocusSpeedVal = 6;
	private static double speedDiffVal = 1.8;
	private static double smallestStationSpeedVal = 4.4;
	private static double screenScale = 1.0;
	private static int stationSizeDiffVal = 40;
	private static int heightDiffVal = 120;
	private static int disDiffVal = 50;
	private static int smallestStationSizeVal = 60;
	private static int blockSizeVal = 200;
	private static int avgStationHeightVal = 180;
	private static int playerSizeVal = 20;
	private static GameState gameStateVal;
	private static boolean pressedState;

	private static final int frameIntervalVal = 15;

	public static boolean getPressedState() {
		return pressedState;
	}

	public static void setPressedState(boolean _pressedState) {
		pressedState = _pressedState;
	}

	public enum GameState {
		inGame, pause, restart, end
	}

	public static GameState gameState() {
		return gameStateVal;
	}

	public static void setGameState(GameState input) {
		gameStateVal = input;
	}


	public static void updateScale() {
		stationSpeedVal = stationSpeedVal * screenScale;
		gravityVal = gravityVal * screenScale;
		screenFocusSpeedVal = screenFocusSpeedVal * screenScale;
		speedDiffVal = speedDiffVal * screenScale;
		smallestStationSpeedVal = smallestStationSpeedVal * screenScale;

		stationSizeDiffVal = (int)(stationSizeDiffVal * screenScale);
		heightDiffVal = (int)(heightDiffVal * screenScale);
		disDiffVal = (int)(disDiffVal * screenScale);
		smallestStationSizeVal = (int)(smallestStationSizeVal * screenScale);
		blockSizeVal = (int)(blockSizeVal * screenScale);
		avgStationHeightVal = (int)(180 * screenScale) + mainHeight / 4;
		playerSizeVal = (int)(playerSizeVal * screenScale);
	}

	public static int playerSize() {
		return playerSizeVal;
	}

	public static double smallestStationSpeed() {
		return smallestStationSpeedVal;
	}

	public static int smallestStationSize() {
		return smallestStationSizeVal;
	}

	public static double speedDiff() {
		return speedDiffVal;
	}

	public static int stationSizeDiff() {
		return stationSizeDiffVal;
	}

	public static int heightDiff() {
		return heightDiffVal;
	}

	public static int disDiff() {
		return disDiffVal;
	}

	public static int avgStationHeight() {
		return avgStationHeightVal;
	}

	public static int blockSize() {
		return blockSizeVal;
	}

	public static int getMainWidth() {
		return mainWidth;
	}

	public static void setMainWidth(int width) {
		mainWidth = width;
	}

	public static int getMainHeight() {
		return mainHeight;
	}

	public static void setMainHeight(int height) {
		mainHeight = height;

	}

	public static FreeSpace freeSpace() {
		return freeSpaceObj;
	}

	public static double stationSpeed() {
		return stationSpeedVal;
	}

	public static double screenFocusSpeed() {
		return screenFocusSpeedVal;
	}

	public static int frameInterval() {
		return frameIntervalVal;
	}

	public EnvVar() {
	}

	public static void updateHW(int height, int width) {
		mainHeight = height;
		mainWidth = width;

		screenScale = (double)mainWidth / 400; //400 is used for testing
		updateScale();
	}

	//init called in Game.java
	public static void init() {
		freeSpaceObj = new FreeSpace(gravityVal);
		gameStateVal = GameState.restart;
		pressedState = false;

		//width and hight are adjusted later in Board.java
		updateHW(700, 400);
	}
}