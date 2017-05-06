package com.raydevelopers.sony.knowledgefactory.ui;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raydevelopers.sony.knowledgefactory.R;
import com.raydevelopers.sony.knowledgefactory.data.FactsContract;
import com.raydevelopers.sony.knowledgefactory.data.FactsDbHelper;

/**
 * Created by SONY on 04-05-2017.
 */

public class DemoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get the arguments that was supplied when
        // the fragment was instantiated in the
        // CustomPagerAdapter
        Bundle args = getArguments();

        ((TextView) rootView.findViewById(R.id.text)).setText(args.getString("Fact"));
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("factNumber",args.getString("factNumber"));
        editor.apply();
        return rootView;
    }



}
