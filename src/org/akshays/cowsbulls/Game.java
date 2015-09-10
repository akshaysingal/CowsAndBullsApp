package org.akshays.cowsbulls;

import org.akshays.cowsbulls.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends Activity{
	boolean toast = false;
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		
		if (MainActivity.GAME_STATE == "new") {
			AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
			builder.setMessage("A 4-digit secret number has been generated. Let's see how quickly can you guess this number. Good Luck! ;)");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//do nothing
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		
		mContext = this;
		//Start the timer
		MainActivity.newTime = (int)System.currentTimeMillis()/1000;
			
		//Generate secret number if playing a new game
		if (MainActivity.GAME_STATE == "new")
			MainActivity.secretNumber = generateSecretNumber();
		
		//Declare the textbox taking the input
//		final TextView textInput = (TextView) findViewById(R.id.gameInput1);
//		textInput.isFocusableInTouchMode();
		
		//Declare the textbox showing the entries
		final TextView log = (TextView) findViewById(R.id.gameTextLogs);
		log.setMovementMethod(new ScrollingMovementMethod());
		if (MainActivity.GAME_STATE == "continue")
			log.setText(MainActivity.textLogs);
		
		//Declare the keypad button views
		View []button = new View[12];
		button[0] = findViewById(R.id.button1);
		button[1] = findViewById(R.id.button2);
		button[2] = findViewById(R.id.button3);
		button[3] = findViewById(R.id.button4);
		button[4] = findViewById(R.id.button5);
		button[5] = findViewById(R.id.button6);
		button[6] = findViewById(R.id.button7);
		button[7] = findViewById(R.id.button8);
		button[8] = findViewById(R.id.button9);
		button[9] = findViewById(R.id.button0);
		button[10] = findViewById(R.id.buttonDel);
		button[11] = findViewById(R.id.buttonEnter);
		
		//Declare and define the 'Pass' button
		Button giveUp = (Button) findViewById(R.id.passButton);
		giveUp.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
				builder.setMessage("You lost!\n\n" + "The secret number is: " + MainActivity.secretNumber);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						resetGame("");
						
						Intent newGame = new Intent(Game.this, MainActivity.class);
						startActivity(newGame);
					}
				});
				
				AlertDialog dialog = builder.create();
				dialog.show();
			}
			
		});
		
		
		for (int i=0; i<12; i++){
			button[i].setOnClickListener(new OnClickListener(){
				
				@Override
				public void onClick(View arg) {
					Log.d("Game()", "button " + arg.getId() + " clicked");
					String num = "";
					
					switch (arg.getId()) {
					case R.id.button0:
						num = "0";
						break;
					case R.id.button1:
						num = "1";
						break;
					case R.id.button2:
						num = "2";
						break;
					case R.id.button3:
						num = "3";
						break;
					case R.id.button4:
						num = "4";
						break;
					case R.id.button5:
						num = "5";
						break;
					case R.id.button6:
						num = "6";
						break;
					case R.id.button7:
						num = "7";
						break;
					case R.id.button8:
						num = "8";
						break;
					case R.id.button9:
						num = "9";
						break;
					case R.id.buttonDel:
						MainActivity.text = (MainActivity.text != "") ? MainActivity.text.substring(0, (MainActivity.text.length() - 1)) : "";
//						switch(MainActivity.text.length()) {
//							case 0:
//								((TextView) findViewById(R.id.gameInput1)).setText("");
//								break;
//							case 1:
//								((TextView) findViewById(R.id.gameInput2)).setText("");
//								break;
//							case 2:
//								((TextView) findViewById(R.id.gameInput3)).setText("");
//								break;
//							case 3:
//								((TextView) findViewById(R.id.gameInput4)).setText("");
//								break;
//						}
						break;
					case R.id.buttonEnter:
						if (MainActivity.text.length() != 4) {
							Toast toast = Toast.makeText(getBaseContext(), "Enter 4 digits", 2);
							toast.setGravity(Gravity.BOTTOM, 0, 0);
							toast.show();
							break;
						}
						MainActivity.newTrials++;
						//Evaluate the entry
						int[] cowsBulls = evaluateInput(MainActivity.text);
						if (cowsBulls[1] != 4) {
							MainActivity.textLogs += MainActivity.text + " - " + cowsBulls[0] + (cowsBulls[0]==1 ? " cow, " : " cows, ") + cowsBulls[1] + (cowsBulls[1]==1 ? " bull" : " bulls");
							MainActivity.textLogs += "\n";
							MainActivity.text="";
							clearInput();
						}
						break;
					}
					
					if ((MainActivity.text).length() == 4 && num != "") {
						Toast toast = Toast.makeText(getBaseContext(), "Limit Reached", 2);
						toast.setGravity(Gravity.BOTTOM, 0, 0);
						toast.show();
					}
					else if (num != "" && MainActivity.text.contains(num)){
						Toast toast = Toast.makeText(getBaseContext(), "Cannot repeat digits", 2);
						toast.setGravity(Gravity.BOTTOM, 0, 0);
						toast.show();
					}
					else {
						MainActivity.text = MainActivity.text + num;
						
						if(num != "") {
							switch(MainActivity.text.length()) {
								case 1:
									((TextView) findViewById(R.id.gameInput1)).setText(num);
									break;
								case 2:
									((TextView) findViewById(R.id.gameInput2)).setText(num);
									break;
								case 3:
									((TextView) findViewById(R.id.gameInput3)).setText(num);
									break;
								case 4:
									((TextView) findViewById(R.id.gameInput4)).setText(num);
									break;
							}
						} else {
							switch(MainActivity.text.length()) {
							case 0:
								((TextView) findViewById(R.id.gameInput1)).setText("");
								break;
							case 1:
								((TextView) findViewById(R.id.gameInput2)).setText("");
								break;
							case 2:
								((TextView) findViewById(R.id.gameInput3)).setText("");
								break;
							case 3:
								((TextView) findViewById(R.id.gameInput4)).setText("");
								break;
							}
						}
					}
					
					log.setText(MainActivity.textLogs);
				}
			});	
		}
	}
	
	public void clearInput() {
		((TextView) findViewById(R.id.gameInput1)).setText("");
		((TextView) findViewById(R.id.gameInput2)).setText("");
		((TextView) findViewById(R.id.gameInput3)).setText("");
		((TextView) findViewById(R.id.gameInput4)).setText("");
	}
	
	public int[] evaluateInput(String num) {
		int bulls = 0, cows = 0;
		int[] ret = new int[2];
		
		for (int i=0; i<4; i++) {
			if (MainActivity.secretNumber.charAt(i) == num.charAt(i)) {
				bulls++;
			} else if (MainActivity.secretNumber.contains(num.charAt(i) + "")) {
				cows++;
			}
		}
		if (bulls == 4) {
			System.out.println((int)System.currentTimeMillis()/1000 - MainActivity.newTime);
			System.out.println(getSharedPreferences(MainActivity.PREFS_NAME, 0).getInt("newTime", 0));
			MainActivity.newTime = (int)System.currentTimeMillis()/1000 - MainActivity.newTime;
			if (MainActivity.GAME_STATE == "continue") {
				MainActivity.newTime += getSharedPreferences(MainActivity.PREFS_NAME, 0).getInt("newTime", 0);
			}
			playerWon();
		}
		
		ret[0] = cows;
		ret[1] = bulls;
		
		return ret;
	}
	
	private void playerWon() {
		//Create custom dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.winner_anim, null);
		
		//set values for custom dialog components - text and button
		TextView winText = (TextView) layout.findViewById(R.id.winText);
		winText.setText("Congrats! You won!");
		TextView secretNumText = (TextView) layout.findViewById(R.id.secretNumText);
		secretNumText.setText("The secret number is: " + MainActivity.secretNumber);
		TextView statText = (TextView) layout.findViewById(R.id.stats);
		String timePlayed = "";
		System.out.println(MainActivity.newTime);
		
		if (MainActivity.newTime < 60) {
			String seconds = ((MainActivity.newTime < 10) ? "0"+MainActivity.newTime : ""+MainActivity.newTime);
			timePlayed = "00:00:" + seconds;
		} else if(MainActivity.newTime/60 < 60) {
			String seconds = ((MainActivity.newTime%60 < 10) ? "0"+MainActivity.newTime%60 : ""+MainActivity.newTime%60);
			String minutes = ((MainActivity.newTime/60 < 10) ? "0"+MainActivity.newTime/60 : ""+MainActivity.newTime/60);
			timePlayed = "00:" + minutes + ":" + seconds;
		} else {
			String seconds = (((MainActivity.newTime%3600)/60 < 10) ? "0"+(MainActivity.newTime%3600)/60 : ""+(MainActivity.newTime%3600)/60);
			String minutes = ((MainActivity.newTime%3600 < 10) ? "0"+MainActivity.newTime%3600 : ""+MainActivity.newTime%3600);
			String hours = ((MainActivity.newTime/3600 < 10) ? "0"+MainActivity.newTime/3600 : ""+MainActivity.newTime/3600);
			timePlayed = hours + ":" + minutes + ":" + seconds;
		}
		statText.setText("Number of guesses: " + MainActivity.newTrials + "\n" + "Time played: " + timePlayed);

		// Save the time
		MainActivity.highScores = MainActivity.newTrials + " trials / " + timePlayed + "\n" + MainActivity.highScores;
		String scores = MainActivity.highScores;
		builder.setView(layout);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				resetGame("gameWon");
				Intent i = new Intent(Game.this, MainActivity.class);
				startActivity(i);
			}
		});
		AlertDialog alertDialog = builder.show();
	}

	public String generateSecretNumber() {
		String fourDigits="";
		boolean valid = false;
		int digit=0;
		
		while(fourDigits.length() != 4) {
			while (!valid){
				digit = (int)(Math.random()*10);
				valid = (digit == 10 || fourDigits.contains(Integer.toString(digit))) ? false : true;
			}
			fourDigits += Integer.toString(digit);
			valid = false;
		}
		
		return fourDigits;
	}
	
	public void resetGame(String gameState) {
		//Reset the variables
		/** These variables don't change across games, so don't modify them here
		MainActivity.highScores = "" */
		MainActivity.secretNumber = "";
		MainActivity.text = "";
		MainActivity.textLogs = "";
		MainActivity.newTrials = 0;
		MainActivity.newTime = 0;
		MainActivity.GAME_STATE = gameState;		//make Continue button inactive
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		if(MainActivity.GAME_STATE != "gameWon") {	// has been paused by user pressing back button
			//enable the continue button on home page
			if (MainActivity.GAME_STATE == "new" || MainActivity.GAME_STATE == "continue") {
				MainActivity.continueButton.setVisibility(0);
			}
			MainActivity.newTime = getSharedPreferences(MainActivity.PREFS_NAME, 0).getInt("newTime", 0) + ((int)System.currentTimeMillis()/1000 - MainActivity.newTime);
		} else if (MainActivity.GAME_STATE == "gameWon") {	// Going back to previous activity
			MainActivity.newTime = 0;
			resetGame("gameWon");
		}
		
		SharedPreferences.Editor editor = getSharedPreferences(MainActivity.PREFS_NAME, 0).edit();
		editor.putInt("newTime", MainActivity.newTime);
		editor.putString("highScores", MainActivity.highScores);
		editor.commit();
		
		super.onBackPressed();
	}
	
}
