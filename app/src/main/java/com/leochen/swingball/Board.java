package com.leochen.swingball;

import com.leochen.swingball.units.*;

import java.util.LinkedList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.util.DisplayMetrics;
import android.view.WindowManager;


import static java.security.AccessController.getContext;


public class Board implements GLSurfaceView.Renderer {
	private Context context;
	private Player player;
	private LinkedList<Station> stationList;
	private LinkedList<Block> groundBlockList;
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
	private static long totalXOff;
	private static int totalYOff;

	private static long oldTime = 0;
	private static long sleepTime = 0;

	private final int frameInterval = EnvVar.frameInterval();

	public Board(Context context) {
		this.context = context;
	}

	public static long getTotalXOff() {
		return totalXOff;
	}

	public static void setTotalXOff(long input) {
		totalXOff = input;
	}

	public static int getTotalYOff() {
		return totalYOff;
	}

	public static void setTotalYOff(int input) {
		totalYOff = input;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0, 0, 0, 1);
		DrawObj.init();
		gl.glEnable(GL10.GL_BLEND);

		gl.glDisable(GL10.GL_DEPTH_TEST);

		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glDepthMask(false);
	}

	public void init() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
  			.getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;
		EnvVar.updateHW(height, width);

		Screen.init();
		initBoardComponents();
	}

	private void initBoardComponents() {
		totalXOff = 0;
		totalYOff = EnvVar.getMainHeight() / 2;
		stationList = new LinkedList<Station>();
		groundBlockList = new LinkedList<Block>();

		player = new Player(
			EnvVar.getMainWidth() / 4 + 1,
			totalYOff + 1,
			EnvVar.playerSize()
		);

		EnvVar.freeSpace().giveAction(player);

		stationList.add(new Station(
			EnvVar.getMainWidth() / 4,
			totalYOff,
			EnvVar.smallestStationSize()
		));

		for (int count = 5; count >=0; count--) {
			groundBlockList.add(new Block(
				EnvVar.getMainWidth() / 4 - EnvVar.blockSize() * count,
				totalYOff + EnvVar.avgStationHeight(),
				EnvVar.blockSize(),
				EnvVar.getMainHeight() / 2
			));
		}

		for (int count = 0; count < 5; count ++)
			addNextSection();
	}

	private void addNextSection() {
		int xStart = groundBlockList.getLast().getx() + EnvVar.blockSize();

		double stationSpeed = EnvVar.stationSpeed();
		double speedDiff = EnvVar.speedDiff();
		int stationSizeDiff = EnvVar.stationSizeDiff();
		int heightDiff = EnvVar.heightDiff();
		int disDiff = EnvVar.disDiff();

		Station stationObj = new Station(
			xStart + (int)(Math.random() * (disDiff + 1)) - disDiff / 2,
			totalYOff + (int)(Math.random() * (heightDiff + 1)) - heightDiff / 2,
			(int)(Math.random() * (stationSizeDiff + 1)) + EnvVar.smallestStationSize()
		);

		stationObj.setStationSpeed(EnvVar.smallestStationSpeed() + Math.random() * speedDiff);

		stationList.add(stationObj);

		groundBlockList.add(new Block(
			xStart,
			totalYOff + EnvVar.avgStationHeight(),
			EnvVar.blockSize(),
			EnvVar.getMainHeight() / 2
		));
	}

	@Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.orthoM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

	@Override
	public void onDrawFrame(GL10 unused) {
		sleepTime = frameInterval - (System.currentTimeMillis() - oldTime);
		sleepTime = sleepTime > 0? sleepTime : 0;
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			System.out.println("Interrupt happens in Board.java");
			System.out.println("Interrupted: " + e.getMessage());
		}
		oldTime = System.currentTimeMillis();

		switch(EnvVar.gameState()) {
			case inGame:
				update();
				break;
			case pause:
				//wait for lock to release
				EnvVar.pauseLock();
				EnvVar.pauseUnlock();
				return;
			case restart:
				init();
				EnvVar.setGameState(EnvVar.GameState.inGame);
				return;
			case end:
				return;
		}

        float[] scratch = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0, 0, 0, 0, -1, 0);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		for (Station station : stationList)
			station.draw(mMVPMatrix);

		for (Block block : groundBlockList)
			block.draw(mMVPMatrix);

		player.draw(mMVPMatrix);

	}

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("Board", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

	private void checkCollision() {
		//check if player is still changing action
		if (player.isShifting())
			return;

		//check if player hit station
		for (Station station : stationList) {
			CollisionFunc chkFunc = station.getColliFunc();
			if (chkFunc.chk(player)) {
				station.giveAction(player);
				Screen.focusOn(station,-EnvVar.getMainWidth() / 4, 0);
				return;
			}
		}

		//check if player hit ground
		for (Block block : groundBlockList) {
			CollisionFunc chkFunc = block.getColliFunc();
			if (chkFunc.chk(player)) {
				block.giveAction(player);
				EnvVar.setGameState(EnvVar.GameState.end);
				return;
			}
		}
	}

	public void updatePlayer() {
		if (EnvVar.getPressedState()) {
			EnvVar.freeSpace().giveAction(player);
			Screen.focusOn(player);
			EnvVar.setPressedState(false);
		}

		switch(player.getActionType()) {
			case none:
				break;
			case inAir:
				checkCollision();
				break;
			case inStation:

				break;
		}

		player.doAction();
	}

	public void updateComponents() {
		//remove section if it's 4 blocks back
		int oldestX = groundBlockList.getFirst().getx();
		if (oldestX < -7 * EnvVar.blockSize()) {
			addNextSection();
			stationList.removeFirst();
			groundBlockList.removeFirst();
		}
	}

	public void updateScreen() {
		Screen.updateScreen();

		for (Station station : stationList) {
			Screen.screenLocation(station);
		}

		for (Block block : groundBlockList) {
			Screen.screenLocation(block);
		}

		Screen.screenLocation(player);

	}

	public void update() {
		//update elements first
		updatePlayer();

		//update screen
		updateScreen();

		//update sections
		updateComponents();
	}

}