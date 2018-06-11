package com.dairam.android.fragments.activity.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dairam.android.fragments.R;

public class NoInternetFragment extends Fragment {
	 public View onCreateView(LayoutInflater inflater,
		      ViewGroup container, Bundle savedInstanceState) {
      
			   View v = inflater.inflate(R.layout.nointernet, container, false);
		return v;
	 }
}
