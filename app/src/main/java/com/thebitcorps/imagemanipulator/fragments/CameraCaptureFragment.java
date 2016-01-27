package com.thebitcorps.imagemanipulator.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.thebitcorps.imagemanipulator.R;
import com.thebitcorps.imagemanipulator.helpers.CamaraPreview;


public class CameraCaptureFragment extends Fragment{
	private CamaraPreview cameraPreview;
	private Camera camera;
	private static final String TAG = "shit";
	FrameLayout  frameLayout;
	public static CameraCaptureFragment newInstance(){
		CameraCaptureFragment cameraCaptureFragment = new CameraCaptureFragment();
		return  cameraCaptureFragment;
	}
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_camera_capture,container,false);
		FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.capture);
		frameLayout = (FrameLayout) view.findViewById(R.id.camera);
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

	/**
	 *
	 * @return return the instane of the camera selected of null if can't open any
	 */
	public static Camera getCameraInstance(){
		Camera c = null;
		try{
			c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
		}
		catch (Exception e){
			e.printStackTrace();
			Log.e(TAG,e.getMessage());
		}
		return c;
	}

	@Override
	public void onResume() {
		super.onResume();
		camera = getCameraInstance();
		if(camera != null){
//			create a new camera preview and add it to the framelayot
//			// TODO: 1/27/16 Change so we dont instance a new camera preview so only we change the camera
			cameraPreview = new CamaraPreview(this.getActivity(),camera);
			frameLayout.addView(cameraPreview);
		}
		else{
			Snackbar.make(frameLayout,"Error opening camera",Snackbar.LENGTH_LONG).setAction("~Action", null).show();
			Toast.makeText(getActivity(),"Error opening camera", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onPause() {

		super.onPause();
		if(camera != null){
//			we stop the camera so other applications can use it
			camera.stopPreview();
			camera.setPreviewCallback(null);
			cameraPreview.getHolder().removeCallback(cameraPreview);
			camera.release();
			camera = null;
		}

	}
}
