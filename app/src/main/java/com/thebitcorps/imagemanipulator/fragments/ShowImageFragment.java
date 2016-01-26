package com.thebitcorps.imagemanipulator.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thebitcorps.imagemanipulator.R;


public class ShowImageFragment extends android.app.Fragment {
	public static ShowImageFragment newInstance(){
		ShowImageFragment fragment =  new ShowImageFragment();
		return fragment;
	}
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_show_image,container,false);
	}
}
