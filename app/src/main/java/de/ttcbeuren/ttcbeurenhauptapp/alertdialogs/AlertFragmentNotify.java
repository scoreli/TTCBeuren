package de.ttcbeuren.ttcbeurenhauptapp.alertdialogs;

import de.ttcbeuren.ttcbeurenhauptapp.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AlertFragmentNotify extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(
				"Funktion benötigt eine bestehende Internetverbinung")
				.setTitle("Warnung !").setIcon(R.drawable.ic_launcher)

				.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
