package com.leochen.swingball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public class SwingBall extends AppCompatActivity {

    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLView = new Game(this);
        //setContentView(mGLView);
        setContentView(R.layout.activity_swing_ball);

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
}
