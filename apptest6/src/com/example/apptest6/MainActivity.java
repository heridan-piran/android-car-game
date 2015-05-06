package com.example.apptest6;

import java.util.GregorianCalendar;

import com.example.apptest6.GameView.GameThread;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.WindowManager;


public class MainActivity extends Activity implements SensorListener
{
	GameView gameView;
	SensorManager sensors;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		sensors = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensors.registerListener(this,Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		gameView = (GameView) findViewById(R.id.GameViewID);
		gameView.setScreenSize(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());
		Log.e("status", "finish onCreate()");
		
		gameView.setVib((Vibrator)getSystemService(Context.VIBRATOR_SERVICE));
		
		Intent intent = getIntent(); 
		Log.e("status", "intent gotten");
		int carchoice = intent.getIntExtra("carchoice",1); 
		String playername = intent.getStringExtra("name");
		Log.e("status", "intent name " + playername);
		gameView.setMainPointer(this);
		gameView.setCarChoice(carchoice);
		gameView.setPlayerName(playername);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void endGame()
	{
		android.os.Process.killProcess(android.os.Process.myPid());
		gameView = null;
		this.finish();
	}
	
	@Override
	public void onStop()
	{
		gameView.getThread().stop();
	}

	//@Override
	public void onSensorChanged(int s, float[] axes)
	{
		gameView.setDeviceAxes(axes[0], axes[1], axes[2]);
	}
	public void onAccuracyChanged(int a ,int b)
	{
		
	}

}
