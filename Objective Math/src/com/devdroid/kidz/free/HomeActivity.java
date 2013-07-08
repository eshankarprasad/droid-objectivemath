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
	private Integer answerPlayer1;
	private Integer answerPlayer2;
	
	private Random randomNumber1Player1;
	private Random randomNumber1Player2;
	
	private Random randomNumber2Player1;
	private Random randomNumber2Player2;
	
	final Context context = this;
	private Integer totalProblemPassedPlayer1;
	private Integer totalProblemPassedPlayer2;
	
	private Integer totalCorrectAnswerPlayer1;
	private Integer totalCorrectAnswerPlayer2;
	
	private Integer totalWrongAnswerPlayer1;
	private Integer totalWrongAnswerPlayer2;
	
	private Integer totalMissedPlayer1;
	private Integer totalMissedPlayer2;
	
	private String playerMode;
	private String firstPlayerName;
	private String secondPlayerName;
	
	private CountDownTimer timerPlayer1;
	private CountDownTimer timerPlayer2;

	private Integer totalProblemAllowed;
	private Integer totalTimeAlloewed;
	
	private Map<String, String> OPERATOR;

	// UI References
	private TextView textViewNum1Player1;
	private TextView textViewNum1Player2;
	
	private TextView textViewNum2Player1;
	private TextView textViewNum2Player2;
	
	private TextView textViewOperatorPlayer1;
	private TextView textViewOperatorPlayer2;
	
	private RadioGroup answerRadioGroupPlayer1;
	private RadioGroup answerRadioGroupPlayer2;
	
	private ImageView resultImageViewPlayer1;
	private ImageView resultImageViewPlayer2;
	
	private TextView resultTextViewPlayer1;
	private TextView resultTextViewPlayer2;
	
	private TextView counterTextViewPlayer1;
	private TextView counterTextViewPlayer2;
	
	private TextView textViewExpressionCounterPlayer1;
	private TextView textViewExpressionCounterPlayer2;
	
	private Button buttonTimeUpPlayer1;
	private Button buttonTimeUpPlayer2;
	
	private Boolean radioButtonChangedPlayer1 = true;
	private Boolean radioButtonChangedPlayer2 = true;
	
	private Boolean timerTaskPlayer1 = false;
	private Boolean timerTaskPlayer2 = false;
	
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
		//totalProblemAllowed = 5;
		totalTimeAlloewed = getString(R.string.item_unlimited).equals((String) savedInstanceState.get("timer_value")) 
				? 100 : Integer.parseInt((String) savedInstanceState.get("timer_value"));
		//totalTimeAlloewed = 5;
		
		playerMode = (String) savedInstanceState.get("player_mode");
		if("double".equals(playerMode))
		{
			firstPlayerName = (String) savedInstanceState.get("first_player_name");
			secondPlayerName = (String) savedInstanceState.get("second_player_name");
			setContentView(R.layout.activity_home_2_player);
		}
		else
		{
			setContentView(R.layout.activity_home);
		}

		//Log.v("HomeActivity", "Operations: " + operation.toString() + " Difficulty: " + difficulty);

		totalProblemPassedPlayer1 = 1;
		totalCorrectAnswerPlayer1 = 0;
		totalWrongAnswerPlayer1 = 0;
		totalMissedPlayer1 = 0;
		
		textViewExpressionCounterPlayer1 = (TextView) findViewById(R.id.textview_expression_counter_player1);
		textViewExpressionCounterPlayer1.setText("double".equals(playerMode) ? firstPlayerName + " Zone      " + getString(R.string.serial_number) + totalProblemPassedPlayer1 : ""  
							+ getString(R.string.serial_number) + totalProblemPassedPlayer1);

		randomNumber1Player1 = new Random();
		randomNumber2Player1 = new Random();

		textViewNum1Player1 = (TextView) findViewById(R.id.textview_num1_player1);
		textViewNum2Player1 = (TextView) findViewById(R.id.textview_num2_player1);
		textViewOperatorPlayer1 = (TextView) findViewById(R.id.textview_operator_player1);
		answerRadioGroupPlayer1 = (RadioGroup) findViewById(R.id.radiogroup_answers_player1);
		resultImageViewPlayer1 = (ImageView) findViewById(R.id.imageview_result_player1);
		resultTextViewPlayer1 = (TextView) findViewById(R.id.textview_result_player1);
		counterTextViewPlayer1 = (TextView) findViewById(R.id.textview_counter_player1);
		buttonTimeUpPlayer1 = (Button) findViewById(R.id.button_time_up_player1);
		
		timerPlayer1 = new CountDownTimer((totalTimeAlloewed+1)*1000, 1000) 
		{
		     public void onTick(long millisUntilFinished) 
		     {
		    	 long time = millisUntilFinished / 1000;
		    	 counterTextViewPlayer1.setText(time < 2? getString(R.string.label_message_going_for_next_expression) : getString(R.string.label_message_time_remaining) + time);
		    	 timerTaskPlayer1 = true;
		     }

		     public void onFinish() 
		     {
		    	 timerTaskPlayer1 = false;
		    	 buttonTimeUpPlayer1.setVisibility(View.VISIBLE);
		    	 setRadioGroupEnable(answerRadioGroupPlayer1, false);
		    	 answerRadioGroupPlayer1.setVisibility(View.INVISIBLE);
		    	 totalMissedPlayer1++;
		     }
		  }.start();
		
		if(totalTimeAlloewed == 100)
		{
			if(timerTaskPlayer1)
				timerPlayer1.cancel();
			
			counterTextViewPlayer1.setVisibility(View.INVISIBLE);
		}
		setExpressionForFirstPlayer();
		
		setAllChoices(answerRadioGroupPlayer1, answerPlayer1);
		
		resultTextViewPlayer1.setVisibility(View.INVISIBLE);
		resultImageViewPlayer1.setVisibility(View.INVISIBLE);
		
		answerRadioGroupPlayer1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if(radioButtonChangedPlayer1)
				{
					evaluateUserAnswerPlayer1();
					
					Handler handler = new Handler();
					handler.postDelayed(new Runnable(){
					@Override
					      public void run(){
					        nextProblemPlayer1();
					   }
					}, 1200);
				}
			}
		});
		
		buttonTimeUpPlayer1.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				nextProblemPlayer1();
				buttonTimeUpPlayer1.setVisibility(View.GONE);
				setRadioGroupEnable(answerRadioGroupPlayer1, true);
				answerRadioGroupPlayer1.setVisibility(View.VISIBLE);
			}
		});
		
		if(playerMode.equals("double"))
		{
			totalProblemPassedPlayer2 = 1;
			totalCorrectAnswerPlayer2 = 0;
			totalWrongAnswerPlayer2 = 0;
			totalMissedPlayer2 = 0;
			
			textViewExpressionCounterPlayer2 = (TextView) findViewById(R.id.textview_expression_counter_player2);
			textViewExpressionCounterPlayer2.setText(secondPlayerName + " Zone      " 
						+ getString(R.string.serial_number) + totalProblemPassedPlayer2);

			randomNumber1Player2 = new Random();
			randomNumber2Player2 = new Random();

			textViewNum1Player2 = (TextView) findViewById(R.id.textview_num1_player2);
			textViewNum2Player2 = (TextView) findViewById(R.id.textview_num2_player2);
			textViewOperatorPlayer2 = (TextView) findViewById(R.id.textview_operator_player2);
			answerRadioGroupPlayer2 = (RadioGroup) findViewById(R.id.radiogroup_answers_player2);
			resultImageViewPlayer2 = (ImageView) findViewById(R.id.imageview_result_player2);
			resultTextViewPlayer2 = (TextView) findViewById(R.id.textview_result_player2);
			counterTextViewPlayer2 = (TextView) findViewById(R.id.textview_counter_player2);
			buttonTimeUpPlayer2 = (Button) findViewById(R.id.button_time_up_player2);
			
			timerPlayer2 = new CountDownTimer((totalTimeAlloewed+1)*1000, 1000) 
			{
			     public void onTick(long millisUntilFinished) 
			     {
			    	 timerTaskPlayer2 = true;
			    	 long time = millisUntilFinished / 1000;
			    	 counterTextViewPlayer2.setText(time < 2? getString(R.string.label_message_going_for_next_expression) : getString(R.string.label_message_time_remaining) + time);
			     }

			     public void onFinish() 
			     {
			    	 timerTaskPlayer2 = false;
			    	 buttonTimeUpPlayer2.setVisibility(View.VISIBLE);
			    	 setRadioGroupEnable(answerRadioGroupPlayer2, false);
			    	 answerRadioGroupPlayer2.setVisibility(View.INVISIBLE);
			    	 totalMissedPlayer2++;
			     }
			  }.start();
			
			if(totalTimeAlloewed == 100)
			{
				if(timerTaskPlayer2)
					timerPlayer2.cancel();
				
				counterTextViewPlayer2.setVisibility(View.INVISIBLE);
			}
			setExpressionForSecondPlayer();
			
			setAllChoices(answerRadioGroupPlayer2, answerPlayer2);
			
			resultTextViewPlayer2.setVisibility(View.INVISIBLE);
			resultImageViewPlayer2.setVisibility(View.INVISIBLE);
			
			answerRadioGroupPlayer2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() 
			{
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {

					if(radioButtonChangedPlayer2)
					{
						evaluateUserAnswerPlayer2();
						
						Handler handler = new Handler();
						handler.postDelayed(new Runnable(){
						@Override
						      public void run(){
						        nextProblemPlayer2();
						   }
						}, 1200);
					}
				}
			});
			
			buttonTimeUpPlayer2.setOnClickListener(new View.OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					nextProblemPlayer2();
					buttonTimeUpPlayer2.setVisibility(View.GONE);
					setRadioGroupEnable(answerRadioGroupPlayer2, true);
					answerRadioGroupPlayer2.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void setRadioGroupEnable(RadioGroup radioGroup, boolean state) 
	{
		for(int i = 0; i < radioGroup.getChildCount(); i++)
		{
		    ((RadioButton)radioGroup.getChildAt(i)).setEnabled(state);
		}
	}
	
	public void setExpressionForFirstPlayer()
	{
		int range = getString(R.string.item_easy).equals(difficulty) ? 50 : getString(R.string.item_medium).equals(difficulty) ? 1000 : 10000;
		int num1 = randomNumber1Player1.nextInt(range) + 1;
		int num2 = randomNumber2Player1.nextInt(range) + 1;

		if (getString(R.string.button_subtraction_label).equalsIgnoreCase(operation)) {
			if (num2 > num1) {

				int tmp = num2;
				num2 = num1;
				num1 = tmp;
			}
			
		} else if (getString(R.string.button_multiplication_label).equalsIgnoreCase(operation)) {
			range = getString(R.string.item_easy).equals(difficulty) ? 10 : getString(R.string.item_medium).equals(difficulty) ? 50 : 1000;
			num1 = getString(R.string.item_easy).equals(difficulty) ? randomNumber2Player1.nextInt(range) + 1 : num1;
			num2 = randomNumber2Player1.nextInt(range) + 1;
			if (num2 > num1) {

				int tmp = num2;
				num2 = num1;
				num1 = tmp;
			}
			
		} else if (getString(R.string.button_division_label).equalsIgnoreCase(operation)) {
			range = getString(R.string.item_easy).equals(difficulty) ? 10 : getString(R.string.item_medium).equals(difficulty) ? 50 : 100;
			num2 = randomNumber2Player1.nextInt(range) + 1;
			if (num2 > num1) {

				int tmp = num2;
				num2 = num1;
				num1 = tmp;
			}
			num1 -= num1 % num2;
		}
		
		
		//expression = num1 + " " + OPERATOR.get(operation) + " " + num2;
		//Log.v("HomeActivity", num1 + " " + num2);
		
		textViewNum1Player1.setText(num1 + "");
		textViewNum2Player1.setText(num2 + "");
		textViewOperatorPlayer1.setText(OPERATOR.get(operation));
		
		answerPlayer1 = getAnswer(num1, num2, OPERATOR.get(operation));
	}
	
	public void setExpressionForSecondPlayer()
	{
		int range = getString(R.string.item_easy).equals(difficulty) ? 50 : getString(R.string.item_medium).equals(difficulty) ? 1000 : 10000;
		int num1 = randomNumber1Player2.nextInt(range) + 1;
		int num2 = randomNumber2Player2.nextInt(range) + 1;

		if (getString(R.string.button_subtraction_label).equalsIgnoreCase(operation)) {
			if (num2 > num1) {

				int tmp = num2;
				num2 = num1;
				num1 = tmp;
			}
			
		} else if (getString(R.string.button_multiplication_label).equalsIgnoreCase(operation)) {
			range = getString(R.string.item_easy).equals(difficulty) ? 10 : getString(R.string.item_medium).equals(difficulty) ? 50 : 1000;
			num1 = getString(R.string.item_easy).equals(difficulty) ? randomNumber2Player2.nextInt(range) + 1 : num1;
			num2 = randomNumber2Player2.nextInt(range) + 1;
			if (num2 > num1) {

				int tmp = num2;
				num2 = num1;
				num1 = tmp;
			}
			
		} else if (getString(R.string.button_division_label).equalsIgnoreCase(operation)) {
			range = getString(R.string.item_easy).equals(difficulty) ? 10 : getString(R.string.item_medium).equals(difficulty) ? 50 : 100;
			num2 = randomNumber2Player2.nextInt(range) + 1;
			if (num2 > num1) {

				int tmp = num2;
				num2 = num1;
				num1 = tmp;
			}
			num1 -= num1 % num2;
		}
		
		
		//expression = num1 + " " + OPERATOR.get(operation) + " " + num2;
		//Log.v("HomeActivity", num1 + " " + num2);
		
		textViewNum1Player2.setText(num1 + "");
		textViewNum2Player2.setText(num2 + "");
		textViewOperatorPlayer2.setText(OPERATOR.get(operation));
		
		answerPlayer2 = getAnswer(num1, num2, OPERATOR.get(operation));
	}
	
	public void setAllChoices(RadioGroup answerRadioGroup, Integer answer) {
		
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
			if(toggleFlag == 0) 
			{
				
				if(plusMinusFlag == 0) 
				{
					otherAnswer = answer > 100 ? answer + 100 : answer + 10;
					otherAnswer = answer > 1000 ? answer + 1000 : otherAnswer;
					plusMinusFlag = 1;
				}
				else 
				{
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
	
	public int getAnswer(int num1, int num2, String operator) 
	{
		int ans = 0;
		if(OPERATOR.get(getString(R.string.button_addition_label).toLowerCase()).equals(operator)) 
		{
			ans = num1 + num2;
		}
		else if(OPERATOR.get(getString(R.string.button_subtraction_label).toLowerCase()).equals(operator)) 
		{
			ans = num1 - num2;
		}
		else if(OPERATOR.get(getString(R.string.button_multiplication_label).toLowerCase()).equals(operator)) 
		{
			ans = num1 * num2;
		}
		else if(OPERATOR.get(getString(R.string.button_division_label).toLowerCase()).equals(operator)) 
		{
			ans = num1 / num2;
		}
		return ans;
	}
	
	@Override
    public void onDestroy() 
	{
        super.onDestroy();
        timerPlayer1.cancel();
        
        if("double".equals(playerMode))
		{
        	timerPlayer2.cancel();
		}        
	}
	
	public void evaluateUserAnswerPlayer1()
	{
		int selectedId1 = answerRadioGroupPlayer1.getCheckedRadioButtonId();
		RadioButton answerOptionRadio = (RadioButton) findViewById(selectedId1);
		int userAnswer = Integer.parseInt(answerOptionRadio.getText().toString());
		if(answerPlayer1 == userAnswer)
		{
			//Toast.makeText(HomeActivity.this, "Correct answer!", Toast.LENGTH_SHORT).show();
			resultImageViewPlayer1.setImageResource(R.drawable.correct_image);
			resultTextViewPlayer1.setText(getString(R.string.label_correct_answer));
			resultTextViewPlayer1.setTextColor(Color.parseColor("#22B14C"));
			totalCorrectAnswerPlayer1++;
		}
		else
		{
			/*
			 Toast.makeText(HomeActivity.this, "Wrong answer!", Toast.LENGTH_SHORT).show();
			 Toast.makeText(HomeActivity.this, answer + " is right answer", Toast.LENGTH_SHORT).show();
			 */
			
			resultImageViewPlayer1.setImageResource(R.drawable.wrong_image);
			resultTextViewPlayer1.setText(getString(R.string.label_wrong_answer, answerPlayer1));
			resultTextViewPlayer1.setTextColor(Color.RED);
			totalWrongAnswerPlayer1++;
		}
		setRadioGroupEnable(answerRadioGroupPlayer1, false);
		//nextButton.setVisibility(View.VISIBLE);
		//evaluateButton.setVisibility(View.INVISIBLE);
		resultImageViewPlayer1.setVisibility(View.VISIBLE);
		resultTextViewPlayer1.setVisibility(View.VISIBLE);
		//chronometerTimer.stop();
		timerPlayer1.cancel();
		counterTextViewPlayer1.setText("");
	}
	
	public void evaluateUserAnswerPlayer2()
	{
		int selectedId1 = answerRadioGroupPlayer2.getCheckedRadioButtonId();
		RadioButton answerOptionRadio = (RadioButton) findViewById(selectedId1);
		int userAnswer = Integer.parseInt(answerOptionRadio.getText().toString());
		if(answerPlayer2 == userAnswer)
		{
			//Toast.makeText(HomeActivity.this, "Correct answer!", Toast.LENGTH_SHORT).show();
			resultImageViewPlayer2.setImageResource(R.drawable.correct_image);
			resultTextViewPlayer2.setText(getString(R.string.label_correct_answer));
			resultTextViewPlayer2.setTextColor(Color.parseColor("#22B14C"));
			totalCorrectAnswerPlayer2++;
		}
		else
		{
			/*
			 Toast.makeText(HomeActivity.this, "Wrong answer!", Toast.LENGTH_SHORT).show();
			 Toast.makeText(HomeActivity.this, answer + " is right answer", Toast.LENGTH_SHORT).show();
			 */
			
			resultImageViewPlayer2.setImageResource(R.drawable.wrong_image);
			resultTextViewPlayer2.setText(getString(R.string.label_wrong_answer, answerPlayer2));
			resultTextViewPlayer2.setTextColor(Color.RED);
			totalWrongAnswerPlayer2++;
		}
		setRadioGroupEnable(answerRadioGroupPlayer2, false);
		//nextButton.setVisibility(View.VISIBLE);
		//evaluateButton.setVisibility(View.INVISIBLE);
		resultImageViewPlayer2.setVisibility(View.VISIBLE);
		resultTextViewPlayer2.setVisibility(View.VISIBLE);
		//chronometerTimer.stop();
		timerPlayer2.cancel();
		counterTextViewPlayer2.setText("");
	}
	
	public void nextProblemPlayer1()
	{	
		if(checkGameStatus())
		{
			setExpressionForFirstPlayer();
			
			radioButtonChangedPlayer1 = false;
			answerRadioGroupPlayer1.clearCheck();
			setAllChoices(answerRadioGroupPlayer1, answerPlayer1);
			setRadioGroupEnable(answerRadioGroupPlayer1, true);
			resultImageViewPlayer1.setVisibility(View.INVISIBLE);
			resultTextViewPlayer1.setVisibility(View.INVISIBLE);
			timerPlayer1.start();
			totalProblemPassedPlayer1++;
			textViewExpressionCounterPlayer1.setText("double".equals(playerMode) ? firstPlayerName + " Zone      "  + getString(R.string.serial_number) + totalProblemPassedPlayer1 : "" + getString(R.string.serial_number) + totalProblemPassedPlayer1);
			radioButtonChangedPlayer1 = true;
		}
		else
		{
			if(timerTaskPlayer1)
				timerPlayer1.cancel();
			
			timerTaskPlayer1 = false;
			
			String resultTitle = getString(R.string.alert_message_title_single_player);
			
			int scorePlayer1 = ((totalCorrectAnswerPlayer1  * 100) / totalProblemPassedPlayer1);
			int scorePlayer2 = 0;
			
			String result = getString(R.string.alert_message_score
						, scorePlayer1 + "%"
						, totalProblemPassedPlayer1 + ""
						, (totalProblemPassedPlayer1-totalMissedPlayer1) + ""
						, totalCorrectAnswerPlayer1 + ""
						, totalWrongAnswerPlayer1 + "");
			/*
			System.out.println(firstPlayerName + "\ntotalProblemPassedPlayer1 : " + totalProblemPassedPlayer1 
					+ "\ntotalCorrectAnswerPlayer1 : " + totalCorrectAnswerPlayer1
					+ "\ntotalMissedPlayer1 : " + totalMissedPlayer1
					+ "\ntotalWrongAnswerPlayer1 : " + totalWrongAnswerPlayer1);
			*/
			if("double".equals(playerMode))
			{
				if(timerTaskPlayer2)
					timerPlayer2.cancel();
				
				timerTaskPlayer2 = false;
				
				scorePlayer2 = ((totalCorrectAnswerPlayer2  * 100) / totalProblemPassedPlayer2);
				
				result = firstPlayerName + "'s Score\n"
						+ result
						+ "\n\n" + secondPlayerName + "'s Score\n"
						+ getString(R.string.alert_message_score
								, scorePlayer2 + "%"
								, totalProblemPassedPlayer2 + ""
								, (totalProblemPassedPlayer2-totalMissedPlayer2) + ""
								, totalCorrectAnswerPlayer2 + ""
								, totalWrongAnswerPlayer2 + "");
				/*
				System.out.println(secondPlayerName + "\ntotalProblemPassedPlayer2 : " + totalProblemPassedPlayer2 
						+ "\ntotalCorrectAnswerPlayer2 : " + totalCorrectAnswerPlayer2
						+ "\ntotalMissedPlayer2 : " + totalMissedPlayer2
						+ "\ntotalWrongAnswerPlayer : " + totalWrongAnswerPlayer2);
				*/
				
				if(scorePlayer1 > scorePlayer2)
				{
					resultTitle = getString(R.string.alert_message_title_win, firstPlayerName);
				}
				else if(scorePlayer1 < scorePlayer2)
				{
					resultTitle = getString(R.string.alert_message_title_win, secondPlayerName);
				}
				else
				{
					resultTitle = getString(R.string.alert_message_title_tie);
				}
			}
			
			//String title = getString(R.string.alert_title_score, ((totalCorrectAnswerPlayer1  * 100) / totalProblemPassedPlayer1) + "%");
			
			AlertDialog.Builder finishAlertDialogBuilder = new AlertDialog.Builder(context);
	  
 			// set title
			finishAlertDialogBuilder.setTitle(resultTitle);
	  
 			// set dialog message
			finishAlertDialogBuilder
	 				.setMessage(result)
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
	
	public void nextProblemPlayer2()
	{	
		if(checkGameStatus())
		{
			setExpressionForSecondPlayer();
			
			radioButtonChangedPlayer2 = false;
			answerRadioGroupPlayer2.clearCheck();
			setAllChoices(answerRadioGroupPlayer2, answerPlayer2);
			setRadioGroupEnable(answerRadioGroupPlayer2, true);
			resultImageViewPlayer2.setVisibility(View.INVISIBLE);
			resultTextViewPlayer2.setVisibility(View.INVISIBLE);
			timerPlayer2.start();
			totalProblemPassedPlayer2++;
			textViewExpressionCounterPlayer2.setText(secondPlayerName + " Zone      " + getString(R.string.serial_number) + totalProblemPassedPlayer2);
			radioButtonChangedPlayer2 = true;
		}
		else
		{
			if(timerTaskPlayer1)
				timerPlayer1.cancel();
			
			timerTaskPlayer1 = false;
			
			if(timerTaskPlayer2)
				timerPlayer2.cancel();
			
			timerTaskPlayer2 = false;
			
			String resultTitle = getString(R.string.alert_message_title_single_player);
			int scorePlayer1 = ((totalCorrectAnswerPlayer1  * 100) / totalProblemPassedPlayer1);
			int scorePlayer2 = ((totalCorrectAnswerPlayer2  * 100) / totalProblemPassedPlayer2);
			
			if(scorePlayer1 > scorePlayer2)
			{
				resultTitle = getString(R.string.alert_message_title_win, firstPlayerName);
			}
			else if(scorePlayer1 < scorePlayer2)
			{
				resultTitle = getString(R.string.alert_message_title_win, secondPlayerName);
			}
			else
			{
				resultTitle = getString(R.string.alert_message_title_tie);
			}
			
			String result = getString(R.string.alert_message_score
						, scorePlayer1 + "%"
						, totalProblemPassedPlayer1 + ""
						, (totalProblemPassedPlayer1-totalMissedPlayer1) + ""
						, totalCorrectAnswerPlayer1 + ""
						, totalWrongAnswerPlayer1 + "");
			
			result = firstPlayerName + "'s Score\n"
					+ result
					+ "\n\n" + secondPlayerName + "'s Score\n"
					+ getString(R.string.alert_message_score
							, scorePlayer2 + "%"
							, totalProblemPassedPlayer2 + ""
							, (totalProblemPassedPlayer2-totalMissedPlayer2) + ""
							, totalCorrectAnswerPlayer2 + ""
							, totalWrongAnswerPlayer2 + "");
			/*
			System.out.println("totalProblemPassedPlayer1 : " + totalProblemPassedPlayer1 
					+ "\ntotalCorrectAnswerPlayer1 : " + totalCorrectAnswerPlayer1
					+ "\ntotalMissedPlayer1 : " + totalMissedPlayer1
					+ "\ntotalWrongAnswerPlayer1 : " + totalWrongAnswerPlayer1);
			
			System.out.println("totalProblemPassedPlayer2 : " + totalProblemPassedPlayer2 
					+ "\ntotalCorrectAnswerPlayer2 : " + totalCorrectAnswerPlayer2
					+ "\ntotalMissedPlayer2 : " + totalMissedPlayer2
					+ "\ntotalWrongAnswerPlayer2 : " + totalWrongAnswerPlayer2);
			*/
			AlertDialog.Builder finishAlertDialogBuilder = new AlertDialog.Builder(context);
	  
 			// set title
			finishAlertDialogBuilder.setTitle(resultTitle);
	  
 			// set dialog message
			finishAlertDialogBuilder
	 				.setMessage(result)
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
	
	public boolean checkGameStatus()
	{
		if("double".equals(playerMode))
		{
			return (totalProblemPassedPlayer1 < totalProblemAllowed 
					&& totalProblemPassedPlayer2 < totalProblemAllowed);
		}
		
		return totalProblemPassedPlayer1 < totalProblemAllowed;
	}
}
