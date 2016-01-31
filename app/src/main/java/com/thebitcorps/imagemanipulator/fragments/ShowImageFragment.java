package com.thebitcorps.imagemanipulator.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.thebitcorps.imagemanipulator.R;


public class ShowImageFragment extends android.app.Fragment {
	public static ShowImageFragment newInstance(){
		ShowImageFragment fragment =  new ShowImageFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle extras = getArguments();
		Bitmap image = extras.getParcelable(CameraCaptureFragment.IMAGE_EXTRA);
		View view = inflater.inflate(R.layout.fragment_show_image, container, false);
		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		imageView.setImageBitmap(image);
		return view;
	}
}
