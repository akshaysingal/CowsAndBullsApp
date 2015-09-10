package org.akshays.cowsbulls;

import org.akshays.cowsbulls.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class HighScores extends Activity {
	
	@Override
	public void onCreate(Bundle savedBundleInstance){
		super.onCreate(savedBundleInstance);
		setContentView(R.layout.high_scores);
		
		TextView title = (TextView) findViewById(R.id.hs_title);
		TextView score = (TextView) findViewById(R.id.hs_score);
		String score1 = MainActivity.highScores;
		title.setText("Scores");	//Scores to beat
		
		score.setText((MainActivity.highScores == "" || MainActivity.highScores == null) ? "0 trials / 00:00:00" : MainActivity.highScores);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		//finish();
	}
}
