package com.leochen.swingball;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import android.util.Log;

public class Game extends GLSurfaceView {
    private final Board board;

	public Game(Context context) {
		super(context);
		bootstrap();

		setEGLContextClientVersion(2);

		board = new Board(context);

		setRenderer(board);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	private void bootstrap() {
		EnvVar.init();
	}

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	EnvVar.setPressedState(true);
        }

        return true;
    }
}