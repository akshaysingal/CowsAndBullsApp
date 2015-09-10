package org.akshays.cowsbulls;

import java.io.ObjectOutputStream.PutField;

import org.akshays.cowsbulls.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	public static String highScores;
	public static String secretNumber;
	public static String text;
	public static String textLogs;
	public static int newTrials;
	public static int newTime;
	public static AlertDialog.Builder builder;
	public static AlertDialog alertDialog = null;
	public static LayoutInflater inflater = null;
	
	public static final String PREFS_NAME = "CowsBullsPrefs";
	public static String GAME_STATE = "";		//"new" for new game, "continue" for continued game, "reset" implies grey out 'Continue' button.
	
	public static Button continueButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.play_game).setOnClickListener(this);
		findViewById(R.id.instructions).setOnClickListener(this);
		findViewById(R.id.high_scores).setOnClickListener(this);
		continueButton = (Button) findViewById(R.id.continue_game);
		continueButton.setOnClickListener(this);
		
		//Handle the Continue Button - find the previous state of the game only if GAME is newly loaded
		//This is partially determined by checking if the secret number has been set. This is due to 
		//the behaviour of the application saving a state (in GAME_STATE) but not having a secret number.
		if (GAME_STATE == "") {
			if (getSharedPreferences(PREFS_NAME, 0).getString("secretNumber", "") != "")
				GAME_STATE = getSharedPreferences(PREFS_NAME, 0).getString("GAME_STATE", "");
		}
		
		if (GAME_STATE == "reset" || GAME_STATE == "" || GAME_STATE == "gameWon") {
			continueButton.setVisibility(8);
		} else {
			continueButton.setVisibility(0);
		}
		
		// Load the saved scores
//		highScores = getSharedPreferences(PREFS_NAME, 0).getString("highScores", "");
	}
	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	} */

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id){
		case R.id.continue_game:
			//Initialize the variables
			if (secretNumber == null) {
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				highScores = settings.getString("highScores", "");
				secretNumber = settings.getString("secretNumber", "");
				text = settings.getString("text", "");
				textLogs = settings.getString("textLogs", "");
			}
			
			newTime = 0;
			
			//Set the GAME_STATE flag
			GAME_STATE = "continue";
			
			//Start the game			
			Intent continueGame = new Intent(MainActivity.this, Game.class);
			startActivity(continueGame);
			break;
		case R.id.play_game:
			//Initialize the variables
			highScores = (highScores != null) ? highScores : "";
			secretNumber = "";
			text = "";
			textLogs = "";
			newTime = 0;
			
			SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, 0).edit();
			editor.putInt("newTime", 0);
			editor.commit();
			
			//Set the GAME_STATE flag
			GAME_STATE = "new";
			
			//Start the game
			Intent newGame = new Intent(MainActivity.this, Game.class);
			startActivity(newGame);
			break;
		case R.id.instructions:
			//String text = "The player tries to guess the computer generated secret number. After each guess, the computer returns the number of matches. If the matching digits are on their right positions, they are \"bulls\", if on different positions, they are \"cows\". \n\nExample: Secret number: 4271 \n\n" +
			//		"Opponent's try: 1234\nAnswer: 2 cows and 1 bull. (The cows are \"4\" and \"1\", the bull is \"2\").";
			//Create custom dialog
			builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
			
			inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.instructions, null);
			
			builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					View layout = inflater.inflate(R.layout.instruction_page2, null);
					builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							View layout = inflater.inflate(R.layout.instruction_page3, null);
							builder.setPositiveButton("StartPlaying!", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									alertDialog.dismiss();
								}
							});
							alertDialog.dismiss();
							builder.setView(layout);
							alertDialog = builder.show();
						}
					});
					alertDialog.dismiss();
					builder.setView(layout);
					alertDialog = builder.show();
				}

			});
			
			builder.setView(layout);
			alertDialog = builder.show();
			break;
		case R.id.high_scores:
			Intent scoresIntent = new Intent(MainActivity.this, HighScores.class);
			startActivity(scoresIntent);
			break;
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		//Save settings
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("secretNumber", secretNumber);
		editor.putString("text", text);
		editor.putString("textLogs", textLogs);
		//editor.putInt("newTime", newTime); // Already handled in Game.java onPause() method
		editor.putString("GAME_STATE", GAME_STATE);
		
		// Commit the edits
		editor.commit();
		
		super.finish();
	}
	
//	@Override
//	public void onStop() {
//		super.onStop();
//		
//		//Save settings
//		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//		SharedPreferences.Editor editor = settings.edit();
//		editor.putInt("highScoreTrials", highScoreTrials);
//		editor.putInt("previousBestTime", previousBestTime);
//		editor.putString("highScoreTime", highScoreTime);
//		
//		// Commit the edits
//		editor.commit();
//	}
	
}
