package com.thebitcorps.imagemanipulator.helpers;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by diegollams on 1/26/16.
 */
public class CamaraPreview extends SurfaceView implements SurfaceHolder.Callback{
	private SurfaceHolder holder;
	private Camera camera;
	private static final String TAG = "shit";

	public CamaraPreview(Context context,Camera camera) {
		super(context);
		this.camera = camera;
		this.holder = getHolder();
		this.holder.addCallback(this);
		this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		starPreviewForCamera(holder);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if(holder.getSurface() == null){
			return;
		}
		stopPreviewForCamera();
		starPreviewForCamera(holder);
	}

	private void starPreviewForCamera(SurfaceHolder holder) {
		try{
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		}catch (Exception e){
			e.printStackTrace();
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}


	public void setCamera(Camera camera) {
		stopPreviewForCamera();
		this.camera = camera;
		starPreviewForCamera(getHolder());
	}

	private void stopPreviewForCamera(){
		try{
			camera.stopPreview();
		}catch (Exception e){
			e.printStackTrace();
			Log.d(TAG, "Error stoping preview: " + e.getMessage());
		}
	}
}
