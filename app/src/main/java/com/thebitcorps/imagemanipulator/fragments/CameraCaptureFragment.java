package com.thebitcorps.imagemanipulator.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.thebitcorps.imagemanipulator.R;


public class CameraCaptureFragment extends Fragment {

	public static CameraCaptureFragment newInstance(){
		CameraCaptureFragment cameraCaptureFragment = new CameraCaptureFragment();
		return  cameraCaptureFragment;
	}
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_camera_capture,container,false);
		Button button = (Button) view.findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.fragment, ShowImageFragment.newInstance());
				fragmentTransaction.commit();
			}
		});
		return view;
	}
}
