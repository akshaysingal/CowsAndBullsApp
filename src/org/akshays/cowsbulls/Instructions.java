package org.akshays.cowsbulls;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Instructions extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}
}
