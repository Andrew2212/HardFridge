package com.blogspot.hqup.hardfridge;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.blogspot.hqup.hardfridge.utils.Logger;
import com.hqup.hardfridge.R;

public class SplashScreenActivity extends Activity {

	ImageView ivStartTween;
	ImageView ivStartFrame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set FullScreen mode
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Hide status bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash_screen);

		Logger.v();
		ivStartTween = (ImageView) findViewById(R.id.ivStartTween);
		ivStartFrame = (ImageView) findViewById(R.id.ivStartFrame);

		ivStartTween.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.project_title_1w));
		ivStartFrame.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.frame_anim_start));

		new Thread(new AnimatorTween()).start();

	}

	private class AnimatorTween implements Runnable {
		public void run() {
			Logger.v();

			// Tween Animation
			Animation animationTween = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.icon_start);
			ivStartTween.startAnimation(animationTween);

			animationTween.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					Logger.v();
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					Logger.v();
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					Logger.v();
					// ivStartTween.post(new Thread( new Starter()));
					new Thread(new AnimatorFrame()).start();

					/*
					 * Wait 3,2 s. while the entire Animation will be done
					 * before'task' will be executed
					 */
					Timer timer = new Timer();
					TimerTaskerForIntent task = new TimerTaskerForIntent();
					timer.schedule(task, 3200);
				}
			});

		}
	}

	private class AnimatorFrame implements Runnable {
		public void run() {
			Logger.v();
			Drawable animationFrame = ivStartFrame.getBackground();
			((AnimationDrawable) animationFrame).start();

		}
	}

	private class TimerTaskerForIntent extends TimerTask {
		@Override
		public void run() {
			Logger.v();
			Intent intent = new Intent(getApplicationContext(),
					HomeActivity.class);
			startActivity(intent);
			finish();
		}

	}

}
