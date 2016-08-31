package de.ttcbeuren.ttcbeurenhauptapp.alertdialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import de.ttcbeuren.ttcbeurenhauptapp.R;

public class AlertFragmentPWForgot extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(
				"Falls ihre Email zu einem Konto gehört, wird Ihnen auf diese ein neues Passwort zugeschickt. Bitte schauen sie auch im Spam Ordner nach.")
				.setTitle("Information").setIcon(R.drawable.ic_launcher)

				.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
