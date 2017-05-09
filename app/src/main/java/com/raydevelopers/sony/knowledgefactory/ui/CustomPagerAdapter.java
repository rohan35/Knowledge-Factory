package com.raydevelopers.sony.knowledgefactory.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.raydevelopers.sony.knowledgefactory.MainActivity;
import com.raydevelopers.sony.knowledgefactory.R;
import com.raydevelopers.sony.knowledgefactory.data.FactsContract;
import com.raydevelopers.sony.knowledgefactory.data.FactsDbHelper;

/**
 * Created by SONY on 04-05-2017.
 */

public class CustomPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private Cursor mCursor;

    public CustomPagerAdapter(FragmentManager fm, Context c, Cursor cursor) {
        super(fm);
        this.mContext = c;
        this.mCursor = cursor;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new DemoFragment();
        mCursor.moveToPosition(position);
        Bundle args = new Bundle();
        args.putString(mContext.getString(R.string.send_fact_text), mCursor.getString(MainActivity.INDEX_FACT_TEXT));
        args.putString(mContext.getString(R.string.send_fact_number), mCursor.getString(MainActivity.INDEX_FACT_NUMBER));
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }


}
