package net.trials;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Avichal Rakesh on 4/19/2015.
 */
public class SQLiteAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_CONTENT = "Content";
    public static final String KEY_DATE = "Date";
    // public static final String KEY_CONTINENT = "continent";
    //public static final String KEY_REGION = "region";

    private static final String TAG = "SQLiteAdpater";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "World";
    private static final String SQLITE_TABLE = "Country";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CONTENT + "," +
                    KEY_DATE + "," +
                    // KEY_CONTINENT + "," +
                    //KEY_REGION + "," +
                    " UNIQUE (" + KEY_CONTENT + "));";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public SQLiteAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public SQLiteAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createHistory(String content, String date) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CONTENT, content);
        initialValues.put(KEY_DATE, date);
        /*initialValues.put(KEY_CONTINENT, continent);
        initialValues.put(KEY_REGION, region);*/

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean removeHistory() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    /*public Cursor fetchCountriesByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null || inputText.length() == 0) {
            mCursor = mDb.query(SQLITE_TABLE, new String[]{KEY_ROWID,
                            KEY_CONTENT, KEY_DATE, KEY_CONTINENT, KEY_REGION},
                    null, null, null, null, null);

        } else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[]{KEY_ROWID,
                            KEY_CONTENT, KEY_DATE, KEY_CONTINENT, KEY_REGION},
                    KEY_DATE + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
*/
    public Cursor fetchAllHistory() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[]{KEY_ROWID,
                        KEY_CONTENT, KEY_DATE},
                null, null, null, null, KEY_ROWID + " desc", "20");

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

   /* public void insertSomeCountries() {

        createCountry("AFG", "Afghanistan", "Asia", "Southern and Central Asia");
        createCountry("ALB", "Albania", "Europe", "Southern Europe");
        createCountry("DZA", "Algeria", "Africa", "Northern Africa");
        createCountry("ASM", "American Samoa", "Oceania", "Polynesia");
        createCountry("AND", "Andorra", "Europe", "Southern Europe");
        createCountry("AGO", "Angola", "Africa", "Central Africa");
        createCountry("AIA", "Anguilla", "North America", "Caribbean");

    }*/

}
