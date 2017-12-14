package com.leochen.swingball;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import android.util.Log;

public class Game extends GLSurfaceView {
    private final Board board;

	public Game(Activity context) {
		super(context);

		setEGLContextClientVersion(2);

		board = new Board(context);

		setRenderer(board);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}
}