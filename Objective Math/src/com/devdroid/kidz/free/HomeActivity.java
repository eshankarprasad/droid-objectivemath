package com.devdroid.kidz.free;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class HomeActivity extends Activity 
{
	private String operation;
	private String difficulty;
	private Integer answer;
	private Random randomNumber1;
	private Random randomNumber2;
	final Context context = this;
	private Integer totalProblem;
	private Integer totalCorrectAnswer;
	private Integer totalWrongAnswer;
	private Integer totalMissed;
	CountDownTimer timer;

	private Integer totalProblemAllowed;
	private Integer totalTimeAlloewed;
	
	private Map<String, String> OPERATOR;

	// UI References
	private TextView textViewNum1;
	private TextView textViewNum2;
	private TextView textViewOperator;
	private RadioGroup answerRadioGroup;
	private ImageView resultImageView;
	private TextView resultTextView;
	private TextView counterTextView;
	private Button evaluateButton;
	private Button nextButton;
	private TextView textViewExpressionCounter;
	private RadioButton answerOptionRadio;
	
	private Boolean radioButtonChanged = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		savedInstanceState = this.getIntent().getExtras();

		Map<String, String> map = new HashMap<String, String>();
		map.put(getString(R.string.button_addition_label).toLowerCase(), "+");
		map.put(getString(R.string.button_subtraction_label).toLowerCase(), "-");
		map.put(getString(R.string.button_multiplication_label).toLowerCase(), "x");
		map.put(getString(R.string.button_division_label).toLowerCase(), "/");
		OPERATOR = Collections.unmodifiableMap(map);
		
		operation = (String) savedInstanceState.get("operation");
		difficulty = (String) savedInstanceState.get("difficulty");
		totalProblemAllowed = Integer.parseInt(((String) savedInstanceState.get("total_problem")));
		totalTimeAlloewed = getString(R.string.item_unlimited).equals((String) savedInstanceState.get("timer_value")) ? 100 : Integer.parseInt((String) savedInstanceState.get("timer_value"));
		//totalTimeAlloewed = (Integer) savedInstanceState.get("timer_value");

		Log.v("HomeActivity", "Operations: " + operation.toString()
				+ " Difficulty: " + difficulty);

		setContentView(R.layout.activity_home);

		totalProblem = 1;
		totalCorrectAnswer = 0;
		totalWrongAnswer = 0;
		totalMissed = 0;
		
		textViewExpressionCounter = (TextView) findViewById(R.id.textview_expression_counter);
		textViewExpressionCounter.setText(getString(R.string.serial_number) + totalProblem);

		randomNumber1 = new Random();
		randomNumber2 = new Random();

		textViewNum1 = (TextView) findViewById(R.id.textview_num1);
		textViewNum2 = (TextView) findViewById(R.id.textview_num2);
		textViewOperator = (TextView) findViewById(R.id.textview_operator);
		answerRadioGroup = (RadioGroup) findViewById(R.id.radiogroup_answers);
		resultImageView = (ImageView) findViewById(R.id.imageview_result);
		resultTextView = (TextView) findViewById(R.id.textview_result);
		counterTextView = (TextView) findViewById(R.id.textview_counter);
		evaluateButton = (Button) findViewById(R.id.button_evaluate);
		nextButton = (Button) findViewById(R.id.button_next_expression);
		
		timer = new CountDownTimer((totalTimeAlloewed+1)*1000, 1000) {

		     public void onTick(long millisUntilFinished) {
		    	 long time = millisUntilFinished / 1000;
		    	 counterTextView.setText(time < 2? getString(R.string.label_message_going_for_next_expression) : getString(R.string.label_message_time_remaining) + time);
		     }

		     public void onFinish() {
		    	 
		    	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		 				context);
		  
		 			// set title
		 			alertDialogBuilder.setTitle(getString(R.string.alert_title_alert));
		  
		 			// set dialog message
		 			alertDialogBuilder
		 				.setMessage(getString(R.string.alert_message_timeup))
		 				.setCancelable(false)
		 				.setPositiveButton(getString(R.string.button_next_label), new DialogInterface.OnClickListener() {
		 					public void onClick(DialogInterface dialog,int id) {
		 						// if this button is clicked, close
		 						// current activity
		 						//nextButton.performClick();
		 						nextProblem();
		 						dialog.cancel();
		 						totalMissed++;
		 					}
		 				  });
			 				/*.setNegativeButton("No",new DialogInterface.OnClickListener() {
			 					public void onClick(DialogInterface dialog,int id) {
			 						// if this button is clicked, just close
			 						// the dialog box and do nothing
			 						dialog.cancel();
			 					}
			 				});*/
		  
		 				// create alert dialog
		 				AlertDialog alertDialog = alertDialogBuilder.create();
		  
		 				// show it
		 				alertDialog.show();
		     }
		  }.start();
		
		if(totalTimeAlloewed == 100)
		{
			timer.cancel();
			counterTextView.setVisibility(View.INVISIBLE);
		}
		setExpression();
		
		setAllChoices(answerRadioGroup);
		
		nextButton.setVisibility(View.INVISIBLE);
		evaluateButton.setVisibility(View.INVISIBLE);
		resultTextView.setVisibility(View.INVISIBLE);
		resultImageView.setVisibility(View.INVISIBLE);
		
		answerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				//evaluateButton.setVisibility(View.VISIBLE);
				//evaluateButton.performClick();
				if(radioButtonChanged)
				{
					evaluateUserAnswer();
					
					Handler handler = new Handler();
					handler.postDelayed(new Runnable(){
					@Override
					      public void run(){
					        nextProblem();
					   }
					}, 1200);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void setRadioGroupEnable(RadioGroup radioGroup, boolean state) {
		//int x = radioGroup.getChildCount();
		for(int i = 0; i < radioGroup.getChildCount(); i++){
		    ((RadioButton)radioGroup.getChildAt(i)).setEnabled(state);
		}
	}
	
	public void setExpression()
	{
		int range = getString(R.string.item_easy).equals(difficulty) ? 50 : getString(R.string.item_medium).equals(difficulty) ? 1000 : 10000;
		int num1 = randomNumber1.nextInt(range) + 1;
		int num2 = randomNumber2.nextInt(range) + 1;

		if (getString(R.string.button_subtraction_label).equalsIgnoreCase(operation)) {
			if (num2 > num1) {

				int tmp = num2;
				num2 = num1;
				num1 = tmp;
			}
			
		} else if (getString(R.string.button_multiplication_label).equalsIgnoreCase(operation)) {
			range = getString(R.string.item_easy).equals(difficulty) ? 10 : getString(R.string.item_medium).equals(difficulty) ? 50 : 1000;
			num1 = getString(R.string.item_easy).equals(difficulty) ? randomNumber2.nextInt(range) + 1 : num1;
			num2 = randomNumber2.nextInt(range) + 1;
			if (num2 > num1) {

				int tmp = num2;
				num2 = num1;
				num1 = tmp;
			}
			
		} else if (getString(R.string.button_division_label).equalsIgnoreCase(operation)) {
			range = getString(R.string.item_easy).equals(difficulty) ? 10 : getString(R.string.item_medium).equals(difficulty) ? 50 : 100;
			num2 = randomNumber2.nextInt(range) + 1;
			if (num2 > num1) {

				int tmp = num2;
				num2 = num1;
				num1 = tmp;
			}
			num1 -= num1 % num2;
		}
		
		
		//expression = num1 + " " + OPERATOR.get(operation) + " " + num2;
		//Log.v("HomeActivity", num1 + " " + num2);
		
		textViewNum1.setText(num1 + "");
		textViewNum2.setText(num2 + "");
		textViewOperator.setText(OPERATOR.get(operation));
		
		answer = getAnswer(num1, num2, OPERATOR.get(operation));
	}
	
	public void setAllChoices(RadioGroup answerRadioGroup) {
		
		Random correctAnswerRandom = new Random();
		int correctAnswerIndex = correctAnswerRandom.nextInt(4);
		RadioButton correctRadioButton = (RadioButton) answerRadioGroup.getChildAt(correctAnswerIndex);
		correctRadioButton.setText(answer + "");
		
		RadioButton otherChoiceRadioButton;
		byte toggleFlag = 0;
		int otherAnswer = 0;
		byte plusMinusFlag = 0;
		for(int i=0; i<answerRadioGroup.getChildCount(); i++)
		{
			if(i == correctAnswerIndex)
				continue;
			
			otherChoiceRadioButton = (RadioButton) answerRadioGroup.getChildAt(i);
			if(toggleFlag == 0) {
				
				if(plusMinusFlag == 0) {
					otherAnswer = answer > 100 ? answer + 100 : answer + 10;
					otherAnswer = answer > 1000 ? answer + 1000 : otherAnswer;
					plusMinusFlag = 1;
				}
				else {
					otherAnswer = answer > 100 ? answer - 100 : answer - 10;
					otherAnswer = answer > 1000 ? answer - 1000 : otherAnswer;
					plusMinusFlag = 0;
				}
				//plusMinusFlag = (plusMinusFlag == 0)?1:0;
				
				toggleFlag = 1;
			}
			else if(toggleFlag == 1){
				otherAnswer = answer - 9;
				toggleFlag = 2;
			}
			else{
				otherAnswer = answer + 8;
				toggleFlag = 0;
			}
			
			otherAnswer = otherAnswer < 0 ? (-1) * otherAnswer : otherAnswer;
			otherChoiceRadioButton.setText(otherAnswer + "");
		}
	}
	
	public int getAnswer(int num1, int num2, String operator) {
		int ans = 0;
		if(OPERATOR.get(getString(R.string.button_addition_label).toLowerCase()).equals(operator)) {
			ans = num1 + num2;
		}
		else if(OPERATOR.get(getString(R.string.button_subtraction_label).toLowerCase()).equals(operator)) {
			ans = num1 - num2;
		}
		else if(OPERATOR.get(getString(R.string.button_multiplication_label).toLowerCase()).equals(operator)) {
			ans = num1 * num2;
		}
		else if(OPERATOR.get(getString(R.string.button_division_label).toLowerCase()).equals(operator)) {
			ans = num1 / num2;
		}
		return ans;
	}
	
	@Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
	}
	
	public void evaluateUserAnswer()
	{
		int selectedId1 = answerRadioGroup.getCheckedRadioButtonId();
		answerOptionRadio = (RadioButton) findViewById(selectedId1);
		int userAnswer = Integer.parseInt(answerOptionRadio.getText().toString());
		if(answer == userAnswer)
		{
			//Toast.makeText(HomeActivity.this, "Correct answer!", Toast.LENGTH_SHORT).show();
			resultImageView.setImageResource(R.drawable.correct_image);
			resultTextView.setText(getString(R.string.label_correct_answer));
			resultTextView.setTextColor(Color.parseColor("#22B14C"));
			totalCorrectAnswer++;
		}
		else
		{
			/*
			 Toast.makeText(HomeActivity.this, "Wrong answer!", Toast.LENGTH_SHORT).show();
			 Toast.makeText(HomeActivity.this, answer + " is right answer", Toast.LENGTH_SHORT).show();
			 */
			
			resultImageView.setImageResource(R.drawable.wrong_image);
			resultTextView.setText(getString(R.string.label_wrong_answer, answer));
			resultTextView.setTextColor(Color.RED);
			totalWrongAnswer++;
		}
		setRadioGroupEnable(answerRadioGroup, false);
		//nextButton.setVisibility(View.VISIBLE);
		//evaluateButton.setVisibility(View.INVISIBLE);
		resultImageView.setVisibility(View.VISIBLE);
		resultTextView.setVisibility(View.VISIBLE);
		//chronometerTimer.stop();
		timer.cancel();
		counterTextView.setText("");
		
		//nextProblem();
	}
	
	public void nextProblem()
	{	
		
		if(totalProblem < totalProblemAllowed)
		{
			setExpression();
			radioButtonChanged = false;
			answerRadioGroup.clearCheck();
			setAllChoices(answerRadioGroup);
			setRadioGroupEnable(answerRadioGroup, true);
			//nextButton.setVisibility(View.INVISIBLE);
			//evaluateButton.setVisibility(View.VISIBLE);
			resultImageView.setVisibility(View.INVISIBLE);
			resultTextView.setVisibility(View.INVISIBLE);
			//evaluateButton.setVisibility(View.INVISIBLE);
			timer.start();
			totalProblem++;
			textViewExpressionCounter.setText(getString(R.string.serial_number) + totalProblem);
			radioButtonChanged = true;
		}
		else
		{
			AlertDialog.Builder finishAlertDialogBuilder = new AlertDialog.Builder(
	 				context);
	  
	 			// set title
			finishAlertDialogBuilder.setTitle(getString(R.string.alert_title_score, ((totalCorrectAnswer  * 100) / totalProblem) + "%"));
	  
	 			// set dialog message
			finishAlertDialogBuilder
	 				.setMessage(
	 						getString(R.string.alert_message_score
	 								, totalProblem + ""
	 								, (totalProblem-totalMissed) + ""
	 								, totalCorrectAnswer + ""
	 								, totalWrongAnswer + ""))
	 				.setCancelable(false)
	 				.setPositiveButton(getString(R.string.alert_button_ok), new DialogInterface.OnClickListener() {
	 					public void onClick(DialogInterface dialog,int id) {
	 						// if this button is clicked, close
	 						// current activity
	 						dialog.cancel();
	 						finish();
	 					}
	 				  });
	  
	 				// create alert dialog
	 				AlertDialog alertDialog = finishAlertDialogBuilder.create();
	  
	 				// show it
	 				alertDialog.show();
		}
	
	}
}
