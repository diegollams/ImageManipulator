package com.thebitcorps.imagemanipulator.fragments;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
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
import android.widget.Toast;

import com.thebitcorps.imagemanipulator.R;


public class ShowImageFragment extends android.app.Fragment  implements  FilterDialogChoice.FilterDialogListener{
	public static final String IMAGE_URI_EXTRA = "imageExtra";
	private CoordinatorLayout coordinatorLayout;
	private Uri imageUri;
	public static final String TAG = "showImageActivity";
	public static ShowImageFragment newInstance(){
		ShowImageFragment fragment =  new ShowImageFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		android.support.v7.widget.Toolbar tolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
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
		FilterDialogChoice dialogChoice = new FilterDialogChoice();

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
		Snackbar.make(coordinatorLayout, imageUri.toString(), Snackbar.LENGTH_LONG).show();
		ImageView imageView = (ImageView) view.findViewById(R.id.image);
		imageView.setImageURI(imageUri);
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
		Toast.makeText(getActivity(), filter, Toast.LENGTH_SHORT).show();
	}
}
