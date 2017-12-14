package com.leochen.swingball;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.Semaphore;
import android.app.Activity;

public class EnvVar {
	private static FreeSpace freeSpaceObj;
	private static int mainWidth;
	private static int mainHeight;

	private static double stationSpeedVal;
	private static double gravityVal;
	private static double screenFocusSpeedVal;
	private static double speedDiffVal;
	private static double smallestStationSpeedVal;
	private static double screenScale;
	private static int stationSizeDiffVal;
	private static int heightDiffVal;
	private static int disDiffVal;
	private static int smallestStationSizeVal;
	private static int blockSizeVal;
	private static int avgStationHeightVal;
	private static int playerSizeVal;
	private static int bestScore;
	private static GameState gameStateVal;
	private static boolean pressedState;
	private static Activity activity;
	private final static String bestScoreFileName = "bestScore.txt";

	public static int getBestScore() {
		return bestScore;
	}

	public static void setBestScore(int score) {
		if (score <= bestScore)
			return;
		bestScore = score;
	    try {
	    	FileOutputStream fos = activity.openFileOutput(bestScoreFileName,activity.MODE_PRIVATE);
	        OutputStreamWriter osw = new OutputStreamWriter(fos);
	        osw.write(Integer.toString(score));
	        osw.close();
	    }
	    catch (Exception e) {
	        System.out.println(e);
	    }
	}

	private static final int frameIntervalVal = 15;

	public static boolean getPressedState() {
		return pressedState;
	}

	public static void setPressedState(boolean _pressedState) {
		pressedState = _pressedState;
	}

	public enum GameState {
		inGame, restart, end
	}

	public static GameState gameState() {
		return gameStateVal;
	}

	public static void setGameState(GameState input) {
		gameStateVal = input;
	}


	public static void updateScale() {
		//speed: 6.6-8.4, size 50-100 height diff: 120, dis diff:50 (blocksize 200)
		stationSpeedVal = 6.8 * screenScale;
		gravityVal = 0.1 * screenScale;
		screenFocusSpeedVal = 6 * screenScale;
		speedDiffVal = 1.8 * screenScale;
		smallestStationSpeedVal = 4.4 * screenScale;

		stationSizeDiffVal = (int)(40 * screenScale);
		heightDiffVal = (int)(120 * screenScale);
		disDiffVal = (int)(50 * screenScale);
		smallestStationSizeVal = (int)(60 * screenScale);
		blockSizeVal = (int)(200 * screenScale);
		avgStationHeightVal = (int)(180 * screenScale) + mainHeight / 4;
		playerSizeVal = (int)(20 * screenScale);
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

		freeSpaceObj = new FreeSpace(gravityVal);
	}

	//init called in Game.java
	public static void init(Activity activity) {
		EnvVar.activity = activity;

		gameStateVal = GameState.end;
		pressedState = false;

	    try {
	        InputStream is = activity.openFileInput(bestScoreFileName);

	        if ( is != null ) {
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            bestScore = Integer.parseInt(br.readLine());
	            is.close();
	        } else {
	        	bestScore = 0;
	        }
	    }
	    catch (Exception e) {
	        System.out.println(e);
	    }

		//width and hight are adjusted later in Board.java
		updateHW(700, 400);
	}
}