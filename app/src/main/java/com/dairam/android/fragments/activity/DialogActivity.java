package com.dairam.android.fragments.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.dairam.android.fragments.R;

public class DialogActivity extends Activity{
	
	TextView Msg;
	@Override
		protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.floatingactivity);
		
		Msg = (TextView)findViewById(R.id.textViewMsgCC);
		
		Bundle bundle = getIntent().getExtras();
		String message = bundle.getString("msg");
		
		Msg.setText(message);
		
		}
	
}
