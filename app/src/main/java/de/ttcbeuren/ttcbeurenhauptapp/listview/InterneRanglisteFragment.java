/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.ttcbeuren.ttcbeurenhauptapp.listview;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.ttcbeuren.ttcbeurenhauptapp.MainActivityStartseite;
import de.ttcbeuren.ttcbeurenhauptapp.R;
import de.ttcbeuren.ttcbeurenhauptapp.ergebnisse.ErgebnisseFragment;

/**
 * This application creates a listview where the ordering of the data set can be
 * modified in response to user touch events.
 *
 * An item in the listview is selected via a long press event and is then moved
 * around by tracking and following the movement of the user's finger. When the
 * item is released, it animates to its new position within the listview.
 */
public class InterneRanglisteFragment extends Fragment {
	private static final String ARG_SECTION_NUMBER = "section_interneRangliste";

	public InterneRanglisteFragment newInstance(int sectionNumber) {
		InterneRanglisteFragment fragment = new InterneRanglisteFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;

	}

	@Override
	public void onAttach(Activity activity) {
		// Wichtig hier muss die Section angegeben werden ???
		super.onAttach(activity);
		((MainActivityStartseite) activity).onSectionAttached(3);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View root = inflater.inflate(R.layout.fragment_interne_rangliste, container,
				false);
		ArrayList<String> mCheeseList = new ArrayList<String>();
		for (int i = 0; i < Cheeses.sCheeseStrings.length; ++i) {
			mCheeseList.add(Cheeses.sCheeseStrings[i]);
		}

		StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),
				R.layout.text_view, mCheeseList);
		DynamicListView listView = (DynamicListView) root
				.findViewById(R.id.dnylistview);
		listView.setCheeseList(mCheeseList);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		return root;
	}

}
