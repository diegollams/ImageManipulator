package com.thebitcorps.imagemanipulator.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.thebitcorps.imagemanipulator.R;
import com.thebitcorps.imagemanipulator.helpers.BitmapTrasformer;
import com.thebitcorps.imagemanipulator.helpers.CamaraPreview;
import com.thebitcorps.imagemanipulator.helpers.UriCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

// TODO: 2/6/16 fix bug in in oorientation change camera
public class CameraCaptureFragment extends Fragment{
	private CamaraPreview cameraPreview;
	private Camera camera;
	private int cameraId;
	private Uri imageUri;
	private static int IMAGE_WIDTH_DEFAULT = 500;
	private static int IMAGE_HEIGTH_DEFAULT = 500;

	private static final String TAG = "shit";

	public static final String DEFAULT_IMAGE_NAME = "sampleImage.jpg";
	FrameLayout  frameLayout;
	public static CameraCaptureFragment newInstance(){
		CameraCaptureFragment cameraCaptureFragment = new CameraCaptureFragment();
		return  cameraCaptureFragment;
	}
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_camera_capture,container,false);
//		capture camra button
		FloatingActionButton captureButton = (FloatingActionButton) view.findViewById(R.id.capture);
		captureButton.setOnClickListener(captureListener);
//		change camera button
		FloatingActionButton changeCamera = (FloatingActionButton) view.findViewById(R.id.change_camera);
//		if device not support various cameras hide button
		if(Camera.getNumberOfCameras() > 1){
			changeCamera.setOnClickListener(changeCameraOnClickListener);
		}else{
			changeCamera.setVisibility(View.INVISIBLE);
		}
//		default camera
		cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

		frameLayout = (FrameLayout) view.findViewById(R.id.camera);

		return view;
	}


	/**
	 *
	 * @return return the instance of the camera selected of null if can't open any
	 */
	public static Camera getCameraInstance(int cameraId,Activity activity ){
		Camera camera = null;
		try{
// TODO: 2/6/16 Add camera features 
//			open the camra
			camera = Camera.open(cameraId);
			Camera.CameraInfo  info = new Camera.CameraInfo();
			Camera.getCameraInfo(cameraId,info);

//			get the display rotation
			int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
			int degrees = 0;
//			adjust image depending in rotation of the sceeen
			switch (rotation) {
				case Surface.ROTATION_0: degrees = 0; break;
				case Surface.ROTATION_90: degrees = 90; break;
				case Surface.ROTATION_180: degrees = 180; break;
				case Surface.ROTATION_270: degrees = 270; break;
			}
			int result;
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				result = (info.orientation + degrees) % 360;
				result = (360 - result) % 360;  // compensate the mirror
			} else {  // back-facing
				result = (info.orientation - degrees + 360) % 360;
			}
			camera.setDisplayOrientation(result);
		}
		catch (Exception e){
			e.printStackTrace();
			Log.e(TAG,"camera instance: " + e.getMessage());
		}
		return camera;
	}

	@Override
	public void onResume() {
		super.onResume();
		camera = getCameraInstance(cameraId,getActivity());
		if(camera != null){
//			create a new camera preview and add it to the framelayot
			// TODO: 2/2/16 fix bug on resume no opening preview 
//			// TODO: 1/27/16 Change so we dont instance a new camera preview so only we change the camera
			if(cameraPreview == null){
				cameraPreview = new CamaraPreview(getActivity(),camera);
				frameLayout.addView(cameraPreview);
			}else {
				cameraPreview.setCamera(camera);
			}
		}
		else{
			Snackbar.make(frameLayout,R.string.camera_error,Snackbar.LENGTH_LONG).setAction("~Action", null).show();
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


	public static void changeToShowImageFragment(Uri imageUri,FragmentManager fragmentManager) {
		ShowImageFragment imageFragment = ShowImageFragment.newInstance();
		Bundle extras = new Bundle();
		extras.putParcelable(ShowImageFragment.IMAGE_URI_EXTRA, imageUri);
		imageFragment.setArguments(extras);
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment,imageFragment);
//		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}


	private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			File pictureFile = UriCreator.getOutputMediaFile(UriCreator.MEDIA_TYPE_IMAGE,getActivity().getPackageName());
			if(pictureFile == null){
				Log.d(TAG, "Error creating media file, check storage permissions: ");
				return;
			}

			Bitmap bitmap = BitmapTrasformer.decodeSampledBitmapFromData(data,IMAGE_WIDTH_DEFAULT,IMAGE_HEIGTH_DEFAULT);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(pictureFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);

//				fos.write(bitmap,to);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			imageUri = Uri.fromFile(pictureFile);
			stopCamera();

			changeToShowImageFragment(imageUri,getFragmentManager());
		}
	};
	private View.OnClickListener captureListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			camera.takePicture(null, null, pictureCallback);
		}


	};
	private FloatingActionButton.OnClickListener changeCameraOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			stopCamera();
			cameraId = cameraId == Camera.CameraInfo.CAMERA_FACING_BACK ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
			camera = getCameraInstance(cameraId,getActivity());
			if(camera != null) {
				cameraPreview.setCamera(camera);
			}else{
				Snackbar.make(frameLayout,R.string.camera_error,Snackbar.LENGTH_LONG).setAction("~Action", null).show();
			}
		}
	};


}
