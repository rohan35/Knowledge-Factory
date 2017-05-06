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
import com.raydevelopers.sony.knowledgefactory.data.FactsContract;
import com.raydevelopers.sony.knowledgefactory.data.FactsDbHelper;

/**
 * Created by SONY on 04-05-2017.
 */

public class CustomPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private Cursor mCursor;
    static int j=0;
    SQLiteDatabase db;
    public CustomPagerAdapter(FragmentManager fm, Context c, Cursor cursor) {
        super(fm);
        this.mContext=c;
        this.mCursor=cursor;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new DemoFragment();
        String number=getSaveState();
        mCursor.moveToPosition(position);

        if(number!=null)
        {
            for(int i=0;i<mCursor.getCount();i++) {
                if (number.equals(mCursor.getString(MainActivity.INDEX_FACT_NUMBER))) {
                    mCursor.moveToPosition(mCursor.getPosition());
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("factNumber").apply();
                    break;

                }
            }

        }

        Bundle args = new Bundle();
        args.putString("Fact", mCursor.getString(MainActivity.INDEX_FACT_TEXT));
        args.putString("factNumber",mCursor.getString(MainActivity.INDEX_FACT_NUMBER));
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
    public String getSaveState()
    {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
        String factNumber=sharedPreferences.getString("factNumber",null);
        if(factNumber==null)
        {
            return null;
        }
        else {
           // FactsDbHelper factsDbHelper=new FactsDbHelper(mContext);
            //factsDbHelper.getReadableDatabase();
            //String statement = "select * from " + FactsContract.FactsEntry.TABLE_NAME + " where " +
                   // FactsContract.FactsEntry.COLUMN_FACT_NUMBER + "="+factNumber;
            //Cursor c=db.rawQuery(statement,null);
            return factNumber;
        }
    }
}
