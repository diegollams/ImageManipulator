package com.thebitcorps.imagemanipulator.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thebitcorps.imagemanipulator.R;
import com.thebitcorps.imagemanipulator.helpers.BitmapTrasformer;

import java.io.File;


public class ShowImageFragment extends android.app.Fragment  implements  FilterDialogChoice.FilterDialogListener{
	public static final String IMAGE_URI_EXTRA = "imageExtra";
	private CoordinatorLayout coordinatorLayout;
	private Uri imageUri;
	private Bitmap imageBitmap;
	private ImageView imageView;
	private android.support.v7.widget.Toolbar tolbar;
	private static int IMAGE_WIDTH_DEFAULT = 100;
	private static int IMAGE_HEIGTH_DEFAULT = 100;
	public static final String TAG = "showImageActivity";
	public static ShowImageFragment newInstance(){
		ShowImageFragment fragment =  new ShowImageFragment();
		return fragment;
	}


	public Activity getActivity(Fragment fragment) {
		if (fragment == null) {
			return null;
		}
		while (fragment.getParentFragment() != null) {
			fragment = fragment.getParentFragment();
		}
		return fragment.getActivity();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
		if (tolbar == null) return;
		tolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
		tolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.fragment, CameraCaptureFragment.newInstance());
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		});
		tolbar.setTitle(R.string.show_image_title);

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		Bundle extras = getArguments();
		imageUri = extras.getParcelable(IMAGE_URI_EXTRA);

		View view = inflater.inflate(R.layout.fragment_show_image, container, false);
		coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.cordinator);

		FloatingActionButton filterButton = (FloatingActionButton) view.findViewById(R.id.filter_button);
		filterButton.setOnClickListener(filteButtonListener);

//		if the uri was not included show error textView
		if(imageUri == null){
			TextView textView = new TextView(getActivity());
			textView.setText(R.string.image_error);
			textView.setTextSize(30);
			textView.setLayoutParams(new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			container.addView(textView);
			Log.e(TAG,"null uri in extras");
			return view;
		}
		File file = new File(imageUri.getPath());
		Snackbar.make(coordinatorLayout, file.getAbsolutePath(), Snackbar.LENGTH_LONG).show();

		imageBitmap = BitmapTrasformer.getBitmapFromUri(imageUri, IMAGE_WIDTH_DEFAULT,IMAGE_HEIGTH_DEFAULT);

		imageView = (ImageView) view.findViewById(R.id.image);

		imageView.setImageBitmap(imageBitmap);
		return view;
	}

	private void showFilersDialog(){
		FilterDialogChoice dialogChoice = new FilterDialogChoice();
		dialogChoice.setTargetFragment(this,0);
		dialogChoice.show(getFragmentManager(), null);
	}

	private View.OnClickListener filteButtonListener =  new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		showFilersDialog();
		}
	};

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String filter) {
		if(filter.equals( getString(R.string.gray_scale))){
			BitmapTrasformer.grayScaleBitmap(imageBitmap);
			imageView.setImageBitmap(imageBitmap);
		}
		else if(filter.equals(getString(R.string.inverse_max_canal))){
			BitmapTrasformer.inverseMaxCanalImageBitmap(imageBitmap);
			imageView.setImageBitmap(imageBitmap);
		}
		else if(filter.equals(getString(R.string.inverse_normal))){
			BitmapTrasformer.inverseNormal(imageBitmap);
			imageView.setImageBitmap(imageBitmap);
		}
		else{

		}

	}
}
