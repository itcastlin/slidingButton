package org.heima.slidingbutton;

import org.heima.slidingbutton.view.SlidingButton;
import org.heima.slidingbutton.view.SlidingButton.OnSwitchListener;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {
	private SlidingButton slidingButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		slidingButton = (SlidingButton) findViewById(R.id.slidingButton);
		slidingButton.setOnSwitchListener(new OnSwitchListener() {

			@Override
			public void onSwitch(boolean isPastHalf) {
				if (isPastHalf) {
					Toast.makeText(getApplicationContext(), "open", 0).show();
				} else {
					Toast.makeText(getApplicationContext(), "close", 0).show();
				}
			}
		});
	}

}
