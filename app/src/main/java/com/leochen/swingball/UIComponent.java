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
	private static RelativeLayout menu;

    private static Button menuButton;
    private static Button newGameButton;
    private static Button resumeButton;
    private static Activity activity;
    private static TextView score;
    private static TextView bestScore;


    public static void updateScore(final long num) {
    	activity.runOnUiThread(new Runnable(){
    		@Override
    		public void run() {
    			if (score != null)
	    			score.setText("Score: " + num);
    		}
    	});
    }

    public static void updateBestScore(final long num) {
    	activity.runOnUiThread(new Runnable(){
    		@Override
    		public void run() {
    			if (bestScore != null)
	    			bestScore.setText("Best Score: " + num);
    		}
    	});
    }

    public static void startLayout() {
		Runnable action = new Runnable() {
    		@Override
    		public void run() {
    			resumeButton.setVisibility(View.GONE);
        		menuButton.performClick();
    		}
    	};

    	activity.runOnUiThread(action);
    }

	public static void init(Activity activity) {
		UIComponent.activity = activity;
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

        menuButton = (Button)activity.findViewById(R.id.menuButton);
        newGameButton = (Button)activity.findViewById(R.id.newGameButton);
        resumeButton = (Button)activity.findViewById(R.id.resumeButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View w) {
        		menuButton.setEnabled(false);
        		UIComponent.updateBestScore(EnvVar.getBestScore());
        		menu.setVisibility(View.VISIBLE);
                SwingBall.pauseGame();
        	}
        });

        newGameButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View w) {
        		menuButton.setEnabled(true);
        		menu.setVisibility(View.GONE);
        		resumeButton.setVisibility(View.VISIBLE);
        		EnvVar.setGameState(EnvVar.GameState.restart);
                SwingBall.resumeGame();
        	}
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View w) {
        		menuButton.setEnabled(true);
        		menu.setVisibility(View.GONE);
        		EnvVar.setGameState(EnvVar.GameState.inGame);
                SwingBall.resumeGame();
        	}
        });

        score = (TextView)activity.findViewById(R.id.Score);
        bestScore = (TextView)activity.findViewById(R.id.bestScore);

        UIComponent.updateScore(Board.getScore());
        UIComponent.updateBestScore(EnvVar.getBestScore());
	}
}