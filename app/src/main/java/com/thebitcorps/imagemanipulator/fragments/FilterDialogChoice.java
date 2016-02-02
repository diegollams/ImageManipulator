package com.thebitcorps.imagemanipulator.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.thebitcorps.imagemanipulator.R;
// TODO: 2/2/16 Add string array in constructor for reusability 
/**
 * Created by diegollams on 2/2/16.
 */
public class FilterDialogChoice extends android.app.DialogFragment {



	private int selected = 0;

	public interface FilterDialogListener{
		public void onDialogPositiveClick(DialogFragment dialog,String filter);

	}

	FilterDialogListener listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			listener = (FilterDialogListener) getTargetFragment();
		}
		catch (ClassCastException e){
			throw new ClassCastException(" must implement ClickListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final String[] options = getResources().getStringArray(R.array.filters);
		AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.filter_title_choice).setSingleChoiceItems(R.array.filters, selected, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selected = which;
			}
		})
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onDialogPositiveClick(FilterDialogChoice.this,options[selected]);
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}
		});

		return builder.create();
	}

}
