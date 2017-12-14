package com.leochen.swingball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.app.Activity;

public class SwingBall extends AppCompatActivity {

    private static GLSurfaceView mGLView;

    public static void pauseGame() {
    	mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public static void resumeGame() {
    	mGLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swing_ball);
		if (savedInstanceState != null)
			return;

        bootstrap(this);

        mGLView = new Game(this);


        FrameLayout openglFrame = (FrameLayout)findViewById(R.id.openglFragment);
        openglFrame.addView(mGLView);

        UIComponent.init(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void bootstrap(Activity activity) {
        EnvVar.init(activity);
    }
}
