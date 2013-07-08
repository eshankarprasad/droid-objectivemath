package com.devdroid.kidz.free;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class MainActivity extends Activity 
{
	final Context context = this;
	
	// UI References 
	private Spinner difficultySpinner;
	private Spinner totalProblemSpinner;
	private Spinner timerValueSpinner;
	private Button additionButton;
	private Button subtractionButton;
	private Button multiplicationButton;
	private Button divisionButton;
	private RadioGroup radioGroupPlayers;
	private EditText editText1stPlayerName;
	private EditText editText2ndPlayerName;
	private RadioButton playersOption;
	private View firstPlayerNameView;
	private View secondPlayerNameView;
	
	public void launchHome(Intent intent, View view) 
	{
		String difficulty = (String) difficultySpinner.getSelectedItem();
		String totalProblem = (String) totalProblemSpinner.getSelectedItem();
		String timerValue = (String) timerValueSpinner.getSelectedItem();
		
		intent.putExtra("difficulty", difficulty);
		intent.putExtra("total_problem", totalProblem);
		intent.putExtra("timer_value", timerValue);
		intent.putExtra("operation", ((Button) view).getText().toString().toLowerCase());
		intent.putExtra("player_mode", "single");
		
		switch (radioGroupPlayers.getCheckedRadioButtonId()) 
		{
			case R.id.single_player_radio:
				startActivity(intent);
				break;
	
			case R.id.double_player_radio:
				View focusView = null;
				editText1stPlayerName.setError(null);
				editText2ndPlayerName.setError(null);
				
				if(TextUtils.isEmpty(editText2ndPlayerName.getText()))
				{
					focusView = editText2ndPlayerName;
					editText2ndPlayerName.setError("This field is required!");
				}
				
				if(TextUtils.isEmpty(editText1stPlayerName.getText()))
				{
					focusView = editText1stPlayerName;
					editText1stPlayerName.setError("This field is required!");
				}
				
				if(focusView == null)
				{
					intent.putExtra("player_mode", "double");
					intent.putExtra("first_player_name", editText1stPlayerName.getText().toString());
					intent.putExtra("second_player_name", editText2ndPlayerName.getText().toString());
					startActivity(intent);
				}
				else
				{
					((EditText) focusView).setError("This field is required!");
					((EditText) focusView).requestFocus();
					
					if(TextUtils.isEmpty(editText2ndPlayerName.getText()) && TextUtils.isEmpty(editText1stPlayerName.getText()))
					{
						editText2ndPlayerName.setError("This field is required!");
						editText1stPlayerName.setError("This field is required!");
						editText1stPlayerName.requestFocus();
					}
				}
				
				break;
		}
		
		return;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
	
		difficultySpinner = (Spinner) findViewById(R.id.spinner_difficulty);
		totalProblemSpinner = (Spinner) findViewById(R.id.total_problem_spinner);
		timerValueSpinner = (Spinner) findViewById(R.id.timer_value_spinner);
		additionButton = (Button) findViewById(R.id.button_addtion);
		subtractionButton = (Button) findViewById(R.id.button_subtraction);
		multiplicationButton = (Button) findViewById(R.id.button_multiplication);
		divisionButton = (Button) findViewById(R.id.button_division);
		
		radioGroupPlayers = (RadioGroup) findViewById(R.id.radiogroup_players);
		editText1stPlayerName = (EditText) findViewById(R.id.editText_1st_player_name);
		editText2ndPlayerName = (EditText) findViewById(R.id.editText_2nd_player_name);
		
		firstPlayerNameView = ((LinearLayout) findViewById(R.id.linear_layout_row_player1_name));
    	secondPlayerNameView = ((LinearLayout) findViewById(R.id.linear_layout_row_player2_name));
		
		final Intent intent = new Intent().setClass(this, HomeActivity.class);

		additionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				launchHome(intent, v);
			}
		});

		subtractionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				launchHome(intent, v);
			}
		});

		multiplicationButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				launchHome(intent, v);
			}
		});

		divisionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				launchHome(intent, v);
			}
		});
		
		radioGroupPlayers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				playersOption = (RadioButton) findViewById(checkedId);
				
				switch (checkedId) 
				{
	                case R.id.single_player_radio:
	                	((RadioButton) findViewById(R.id.double_player_radio)).setChecked(false);
	                	editText1stPlayerName.setError(null);
	    				editText2ndPlayerName.setError(null);
	                	firstPlayerNameView.setVisibility(View.GONE);
	                	secondPlayerNameView.setVisibility(View.GONE);
	                	break;
	                case R.id.double_player_radio:
	                	((RadioButton) findViewById(R.id.single_player_radio)).setChecked(false);
	                	firstPlayerNameView.setVisibility(View.VISIBLE);
	                	secondPlayerNameView.setVisibility(View.VISIBLE);
	                	break;
				}
	            
				//Log.d("Main Activity", playersOption.getText().toString());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
