package de.ttcbeuren.ttcbeurenhauptapp.ergebnisse;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.ttcbeuren.ttcbeurenhauptapp.R;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.Spiel;

public class ListViewadapter extends BaseAdapter {
	/**
	 * http://javatechig.com/android/listview-with-section-header-in-android
	 */
	private int benutzer_id = 0;
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
	/**
	 * Nun wird ein ObjektArraylist erstellt. Da man so einmal Spiele und
	 * Strings gleichzeitig einfügen kann. Sonst bekommt man nämlich die Spiele
	 * nicht seperiert.
	 */
	private ArrayList<Object> mData = new ArrayList<Object>();
	private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

	private LayoutInflater mInflater;

	public ListViewadapter(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public void setBenutzer_id(final int benutzer_id) {
		this.benutzer_id = benutzer_id;
	}

	public void addItem(final Spiel item) {
		mData.add(item);
		notifyDataSetChanged();
	}

	public void addSectionHeaderItem(final String item) {
		mData.add(item);
		sectionHeader.add(mData.size() - 1);
		notifyDataSetChanged();
	}

	public void deletelist() {
		mData.clear();
		sectionHeader.clear();
		benutzer_id = -5;
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		int rowType = getItemViewType(position);
		
						if (convertView == null) {
			holder = new ViewHolder();
			switch (rowType) {
			case TYPE_ITEM:
				convertView = mInflater.inflate(
						R.layout.rowlayout_listviewadapter, null);
				holder.textView = (TextView) convertView
						.findViewById(R.id.textViewsdfdf);
				/**
				 * Ist der Benutzer der Benutzer der die Veranstaltung erstellt
				 * hat, wird dieser Eintrag anderst gefärbt.
				 */
				if (benutzer_id != -5) {
					Spiel spiels = (Spiel) mData.get(position);
					if (spiels.getBenutzer_id() == benutzer_id) {
						holder.textView
								.setBackgroundResource(R.color.ListGruen);
					}
				}

				break;
			case TYPE_SEPARATOR:
				convertView = mInflater.inflate(
						R.layout.sectionlayout_listviewadapter, null);
				holder.textView = (TextView) convertView
						.findViewById(R.id.textSeparator);
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textView.setText(mData.get(position).toString());

		return convertView;
	}

	public static class ViewHolder {
		public TextView textView;
	}

}