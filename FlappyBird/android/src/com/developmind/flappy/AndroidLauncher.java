package com.developmind.flappy;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.developmind.flappy.FlappyBird;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


public class AndroidLauncher extends AndroidApplication {

	private AdView adView;
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	private String ADS_KEY = "ca-app-pub-2597576948288169/4535392722";
	private boolean showAds = true;

	protected Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case SHOW_ADS:
				{
					adView.setVisibility(View.VISIBLE);
					break;
				}
				case HIDE_ADS:
				{
					adView.setVisibility(View.GONE);
					break;
				}
			}
		}
	};

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		RelativeLayout layout = new RelativeLayout(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		View gameView = initializeForView(new FlappyBird(), config);

		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(ADS_KEY);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

		adView.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				super.onAdClosed();
				Toast.makeText(AndroidLauncher.this, "OnClosed", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAdFailedToLoad(int i) {
				super.onAdFailedToLoad(i);

				Toast.makeText(AndroidLauncher.this, "onAdFailedToLoad "+i, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAdLeftApplication() {
				super.onAdLeftApplication();
				Toast.makeText(AndroidLauncher.this, "onAdLeftApplication ", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAdOpened() {
				super.onAdOpened();
				Toast.makeText(AndroidLauncher.this, "onAdOpened ", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAdLoaded() {
				super.onAdLoaded();
				Toast.makeText(AndroidLauncher.this, "onAdLoaded ", Toast.LENGTH_SHORT).show();
			}
		});

		layout.addView(gameView);

		RelativeLayout.LayoutParams adParams =
				new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		layout.addView(adView, adParams);

		setContentView(layout);

	}



}
