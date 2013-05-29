package com.devdroid.kidz.free;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends Activity {
	final Context context = this;
	public void launchHome(Spinner difficultySpinner, Spinner totalProblemSpinner, Spinner timerValueSpinner, Intent intent, View view) {

		String difficulty = (String) difficultySpinner.getSelectedItem();
		String totalProblem = (String) totalProblemSpinner.getSelectedItem();
		String timerValue = (String) timerValueSpinner.getSelectedItem();
		
		intent.putExtra("difficulty", difficulty);
		intent.putExtra("total_problem", totalProblem);
		intent.putExtra("timer_value", timerValue);
	
		intent.putExtra("operation", ((Button) view).getText().toString().toLowerCase());
		startActivity(intent);
		return;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		final Spinner difficultySpinner = (Spinner) findViewById(R.id.spinner_difficulty);

		final Spinner totalProblemSpinner = (Spinner) findViewById(R.id.total_problem_spinner);
		
		final Spinner timerValueSpinner = (Spinner) findViewById(R.id.timer_value_spinner);
		
		Button additionButton = (Button) findViewById(R.id.button_addtion);

		final Intent intent = new Intent().setClass(this, HomeActivity.class);

		additionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				launchHome(difficultySpinner, totalProblemSpinner, timerValueSpinner, intent, v);
			}
		});

		Button subtractionButton = (Button) findViewById(R.id.button_subtraction);

		subtractionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				launchHome(difficultySpinner, totalProblemSpinner, timerValueSpinner, intent, v);
			}
		});

		Button multiplicationButton = (Button) findViewById(R.id.button_multiplication);

		multiplicationButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				launchHome(difficultySpinner, totalProblemSpinner, timerValueSpinner, intent, v);
			}
		});

		Button divisionButton = (Button) findViewById(R.id.button_division);

		divisionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				launchHome(difficultySpinner, totalProblemSpinner, timerValueSpinner, intent, v);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
