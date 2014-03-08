package com.placeregister.dialog;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.google.android.gms.maps.model.Marker;
import com.placeregister.R;
import com.placeregister.asynchtask.GetTimeService;
import com.placeregister.places.Place;
import com.placeregister.search.parameters.PlaceMarkerParam;

/**
 * User must select a type for the place he wants to register (a place can have
 * several types, only one is retained for his visit)
 * 
 * @author yoann
 * 
 */
public class SelectPlaceTypeDialog extends DialogFragment {

	/**
	 * Calling activity
	 */
	private Activity activity;

	/**
	 * Place types
	 */
	private List<String> placeTypes;

	/**
	 * Selected place marker
	 */
	private Marker marker;

	/**
	 * Mapping google marker <-> Place object
	 */
	private Map<Marker, Place> placeMap;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		builder.setTitle(R.string.dialog_title_type);

		builder.setSingleChoiceItems(
				placeTypes.toArray(new CharSequence[placeTypes.size()]), 0,
				null);

		builder.setPositiveButton(R.string.OK,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						int selectedPosition = ((AlertDialog) dialog)
								.getListView().getCheckedItemPosition();

						// Sends a request to the server to register the
						// visit
						PlaceMarkerParam markerParam = new PlaceMarkerParam(
								marker, placeMap.get(marker), placeTypes
										.get(selectedPosition), null, activity);
						new GetTimeService().execute(markerParam);
					}
				});
		builder.setNegativeButton(R.string.KO, null);

		return builder.create();
	}

	public void setPlaceTypes(List<String> placeTypes) {
		this.placeTypes = placeTypes;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public void setPlaceMap(Map<Marker, Place> placeMap) {
		this.placeMap = placeMap;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
