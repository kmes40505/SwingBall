package com.leochen.swingball;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.RelativeLayout;

public class UIComponent {
	private static TextView score;
	private static TextView bestScore;

	private static RelativeLayout menu;
    private static View menuPlaceHolder;

    private static Button menuButton;
    private static Button newGameButton;
    private static Button resumeButton;



	public static void init(Activity activity) {
        FrameLayout componentsFrame = (FrameLayout) activity.findViewById(R.id.UIFragment);
        LayoutInflater.from(activity).inflate(
        	R.layout.ui_components,
        	componentsFrame,
        	true);
        //press input(releasing ball)
        componentsFrame.setOnTouchListener(new FrameLayout.OnTouchListener() {
	        @Override
	        public boolean onTouch(View view, MotionEvent e) {
	        	if (EnvVar.gameState() != EnvVar.GameState.inGame)
	        		return true;

		        switch (e.getAction()) {
		            case MotionEvent.ACTION_DOWN:

		            	EnvVar.setPressedState(true);
		        }

		        return true;
	        }
        });

        menu = (RelativeLayout) activity.findViewById(R.id.menu);
        menuPlaceHolder = (View) activity.findViewById(R.id.menuPlaceHolder);

        menuButton = (Button)activity.findViewById(R.id.menuButton);
        newGameButton = (Button)activity.findViewById(R.id.newGameButton);
        resumeButton = (Button)activity.findViewById(R.id.resumeButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View w) {
        		menuButton.setEnabled(false);
        		EnvVar.pauseLock();
        		menuPlaceHolder.setVisibility(View.GONE);
        		menu.setVisibility(View.VISIBLE);
        		EnvVar.setGameState(EnvVar.GameState.pause);
        	}
        });

        newGameButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View w) {
        		menuButton.setEnabled(true);
        		menu.setVisibility(View.GONE);
        		menuPlaceHolder.setVisibility(View.VISIBLE);
        		resumeButton.setVisibility(View.VISIBLE);
        		EnvVar.setGameState(EnvVar.GameState.restart);
        		EnvVar.pauseUnlock();
        	}
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View w) {
        		menuButton.setEnabled(true);
        		menu.setVisibility(View.GONE);
        		menuPlaceHolder.setVisibility(View.VISIBLE);
        		EnvVar.setGameState(EnvVar.GameState.inGame);
        		EnvVar.pauseUnlock();
        	}
        });

        score = (TextView)activity.findViewById(R.id.Score);
        bestScore = (TextView)activity.findViewById(R.id.bestScore);
	}
}

