package com.raydevelopers.sony.knowledgefactory.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by SONY on 04-05-2017.
 */

public class FactsContract {
    private FactsContract() {
        //Empty constructor
    }

    public static final String CONTENT_AUTHORITY = "com.raydevelopers.sony.knowledgefactory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH = "facts";

    public static final class FactsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH)
                .build();
        public static final String TABLE_NAME = "facts";
        public static final String COLUMN_FACT_NUMBER = "fact_number";
        public static final String COLUMN_FACT_TEXT = "fact_text";

    }
}
