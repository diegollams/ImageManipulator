package com.thebitcorps.imagemanipulator.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
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
	private int cameraId;
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
		FloatingActionButton changeCamera = (FloatingActionButton) view.findViewById(R.id.change_camera);
		cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
		if(Camera.getNumberOfCameras() > 1){
			changeCamera.setOnClickListener(changeCameraOnClickListener);
		}else{
			changeCamera.setVisibility(View.INVISIBLE);
		}
		frameLayout = (FrameLayout) view.findViewById(R.id.camera);
		button.setOnClickListener(captureListener);
		return view;
	}

	/**
	 *
	 * @return return the instane of the camera selected of null if can't open any
	 */
	public static Camera getCameraInstance(int cameraId ){
		Camera c = null;
		try{
//			cameraId = cameraId > 2 ? 0 : cameraId;
			c = Camera.open(cameraId);
			c.setDisplayOrientation(90);
		}
		catch (Exception e){
			e.printStackTrace();
			Log.e(TAG,"camera instance: " + e.getMessage());
		}
		return c;
	}

	@Override
	public void onResume() {
		super.onResume();
		camera = getCameraInstance(cameraId);
		if(camera != null){
//			create a new camera preview and add it to the framelayot
//			// TODO: 1/27/16 Change so we dont instance a new camera preview so only we change the camera
			if(cameraPreview == null){
				cameraPreview = new CamaraPreview(getActivity(),camera);
				frameLayout.addView(cameraPreview);
			}else {
				cameraPreview.setCamera(camera);
			}
		}
		else{
			Snackbar.make(frameLayout,"Error opening camera",Snackbar.LENGTH_LONG).setAction("~Action", null).show();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		stopCamera();

	}

	private void stopCamera() {
		if(camera != null){
//			we stop the camera so other applications can use it
			camera.stopPreview();
			camera.setPreviewCallback(null);
			cameraPreview.getHolder().removeCallback(cameraPreview);
			camera.release();
			camera = null;
		}
	}
	private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

		}
	};
	private View.OnClickListener captureListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.fragment, ShowImageFragment.newInstance());
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
	};
	private FloatingActionButton.OnClickListener changeCameraOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			stopCamera();
			cameraId = cameraId == Camera.CameraInfo.CAMERA_FACING_BACK ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
			camera = getCameraInstance(cameraId);
			if(camera != null) {
				cameraPreview.setCamera(camera);
			}else{
				Snackbar.make(frameLayout,"Error opening camera",Snackbar.LENGTH_LONG).setAction("~Action", null).show();
			}
		}
	};

}
