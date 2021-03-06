package com.raydevelopers.sony.knowledgefactory;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raydevelopers.sony.knowledgefactory.data.FactsContract;
import com.raydevelopers.sony.knowledgefactory.ui.CustomPagerAdapter;
import com.raydevelopers.sony.knowledgefactory.ui.NetworkUtils;
import com.raydevelopers.sony.knowledgefactory.ui.VerticalViewPager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    CustomPagerAdapter mCustomPagerAdapter;
    VerticalViewPager mViewPager;
    private Toolbar toolbar;
    private SQLiteDatabase db;
    public static AlertDialog dialog;
    public static final String[] MAIN_FACTS_PROJECTION = {
            FactsContract.FactsEntry.COLUMN_FACT_NUMBER,
            FactsContract.FactsEntry.COLUMN_FACT_TEXT
    };
    public static final int INDEX_FACT_NUMBER = 0;
    public static final int INDEX_FACT_TEXT = 1;
    private static final int REQUEST_READ_STORAGE = 112;
    private static ContentValues[] values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        dialog = new SpotsDialog(MainActivity.this);
        mViewPager = (VerticalViewPager) findViewById(R.id.pager);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.ACCESS_NETWORK_STATE
            };

            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_READ_STORAGE);
            } else {
                getDataFromFirebase();

            }
        } else {
            // continue with your code
            getDataFromFirebase();

        }


    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                    getDataFromFirebase();
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri newsQueryUri = FactsContract.FactsEntry.CONTENT_URI;
        CursorLoader cl = new CursorLoader(this,
                newsQueryUri,
                MAIN_FACTS_PROJECTION,
                null,
                null,
                null);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this, data);

        mViewPager.setAdapter(mCustomPagerAdapter);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int pos = sharedPreferences.getInt(getString(R.string.sp_viewpager_pos), 0);
        if (pos != 0) {
            mViewPager.setCurrentItem(pos);
        }

        dialog.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveViewPagerPosition();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveViewPagerPosition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveViewPagerPosition();
    }

    public void saveViewPagerPosition() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(getString(R.string.sp_viewpager_pos), mViewPager.getCurrentItem());
        editor.apply();
    }

    public void getDataFromFirebase() {
        if (!NetworkUtils.isNetworkConnected(this)) {
            Snackbar.make(findViewById(R.id.ll), getString(R.string.no_internet), Snackbar.LENGTH_LONG).show();
            getSupportLoaderManager().initLoader(0, null, MainActivity.this);
        } else {
            dialog.show();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(getString(R.string.facts));

            final ContentResolver newsContentResolver = getContentResolver();
            newsContentResolver.delete(
                    FactsContract.FactsEntry.CONTENT_URI,
                    null,
                    null);
            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    values = new ContentValues[(int) dataSnapshot.getChildrenCount()];
                    int i = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ContentValues newsValues = new ContentValues();
                        newsValues.put(FactsContract.FactsEntry.COLUMN_FACT_NUMBER, ds.getKey());
                        newsValues.put(FactsContract.FactsEntry.COLUMN_FACT_TEXT, ds.getValue().toString());
                        values[i] = newsValues;
                        i++;
                    }
                    if (values != null && values.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */


                /* Insert our new data into ContentProvider */
                        newsContentResolver.bulkInsert(
                                FactsContract.FactsEntry.CONTENT_URI,
                                values);
                        getSupportLoaderManager().initLoader(0, null, MainActivity.this);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value

                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String share_string = "\n" + getString(R.string.share_string);
            String share_link = share_string+" " + getString(R.string.share_link);
            i.putExtra(Intent.EXTRA_TEXT, share_link);
            startActivity(Intent.createChooser(i, "choose one"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
