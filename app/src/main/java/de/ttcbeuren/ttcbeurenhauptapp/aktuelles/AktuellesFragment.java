package de.ttcbeuren.ttcbeurenhauptapp.aktuelles;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.ttcbeuren.ttcbeurenhauptapp.MainActivityStartseite;
import de.ttcbeuren.ttcbeurenhauptapp.R;

public class AktuellesFragment extends Fragment {
	
	private static final String ARG_SECTION_NUMBER = "section_number";

	public AktuellesFragment newInstance(int sectionNumber) {
		AktuellesFragment fragment = new AktuellesFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;

	}

	@Override
	public void onAttach(Activity activity) {
		// Wichtig hier muss die Section angegeben werden ???
		super.onAttach(activity);
		((MainActivityStartseite) activity).onSectionAttached(1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// setHasOptionsMenu(true);
		
		
		View root = inflater.inflate(R.layout.fragment_aktuelles, container,
				false);
		
		
		// WebView browser = (WebView) root.findViewById(R.id.webviewakuelles);
		// browser.loadUrl("http://www.ttc-beuren.de");
		return root;
	}
}
