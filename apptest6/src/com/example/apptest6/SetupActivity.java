package com.example.apptest6;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class SetupActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		

		   Button button = (Button)findViewById(R.id.button1);
		   final RadioButton radio1 = (RadioButton)findViewById(R.id.radio0);
		   final RadioButton radio2 = (RadioButton)findViewById(R.id.radio1);
		   final RadioButton radio3 = (RadioButton)findViewById(R.id.radio2);
		   final EditText ed1 = (EditText)findViewById(R.id.editText2);
		   
		   SharedPreferences settings = getSharedPreferences("settings", this.MODE_WORLD_READABLE);
		   String hname = settings.getString("namehs", "none");
		   int hscore = settings.getInt("highscorehs", 0);
		   final TextView hsname = (TextView)findViewById(R.id.TextViewHighScoreName);
		   final TextView hsscore = (TextView)findViewById(R.id.TextViewHighScoreScore);
		   hsname.setText(hname);
		   hsscore.setText("" + hscore);
		   

		    button.setOnClickListener(new View.OnClickListener() 
		    {

		      @Override
		      public void onClick(View view) 
		      {
		        Intent intent = new Intent(SetupActivity.this, MainActivity.class);
		        
		        Log.e("setup", "intent created");
		        if (radio1.isChecked()){intent.putExtra("carchoice",1);}
		        else if (radio2.isChecked()){intent.putExtra("carchoice",2);}
		        else if (radio3.isChecked()){intent.putExtra("carchoice",3);}
		        
		        String name = ed1.getText().toString();
		        intent.putExtra("name",name);
		        
		        Log.e("setup", "carchoice set");
		        
		        startActivity(intent);
		      }

		    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_setup, menu);
		
		return true;
	}

}
