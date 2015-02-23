package sk.svb.lgg3.svb_circlecase_rocket.game;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sk.svb.lgg3.svb_circlecase_rocket.R;
import sk.svb.lgg3.svb_circlecase_rocket.activity.CurrentScoreActivity;
import sk.svb.lgg3.svb_circlecase_rocket.activity.FullScreenActivity;
import sk.svb.lgg3.svb_circlecase_rocket.activity.QCircleMainActivity;
import sk.svb.lgg3.svb_circlecase_rocket.logic.GameStats;
import sk.svb.lgg3.svb_circlecase_rocket.logic.QcActivity;
import sk.svb.lgg3.svb_circlecase_rocket.view.AccelerometerView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class QcAccelerometerActivity extends QcActivity {

	// sensor
	private SensorManager mSensorManager;
	private boolean isAccelerometerRegistered = false;

	// view
	private AccelerometerView mAccelerometerView;

	private GameStats gs;
	public TextView scoreTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.game_accelerometer);
		scoreTextView = (TextView) findViewById(R.id.actual_score);

		onCreateQcActivity();

		mAccelerometerView = (AccelerometerView) findViewById(R.id.sv);
		mAccelerometerView.setActivit(this);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometerView.startDrawImage();

		gs = new GameStats();
		gs.setScore(0);
		updateScore();

		Date date = new Date(System.currentTimeMillis());
		String time = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(date);
		gs.setDate(time);

	}

	public void updateScore() {
		gs.setScore(gs.getScore() + 1);
		scoreTextView.setText(getString(R.string.actual_score) + ": "
				+ gs.getScore());
	}

	@Override
	public void onResume() {
		super.onResume();
		registerSensor();
				
		registerReceiver(mIntentReceiver, new IntentFilter("game_update"));
	}

	@Override
	public void onPause() {
		unregisterSensor();
		unregisterReceiver(mIntentReceiver);
		gs.save();
		super.onStop();
	}

	private void endGame() {
		finish();
		Intent i = new Intent(getApplicationContext(),
				CurrentScoreActivity.class);
		i.putExtra("score", gs.getScore());
		startActivity(i);
	}

	private final SensorEventListener mSensorAccelerometerEventListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// mAccelX = 0 - event.values[2];
			// mAccelY = 0 - event.values[1];
			// mAccelZ = event.values[0];

			if (event == null)
				return;

			int forwBack = (int) (event.values[1]);
			if (forwBack > 40) {
				forwBack = 40;
			}
			if (forwBack < -40) {
				forwBack = -40;
			}

			int lefRig = (int) (event.values[2]);
			if (lefRig > 40) {
				lefRig = 40;
			}
			if (lefRig < -40) {
				lefRig = -40;
			}

			mAccelerometerView.setCoords(-lefRig, -forwBack);
		}

	};

	private void registerSensor() {
		try {
			// showAllSensors();
			List<Sensor> sensorList = mSensorManager
					.getSensorList(Sensor.TYPE_ORIENTATION);
			mSensorManager.registerListener(mSensorAccelerometerEventListener,
					sensorList.get(0), SensorManager.SENSOR_DELAY_GAME);
			isAccelerometerRegistered = true;
		} catch (Exception e) {
			isAccelerometerRegistered = false;
		}
	}

	private void unregisterSensor() {
		if (!isAccelerometerRegistered)
			mSensorManager
					.unregisterListener(mSensorAccelerometerEventListener);

	}

	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			if (intent.getAction() != null
					&& intent.getAction().equals("game_update")) {
				if (intent.getBooleanExtra("score", false)){					
					updateScore();
				}
				if (intent.getBooleanExtra("end", false)){					
					endGame();
				}
			}

		}
	};
}
