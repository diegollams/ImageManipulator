package com.thebitcorps.imagemanipulator.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.thebitcorps.imagemanipulator.R;
import com.thebitcorps.imagemanipulator.fragments.CameraCaptureFragment;
// TODO: 2/6/16 on orientation change savedInstanceState for the fragment running
public class MainActivity extends AppCompatActivity {
	Toolbar toolbar;
	private static int OPEN_IMAGE_REQUEST_CODE = 100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment);
		android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.add(frameLayout.getId(), CameraCaptureFragment.newInstance());
		fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		if(id == R.id.action_choose_image){
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent,OPEN_IMAGE_REQUEST_CODE);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == OPEN_IMAGE_REQUEST_CODE){
			if(resultCode == Activity.RESULT_OK){
				Uri imageUri = getImagePath(data.getData());
				CameraCaptureFragment.changeToShowImageFragment(imageUri,getFragmentManager());
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * helper to retrieve the path of an image URI
	 */
	public Uri getImagePath(Uri uri) {
		// just some safety built in
		if( uri == null ) {
			// TODO perform some logging or show user feedback
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if( cursor != null ){
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return Uri.parse(cursor.getString(column_index));
		}
		// this is our fallback here
		return uri;
	}

}
